package tictactoe;

import java.util.Arrays;

public class HardBot implements BoardPlayer {

    @Override
    public int getMove(Board board) {
        System.out.println("Making move level \"hard\"");

        int[] field = convertField(board.exportField());
        int botSymbol = board.isXTurn() ? 0 : 1;

        int maxSum = -100_000_000;
        int result = -1;

        for (int i = 0; i < 9; i++) {
            if (field[i] == -1) {
                int[] fieldVariant = Arrays.copyOf(field, field.length);
                fieldVariant[i] = botSymbol;
                int currentSum = minmax(fieldVariant, botSymbol, (botSymbol + 1) % 2);
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                    result = i;
                }
            }
        }

        return result;
    }

    private int minmax(int[] field, int botSymbol, int currentPlayer) {
        if (isPlayerWin(field, botSymbol)) {
            return 10;
        } else if (isPlayerWin(field, (botSymbol + 1) % 2)) {
            return -10;
        } else if (isNoMoves(field)) {
                return 0;
        } else {
            int sum = 0;
            for (int i = 0; i < 9; ++i) {
                if (field[i] == -1) {
                    int[] nextField = Arrays.copyOf(field, field.length);
                    nextField[i] = currentPlayer;
                    sum += minmax(nextField, botSymbol, (currentPlayer + 1) % 2);
                }
            }
            return sum;
        }
    }

    private boolean isPlayerWin(int[] field, int player) {
        return field[0] == player && field[1] == player && field[2] == player ||
                field[3] == player && field[4] == player && field[5] == player ||
                field[6] == player && field[7] == player && field[8] == player ||
                field[0] == player && field[3] == player && field[6] == player ||
                field[1] == player && field[4] == player && field[7] == player ||
                field[2] == player && field[5] == player && field[8] == player ||
                field[0] == player && field[4] == player && field[8] == player ||
                field[2] == player && field[4] == player && field[6] == player;
    }

    private boolean isNoMoves(int[] field) {
        for (int i = 0; i < 9; ++i) {
            if (field[i] == -1) {
                return false;
            }
        }
        return true;
    }

    private int[] convertField(String field) {
        int[] result = new int[9];
        for (int i = 0; i < 9; ++i) {
            switch (field.charAt(i)) {
                case Board.xSymbol:
                    result[i] = 0;
                    break;
                case Board.oSymbol:
                    result[i] = 1;
                    break;
                default:
                    result[i] = -1;
                    break;
            }
        }
        return result;
    }
}
