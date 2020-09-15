package tictactoe;

import java.util.Scanner;

public class Board {

    static final char xSymbol = 'X';
    static final char oSymbol = 'O';
    static final char emptySymbol = '_';

    private BoardState state = BoardState.GAME_NOT_FINISHED;
    private boolean ifXTurn = true;
    private char[][] field = {{emptySymbol, emptySymbol, emptySymbol}, {emptySymbol, emptySymbol, emptySymbol}, {emptySymbol, emptySymbol, emptySymbol}};

    private BoardPlayer playerX;
    private BoardPlayer playerO;

    public Board(BoardPlayer playerX, BoardPlayer playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    public void loadField (String data) {
        for (int i = 0; i < 9; i++) {
            field[i / 3][i % 3] = data.charAt(i);
        }

        updateState();
    }

    public String exportField() {
        String result = "";

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                result += field[i][j];
            }
        }

        return result;
    }

    public boolean isFinished() {
        return state != BoardState.GAME_NOT_FINISHED;
    }

    public String getStringState() {
        return state.getText();
    }

    public boolean isXTurn() {
        return ifXTurn;
    }

    public void printState() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    private void updateState() {
        int xCount = countCells(xSymbol);
        int oCount = countCells(oSymbol);

        ifXTurn = (xCount <= oCount);

        if (checkWin(xSymbol)) {
            state = BoardState.X_WINS;
        } else if (checkWin(oSymbol)) {
            state = BoardState.O_WINS;
        } else if (xCount + oCount == 9) {
            state = BoardState.DRAW;
        }
    }

    int countCells(char type) {
        int result = 0;
        for (char[] raw : field) {
            for (char cell : raw) {
                result += (cell == type) ? 1 : 0;
            }
        }

        return result;
    }

    private boolean checkWin(char type) {
        return field[0][0] == type && field[0][1] == type && field[0][2] == type ||
                field[1][0] == type && field[1][1] == type && field[1][2] == type ||
                field[2][0] == type && field[2][1] == type && field[2][2] == type ||
                field[0][0] == type && field[1][0] == type && field[2][0] == type ||
                field[0][1] == type && field[1][1] == type && field[2][1] == type ||
                field[0][2] == type && field[1][2] == type && field[2][2] == type ||
                field[0][0] == type && field[1][1] == type && field[2][2] == type ||
                field[0][2] == type && field[1][1] == type && field[2][0] == type;
    }

    static boolean checkWin(char type, String field) {
        return field.charAt(0) == type && field.charAt(1) == type && field.charAt(2) == type ||
                field.charAt(3) == type && field.charAt(4) == type && field.charAt(5) == type ||
                field.charAt(6) == type && field.charAt(7) == type && field.charAt(8) == type ||
                field.charAt(0) == type && field.charAt(3) == type && field.charAt(6) == type ||
                field.charAt(1) == type && field.charAt(4) == type && field.charAt(7) == type ||
                field.charAt(2) == type && field.charAt(5) == type && field.charAt(8) == type ||
                field.charAt(0) == type && field.charAt(4) == type && field.charAt(8) == type ||
                field.charAt(2) == type && field.charAt(4) == type && field.charAt(6) == type;
    }

    public void makeMove() {
        int move = ifXTurn ? playerX.getMove(this) : playerO.getMove(this);
        field[move / 3][move % 3] = ifXTurn ? xSymbol : oSymbol;
        updateState();
    }

    public boolean canMove(int x, int y) {
        int realX = 4 - y - 1;
        int realY = x - 1;

        return field[realX][realY] == emptySymbol;
    }
}
