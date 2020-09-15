package encryptdecrypt;

import java.util.Scanner;
//import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);

        String command = "enc";
        String text = "";
        int key = 0;
        String inFile = null;
        String outFile = null;
        CryptoMethod method = new Shift();

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-mode":
                    command = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    text = args[i + 1];
                    break;
                case "-in":
                    inFile = args[i + 1];
                    break;
                case "-out":
                    outFile = args[i + 1];
                    break;
                case "-alg":
                    switch (args[i + 1]) {
                        case "unicode":
                            method = new Unicode();
                    }
            }
        }

        if (inFile != null) {
            try (Scanner scanner = new Scanner(new File(inFile))) {
                text = scanner.nextLine();
            } catch (IOException exc) {
                // do nothing
            }
        }

        System.out.println("Processing: " + text);

        PrintWriter writer;
        if (outFile == null) {
            writer = new PrintWriter(System.out);
        } else {
            try {
                writer = new PrintWriter(new File(outFile));
            } catch (IOException exc) {
                return;
            }
        }

        switch (command) {
            case "enc":
                writer.println(method.encrypt(text, key));
                break;
            case "dec":
                writer.println(method.decrypt(text, key));
                break;
            default:
                System.out.println("Unknown command.");
                break;
        }

        writer.close();

    }
}
