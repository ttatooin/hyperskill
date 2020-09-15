package correcter;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        final String fileSend = "send.txt";
        final String fileEncoded = "encoded.txt";
        final String fileReceived = "received.txt";
        final String fileDecoded = "decoded.txt";

        String inputFile;
        String outputFile;
        Function<byte[], byte[]> action;


        byte[] inputByteArray = null;
        byte[] outputByteArray = null;

        System.out.print("Write a mode: ");
        switch (scanner.nextLine()) {
            case "encode":
                inputFile = fileSend;
                outputFile = fileEncoded;
                action = Main::encodeHamming;
                break;

            case "send":
                inputFile = fileEncoded;
                outputFile = fileReceived;
                action = Main::addByteNoise;
                break;

            case "decode":
                inputFile = fileReceived;
                outputFile = fileDecoded;
                action = Main::decodeHamming;
                break;

            default:
                System.out.println("Unknown command.");
                return;
        }

        try (FileInputStream input = new FileInputStream(inputFile); FileOutputStream output = new FileOutputStream(outputFile)) {
            inputByteArray = new byte[input.available()];
            input.read(inputByteArray);
            outputByteArray = action.apply(inputByteArray);
            /**/ printByteArrayInBinary(inputByteArray);
            /**/printByteArrayInBinary(outputByteArray);
            output.write(outputByteArray);

        } catch (FileNotFoundException e) {
            System.out.println("Error: some file not found.");
            return;

        } catch (IOException e) {
            System.out.println("Error: something else wrong.");
            return;
        }
    }

    public static String addNoise(String text, Random random) {
        StringBuilder builder = new StringBuilder(text);
        for (int i = 0; i < text.length(); i+= 3) {
            int position = random.nextInt(3);
            char symbol;
            do {
                int symbolRaw = random.nextInt(26 + 26 + 1 + 10);
                if (symbolRaw < 26) {
                    symbol = (char) ('a' + symbolRaw);
                } else if (symbolRaw < 26 + 26) {
                    symbol = (char) ('A' + symbolRaw - 26);
                } else if (symbolRaw < 26 + 26 + 1) {
                    symbol = ' ';
                } else {
                    symbol = (char) ('0' + symbolRaw - 26 - 26 - 1);
                }
            } while (symbol == text.charAt(i + position));

            builder.setCharAt(i + position, symbol);
        }
        return builder.toString();
    }

    public static byte[] encode(byte[] byteArray) {
        byte[] result = new byte[byteArray.length * 8 / 3 + (byteArray.length * 8 % 3 > 0 ? 1 : 0)];

        int outputByte = 0;
        int outputBit = 0;

        for (int inputByte = 0; inputByte < byteArray.length; ++inputByte) {
            for (int inputBit = 0; inputBit < 8; ++inputBit) {
                if (outputBit < 3) {
                    result[outputByte] <<= 2;
                    result[outputByte] += (byteArray[inputByte] & (0b00000001 << (7 - inputBit))) != 0 ? 0b00000011 : 0b00000000;
                    ++outputBit;
                    if (inputByte == byteArray.length - 1 && inputBit == 7 || outputBit == 3) {
                        result[outputByte] <<= 2 * (4 - outputBit);
                        int sum = 0;
                        sum += (result[outputByte] & 0b10000000) != 0 ? 1 : 0;
                        sum += (result[outputByte] & 0b00100000) != 0 ? 1 : 0;
                        sum += (result[outputByte] & 0b00001000) != 0 ? 1 : 0;
                        sum %= 2;
                        result[outputByte] += (sum != 0) ? 0b00000011 : 0b00000000;
                        outputBit = 0;
                        ++outputByte;
                    }
                }
            }
        }

        return result;
    }

    public static byte[] repairThenDecode(byte[] byteArray) {
        return decode(repair(byteArray));
    }

    public static byte[] repair(byte[] byteArray) {
        byte[] result = new byte[byteArray.length];

        for (int i = 0; i < byteArray.length; ++i) {
            int sum = 0;
            sum += (byteArray[i] & 0b10000000) != 0 ? 1 : 0;
            sum += (byteArray[i] & 0b00100000) != 0 ? 1 : 0;
            sum += (byteArray[i] & 0b00001000) != 0 ? 1 : 0;
            sum += (byteArray[i] & 0b00000010) != 0 ? 1 : 0;
            sum %= 2;
            if (sum == 0) {
                result[i] += (byteArray[i] & 0b10000000);
                result[i] += (byteArray[i] & 0b10000000) >>> 1;
                result[i] += (byteArray[i] & 0b00100000);
                result[i] += (byteArray[i] & 0b00100000) >>> 1;
                result[i] += (byteArray[i] & 0b00001000);
                result[i] += (byteArray[i] & 0b00001000) >>> 1;
                result[i] += (byteArray[i] & 0b00000010);
                result[i] += (byteArray[i] & 0b00000010) >>> 1;
            } else {
                result[i] += (byteArray[i] & 0b01000000);
                result[i] += (byteArray[i] & 0b01000000) << 1;
                result[i] += (byteArray[i] & 0b00010000);
                result[i] += (byteArray[i] & 0b00010000) << 1;
                result[i] += (byteArray[i] & 0b00000100);
                result[i] += (byteArray[i] & 0b00000100) << 1;
                result[i] += (byteArray[i] & 0b00000001);
                result[i] += (byteArray[i] & 0b00000001) << 1;
            }
        }

        return result;
    }

    public static byte[] decode(byte[] byteArray) {
        byte[] result = new byte[byteArray.length * 3 / 8 + (byteArray.length * 3 % 8 > 0 ? 1 : 0)];

        int outputByte = 0;
        int outputBit = 0;

        for (int inputByte = 0; inputByte < byteArray.length; ++inputByte) {
            for (int inputBit = 0; inputBit < 3; ++inputBit) {
                result[outputByte] <<= 1;
                result[outputByte] += (byteArray[inputByte] & (0b00000100 << 2*(2 - inputBit))) != 0 ? 1 : 0;
                ++outputBit;
                if (outputBit == 8) {
                    outputBit = 0;
                    ++outputByte;
                }
            }
        }

        if (result[result.length - 1] == 0) {
            return Arrays.copyOf(result, result.length - 1);
        } else {
            return result;
        }
    }

    public static byte[] encodeHamming(byte[] byteArray) {
        byte[] result = new byte[byteArray.length * 2];

        for (int i = 0; i < byteArray.length; ++i) {
            result[2 * i] = encodeHamming4Bit((byte) ((byteArray[i] & 0b11110000) >>> 4));
            result[2 * i + 1] = encodeHamming4Bit((byte) ((byteArray[i] & 0b00001111)));
        }

        return result;
    }

    private static byte encodeHamming4Bit(byte element) {
        int bit1 = (element & 0b00001000) >>> 3;
        int bit2 = (element & 0b00000100) >>> 2;
        int bit3 = (element & 0b00000010) >>> 1;
        int bit4 = (element & 0b00000001);

        int key1 = (bit1 + bit2 + bit4) % 2;
        int key2 = (bit1 + bit3 + bit4) % 2;
        int key3 = (bit2 + bit3 + bit4) % 2;

        byte result = 0;
        result += (key1 << 7);
        result += (key2 << 6);
        result += (bit1 << 5);
        result += (key3 << 4);
        result += (bit2 << 3);
        result += (bit3 << 2);
        result += (bit4 << 1);

        return result;
    }

    public static byte[] decodeHamming(byte[] byteArray) {
        byte[] result = new byte[byteArray.length / 2];

        for (int i = 0; i < result.length; ++i) {
            result[i] = (byte) ((decodeHamming4Bit(byteArray[2 * i]) << 4) + decodeHamming4Bit(byteArray[2 * i + 1]));
        }

        return result;
    }

    private static byte decodeHamming4Bit(byte element) {
        int key1 = (element & 0b10000000) >>> 7;
        int key2 = (element & 0b01000000) >>> 6;
        int bit1 = (element & 0b00100000) >>> 5;
        int key3 = (element & 0b00010000) >>> 4;
        int bit2 = (element & 0b00001000) >>> 3;
        int bit3 = (element & 0b00000100) >>> 2;
        int bit4 = (element & 0b00000010) >>> 1;

        boolean test1 = (key1 + bit1 + bit2 + bit4) % 2 == 0;
        boolean test2 = (key2 + bit1 + bit3 + bit4) % 2 == 0;
        boolean test3 = (key3 + bit2 + bit3 + bit4) % 2 == 0;

        if (test1 == false && test2 == false && test3 == false) {
            bit4 = (bit4 + 1) % 2;
        } else if (test1 == false && test2 == false) {
            bit1 = (bit1 + 1) % 2;
        } else if (test1 == false && test3 == false) {
            bit2 = (bit2 + 1) % 2;
        } else if (test2 == false && test3 == false) {
            bit3 = (bit3 + 1) % 2;
        }

        byte result = 0;
        result += (bit1 << 3);
        result += (bit2 << 2);
        result += (bit3 << 1);
        result += bit4;

        return result;
    }

    public static byte[] addByteNoise(byte[] byteArray) {
        byte[] result = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; ++i) {
            result[i] = corruptRandomBit(byteArray[i]);
        }
        return result;
    }

    private static byte corruptRandomBit(byte number) {
        Random random = new Random();
        byte corruptBit = (byte) (0b00000001 << random.nextInt(8));
        return ((number & corruptBit) == 0) ? (byte) (number + corruptBit) : (byte) (number - corruptBit);
    }

    public static String encode(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            for (int j = 0; j < 3; ++j) {
                builder.append(text.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String decode(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i += 3) {
            char c;
            if (text.charAt(i) == text.charAt(i + 1) || text.charAt(i) == text.charAt(i + 2)) {
                c = text.charAt(i);
            } else {
                c = text.charAt(i + 1);
            }
            builder.append(c);
        }
        return builder.toString();
    }

    private static void printByteArrayInBinary(byte[] array) {
        for (byte element : array) {
            System.out.print(byteToBinaryString(element) + " ");
        }
        System.out.println();
    }

    private static String byteToBinaryString(byte element) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            builder.append((element & (0b00000001 << 7 - i)) != 0 ? "1" : "0");
        }
        return builder.toString();
    }

    private static String byteArrayToText(byte[] array) {
        StringBuilder builder = new StringBuilder();
        for (byte element : array) {
            builder.append((char) element);
        }
        return builder.toString();
    }
}
