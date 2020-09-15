package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        Board board;

        exit:
        while (true) {
            while (true) {
                System.out.println("Input command: ");

                String[] input = scanner.nextLine().split(" ");

                if (input.length == 1 && input[0].equals("exit")) {
                    break exit;
                } else if (input.length == 3 && input[0].equals("start") && isCorrectPlayer(input[1]) && isCorrectPlayer(input[2])) {
                    board = new Board(createBoardPlayer(input[1]), createBoardPlayer(input[2]));
                    break;
                } else {
                    System.out.println("Bad parameters!");
                }
            }


            while (!board.isFinished()) {
                board.printState();
                board.makeMove();
            }
            System.out.println(board.getStringState());
            System.out.println();
        }
    }

    private static BoardPlayer createBoardPlayer(String name) {
        switch (name) {
            case "user":
                return new Human();
            case "easy":
                return new EasyBot();
            case "medium":
                return new MediumBot();
            case "hard":
                return new HardBot();
            default:
                return null;
        }
    }

    private static boolean isCorrectPlayer(String name) {
        return name.equals("user") || name.equals("easy") || name.equals("medium") || name.equals("hard");
    }
}
