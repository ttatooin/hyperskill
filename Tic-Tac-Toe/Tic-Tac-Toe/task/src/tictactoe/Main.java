package tictactoe;

import java.util.Scanner;

class Board {
    public final char[][] field = {{'_', '_', '_'}, {'_', '_', '_'}, {'_', '_', '_'}};

    public BoardState state = BoardState.GAME_NOT_FINISHED;
    private boolean playerXTurn = true;

    public void printBoard() {
        System.out.println("---------");

        for (int i = 0; i < 3; ++i) {
            System.out.print("| ");
            for (int j = 0; j < 3; ++j)
            {
                System.out.print(field[i][j] + " ");
            }
            System.out.println("|");
        }

        System.out.println("---------");
    }

    public void analyzeField() {
        if (ifRaw('X') && ifRaw('O') || !checkMoves()) {
            state = BoardState.IMPOSSIBLE;
        } else if (ifRaw('X')) {
            state = BoardState.X_WINS;
        } else if (ifRaw('O')) {
            state = BoardState.O_WINS;
        } else if (!ifFull()) {
            state = BoardState.GAME_NOT_FINISHED;
        } else {
            state = BoardState.DRAW;
        }
    }

    public void loadBoard(String data) {
        int x = 0;
        int o = 0;

        for (int i = 0; i < data.length(); ++i) {
            field[i / 3][i % 3] = data.charAt(i);
            x += data.charAt(i) == 'X' ? 1 : 0;
            o += data.charAt(i) == 'O' ? 1 : 0;
        }

        playerXTurn = x <= o;
    }

    public void makeMove() {
        Scanner scanner = new Scanner(System.in);

        int moveX;
        int moveY;

        int preMoveX;
        int preMoveY;

        while (true) {
            System.out.print("Enter the coordinates: ");

            if (scanner.hasNextInt()) {
                preMoveX = scanner.nextInt();
                if (scanner.hasNextInt()) {
                    preMoveY = scanner.nextInt();
                }
                else {
                    System.out.println("You should enter numbers!");
                    continue;
                }
            } else {
                System.out.println("You should enter numbers!");
                continue;
            }

            moveX = 4 - preMoveY;
            moveY = preMoveX;

            if (moveX < 1 || moveX > 3 || moveY < 1 || moveY > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            } else if (field[moveX - 1][moveY - 1] != '_') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            } else {
                field[moveX - 1][moveY - 1] = playerXTurn ? 'X' : 'O';
                playerXTurn = !playerXTurn;
                //field[moveX - 1][moveY - 1] = 'X';
                break;
            }

        }
    }

    private boolean ifRaw(char c) {
        if (c == field[0][0] && c == field[0][1] && c == field[0][2] ||
                c == field[1][0] && c == field[1][1] && c == field[1][2] ||
                c == field[2][0] && c == field[2][1] && c == field[2][2] ||
                c == field[0][0] && c == field[1][0] && c == field[2][0] ||
                c == field[0][1] && c == field[1][1] && c == field[2][1] ||
                c == field[0][2] && c == field[1][2] && c == field[2][2] ||
                c == field[0][0] && c == field[1][1] && c == field[2][2] ||
                c == field[0][2] && c == field[1][1] && c == field[2][0]) {

            return true;
        } else {
            return false;
        }
    }

    private boolean ifFull() {
        for (char[] raw : field) {
            for (char cell : raw) {
                if (cell == '_') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkMoves() {
        int x = 0;
        int o = 0;

        for (char[] raw : field) {
            for (char cell : raw) {
                x += cell == 'X' ? 1 : 0;
                o += cell == 'O' ? 1 : 0;
            }
        }

        if (x - o == 0 || x - o == 1 || x - o == -1) {
            return true;
        } else {
            return false;
        }
    }
}

enum BoardState {
    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins"),
    IMPOSSIBLE("Impossible");

    String text;

    BoardState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

public class Main {
    public static void main(String[] args) {
        // write your code here
        Board board = new Board();
        board.printBoard();

        while (board.state == BoardState.GAME_NOT_FINISHED) {
            board.makeMove();
            board.printBoard();
            board.analyzeField();
        }

        System.out.println(board.state.getText());
    }
}
