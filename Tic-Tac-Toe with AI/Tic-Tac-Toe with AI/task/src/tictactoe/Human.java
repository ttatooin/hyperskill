package tictactoe;

import java.util.Scanner;

public class Human implements BoardPlayer {
    @Override
    public int getMove(Board board) {
        Scanner scanner = new Scanner(System.in);

        int x;
        int y;

        while (true) {
            System.out.print("Enter the coordinates: ");

            String[] input = scanner.nextLine().split(" ");

            try {
                x = Integer.parseInt(input[0]);
                y = Integer.parseInt(input[1]);
            } catch (Exception e) {
                System.out.println("You should enter numbers!");
                continue;
            }

            if (x < 1 || x > 3 || y < 1 || y > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            if (!board.canMove(x, y)) {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            break;
        }

        return (3 - y) * 3 + x - 1;
    }
}
