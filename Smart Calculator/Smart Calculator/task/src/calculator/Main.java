package calculator;

import java.util.Scanner;

public class Main {

    public static final String CMD_EXIT = "/exit";
    public static final String CMD_HELP = "/help";
    public static final String STR_UNKNOWN_COMMAND = "Unknown command";

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("")) {
                continue;
            } else if (input.matches("^\\s*/.*")) {
                switch (input.trim()) {
                    case CMD_HELP:
                        System.out.println("The program calculates arithmetic expressions.");
                        System.out.println("Supported operations: +, -, *, /, ^.");
                        System.out.println("Assigning variable: <name> = <expression>.");
                        System.out.println("Space symbols are ignored.");
                        break;
                    case CMD_EXIT:
                        System.out.println("Bye!");
                        return;
                    default:
                        System.out.println(STR_UNKNOWN_COMMAND);
                        break;
                }
            } else {
                String result = calc.process(input);
                if (result != null) {
                    System.out.println(result);
                    continue;
                }
            }
        }
    }
}
