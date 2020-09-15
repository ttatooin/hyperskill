package tictactoe;

public enum BoardState {

    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins");

    private String text;

    BoardState(String output) {
        text = output;
    }

    public String getText() {
        return text;
    }
}
