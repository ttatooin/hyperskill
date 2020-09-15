package tictactoe;

import java.util.Random;

public class EasyBot implements  BoardPlayer {

    Random random = new Random();

    @Override
    public int getMove(Board board) {
        System.out.println("Making move level \"easy\"");

        String field = board.exportField();

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
}
