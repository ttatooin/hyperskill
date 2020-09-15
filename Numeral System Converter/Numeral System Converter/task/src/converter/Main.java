package converter;

import java.util.Scanner;

public class Main {
    public static double convertToDecimal(String number, int radix) {

        String[] numberParts = number.split("\\.");

        String integerNumberPart = numberParts[0];
        String fractionalNumberPart = numberParts.length > 1 ? numberParts[1] : "0";

        ///**/System.out.println(number + " splitted to " + integerNumberPart + " and "+ fractionalNumberPart);

        double sign = number.charAt(0) == '-' ? -1 : 1;
        double integer;
        double fractional;

        if (radix == 1) {
            integer = sign > 0 ? integerNumberPart.length() : integerNumberPart.length() - 1;
            fractional = 0;
        } else {
            integer = Integer.parseInt(integerNumberPart, radix);
            fractional = 0;
            for (int i = 1; i <= fractionalNumberPart.length(); ++i) {
                String digit = ((Character)fractionalNumberPart.charAt(i - 1)).toString();
                fractional += 1.0 * Integer.parseInt(digit, radix) / Math.pow(radix, i);
                ///**/System.out.println(" digit = " + digit + ", fractional = " + fractional);
            }
        }

        return sign * integer + fractional;
    }

    public static String convertFromDecimal(double number, int radix) {

        int signNumberPart = number < 0 ? -1 : 1;
        int integerNumberPart = signNumberPart * Integer.parseInt(Double.toString(number).split("\\.")[0]);
        double fractionalNumberPart = signNumberPart * number - integerNumberPart;

        ///**/System.out.println(number + " splitted to " + integerNumberPart + " and " + fractionalNumberPart);

        String sign = signNumberPart == 1 ? "" : "-";
        String integer = "";
        String fractional = "";

        if (radix == 1) {
            for (int i = 0; i < integerNumberPart; i++) {
                integer += "1";
            }
            fractional = "00000";
        } else {
            integer += Integer.toString(integerNumberPart, radix);
            int tempIntegerPart;
            double tempFractional = fractionalNumberPart;
            int digitsFound = 1;
            while ((tempIntegerPart = (int) (tempFractional * radix)) > 0 && digitsFound <= 5) {
                ///**/System.out.print(" *base = " + (tempFractional * radix) + ", int-part = " + tempIntegerPart);
                fractional += Integer.toString(tempIntegerPart, radix);
                ///**/System.out.println(", fractional = " + fractional);
                tempFractional = tempFractional * radix - tempIntegerPart;
                ++digitsFound;
            }
        }

        return sign + integer + "." + fractional;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int radixFrom;
        String input;
        int radixTo;

        try {
            radixFrom = scanner.nextInt();
            input = scanner.next();
            radixTo = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("error");
            return;
        }

        if (radixFrom < 1 || radixFrom > 36 || radixTo < 1 || radixTo > 36) {
            System.out.println("error");
        } else {
            double intermediateResult = convertToDecimal(input, radixFrom);
            String result = convertFromDecimal(intermediateResult, radixTo);

            System.out.println(result);
        }
    }
}
