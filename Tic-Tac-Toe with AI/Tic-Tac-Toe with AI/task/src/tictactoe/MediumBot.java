package tictactoe;

import java.util.Random;

public class MediumBot implements  BoardPlayer{
    Random random = new Random();

    @Override
    public int getMove(Board board) {
        System.out.println("Making move level \"medium\"");

        String field = board.exportField();
        boolean ifXTurn = board.isXTurn();
        char botSymbol = ifXTurn ? Board.xSymbol : Board.oSymbol;
        char opponentSymbol = ifXTurn ? Board.oSymbol : Board.xSymbol;

        int botWinMove = findOneMoveWin(botSymbol, field);
        if (botWinMove != -1) {
            return botWinMove;
        }

        int opponentWinMove = findOneMoveWin(opponentSymbol, field);
        if (opponentWinMove != -1) {
            return opponentWinMove;
        }

        int emptyCellNumber = random.nextInt(board.countCells(Board.emptySymbol)) + 1;

        int result = -1;
        for (int i = 0; emptyCellNumber > 0; ++i) {
            if (field.charAt(i) == Board.emptySymbol) {
                --emptyCellNumber;
                result = i;
            }
        }

        return result;
    }

    private int findOneMoveWin(char player, String field) {

        for (int i = 0; i < field.length(); ++i) {
            if (field.charAt(i) != Board.emptySymbol) {
                continue;
            } else {
                StringBuilder testField = new StringBuilder(field);
                testField.setCharAt(i, player);
                if (Board.checkWin(player, testField.toString())) {
                    return i;
                } else {
                    continue;
                }
            }
        }

        return -1;
    }

}
