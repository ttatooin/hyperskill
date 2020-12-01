package battleship;

class Position {

    public final int row;
    public final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position add(Position pos) {
        return new Position(this.row + pos.row, this.col + pos.col);
    }

    public Position iterate(int rowLength) {
        return new Position(row + col / rowLength, col % rowLength + 1);
    }

    public Position copyOf() {
        return new Position(row, col);
    }

    // Calculating $l_{\inf}$ norm.
    public int getLInfDistTo(Position pos) {
        return Math.abs(row - pos.row) + Math.abs(col - pos.col);
    }
}
