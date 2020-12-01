package bullscows;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grader grader;
        String input;

        System.out.println("Input the length of the secret code:");
        input = scanner.next();
        if (!input.matches("\\d+") || input.matches("0")) {
            System.out.println("Error: " + "\"" + input + "\"" + " isn't a valid number.");
            return;
        }
        int secretCodeLength = Integer.parseInt(input);

        System.out.println("Input the number of possible symbols in the code:");
        input = scanner.next();
        if (!input.matches("\\d+") || input.matches("0")) {
            System.out.println("Error: " + "\"" + input + "\"" + " isn't a valid number.");
            return;
        }
        int symbolsQuantitySet = Integer.parseInt(input);

        if (symbolsQuantitySet > 36 || secretCodeLength > symbolsQuantitySet) {
            System.out.println("Error: can't generate a secret code with a length of " +
                    secretCodeLength + " because there aren't enough unique symbols.");
            return;
        } else {
            grader = new Grader(generateSecretNumber(secretCodeLength, symbolsQuantitySet));
            System.out.print("The secret number is prepared: ");
            for (int k = 0; k < secretCodeLength; ++k) {
                System.out.print("*");
            }
            System.out.print(" (0");
            if (symbolsQuantitySet > 1) {
                System.out.print("-" + Math.min(9, symbolsQuantitySet - 1));
            }
            if (symbolsQuantitySet > 10) {
                System.out.print(", a");
                if (symbolsQuantitySet > 11) {
                    System.out.print("-" + (char) ('a' + symbolsQuantitySet - 10 - 1));
                }
            }
            System.out.println(").");
            System.out.println("Okay, let's start a game");
        }

        int turn = 0;
        while (true) {
            ++turn;
            System.out.println("Turn " + turn + ":");
            input = scanner.next();
            GradeAnswer answer = grader.grade(input);
            System.out.println(answer.toString());
            if (answer.isGuessed) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }
        }
    }

    public static Character[] generateSecretNumber(int length, int symbols) {
        Random random = new Random();
        List<Character> symbolsSet = new LinkedList<>();
        for (int k = 0; k < 10 && symbols > 0; ++k) {
            symbolsSet.add((char) ('0' + k));
            --symbols;
        }
        for (int k = 0; k < 26 && symbols > 0; ++k) {
            symbolsSet.add((char) ('a' + k));
            --symbols;
        }

        Character[] secretCode = new Character[length];
        for (int k = 0; k < length; ++k) {
            int retrievedIndex = random.nextInt(symbolsSet.size());
            secretCode[k] = symbolsSet.get(retrievedIndex);
            symbolsSet.remove(retrievedIndex);
        }
        return secretCode;
    }
}
