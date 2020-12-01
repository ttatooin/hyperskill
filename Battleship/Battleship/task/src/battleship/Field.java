package battleship;

import java.util.Iterator;
import java.util.NoSuchElementException;

class Field implements Iterable<Character> {

    public enum ErrorCode {
        COORDINATES_OUT_OF_FIELD("Invalid ship coordinates: coordinates out of field."),
        COORDINATES_NOT_ON_LINE("Invalid ship coordinates: ship in not horizontal or vertical."),
        ANOTHER_SHIP_IN_PLACE("Invalid ship coordinates: another ship here."),
        ANOTHER_SHIP_TOO_CLOSE("Invalid ship coordinates: you placed it too close to another one.");

        String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }

    public static final char SYMBOL_SHIP_CELL = 'O';
    public static final char SYMBOL_FREE_CELL = '~';
    public static final char SYMBOL_HIT_CELL = 'X';
    public static final char SYMBOL_MISS_CELL = 'M';

    private char[][] field;
    public final int rowNumber;
    public final int colNumber;

    public Field(int rowNumber, int colNumber) {
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        field = new char[rowNumber][colNumber];
        fill(SYMBOL_FREE_CELL);
    }

    private void fill(char symbol) {
        for (int n = 0; n < rowNumber; ++n) {
            for (int k = 0; k < colNumber; ++k) {
                field[n][k] = SYMBOL_FREE_CELL;
            }
        }
    }

    public char get(Position pos) {
        return field[pos.row - 1][pos.col - 1];
    }

    public void set(Position pos, char value) {
        field[pos.row - 1][pos.col - 1] = value;
    }

    public String toStringRevealed() {
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        for (int k = 1; k <= 10; ++k) {
            builder.append(" ");
            builder.append(k);
        }
        builder.append("\n");
        for (int n = 1; n <= 10; ++n) {
            builder.append((char) ('A' + n - 1));
            for (int k = 1; k <= 10; ++k) {
                builder.append(" ");
                builder.append(get(new Position(n, k)));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String toStringFogged() {
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        for (int k = 1; k <= 10; ++k) {
            builder.append(" ");
            builder.append(k);
        }
        builder.append("\n");
        for (int n = 1; n <= 10; ++n) {
            builder.append((char) ('A' + n - 1));
            for (int k = 1; k <= 10; ++k) {
                builder.append(" ");
                if (get(new Position(n, k)) != SYMBOL_SHIP_CELL) {
                    builder.append(get(new Position(n, k)));
                } else {
                    builder.append(SYMBOL_FREE_CELL);
                }

            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public boolean isShipAlive(Position someShipCell) {
        Position[] deltas = new Position[] {new Position(1, 0),
                                           new Position(-1, 0),
                                           new Position(0, 1),
                                           new Position(0, -1)};
        for (Position delta : deltas) {
            Position pos = someShipCell.copyOf();
            cycle:
            while (isPositionCorrect(pos)) {
                switch (get(pos)) {
                    case SYMBOL_SHIP_CELL:
                        return true;
                    case SYMBOL_HIT_CELL:
                        pos = pos.add(delta);
                        continue;
                    default:
                        break cycle;
                }
            }
        }
        return false;
    }

    public boolean existsAliveCell() {
        for (char cell : this) {
            if (cell == SYMBOL_SHIP_CELL) {
                return true;
            }
        }
        return false;
    }

    public boolean isPositionCorrect(Position pos) {
        return pos.row >= 1 && pos.row <= rowNumber && pos.col >=1 && pos.col <= colNumber;
    }

    public ErrorCode addShip(Position head, Position tail) {
        //  Checking positions to be in field.
        if (!isPositionCorrect(head) || !isPositionCorrect(tail)) {
            return ErrorCode.COORDINATES_OUT_OF_FIELD;
        }
        // Checking ship to be horizontal or vertical.
        if (head.row - tail.row != 0 && head.col - tail.col != 0) {
            return ErrorCode.COORDINATES_NOT_ON_LINE;
        }
        // Calculation ship cells coordinates.
        Position delta = new Position((int) Math.signum(tail.row - head.row), (int) Math.signum(tail.col - head.col));
        Position[] shipCells = new Position[head.getLInfDistTo(tail) + 1];
        shipCells[0] = head.copyOf();
        for (int n = 1; n < shipCells.length; ++n) {
            shipCells[n] = shipCells[n - 1].add(delta);
        }
        // Checking new ship not intersecting with old one.
        for (int n = 0; n < shipCells.length; ++n) {
            if (get(shipCells[n]) != SYMBOL_FREE_CELL ) {
                return ErrorCode.ANOTHER_SHIP_IN_PLACE;
            }
        }
        // Checking new ship not to be place near old one.
        for (int n = Math.min(head.row, tail.row) - 1; n <= Math.max(head.row, tail.row) + 1; ++n) {
            for (int k = Math.min(head.col, tail.col) - 1; k <= Math.max(head.col, tail.col) + 1; ++k) {
                if (isPositionCorrect(new Position(n, k)) && get(new Position(n, k)) != SYMBOL_FREE_CELL) {
                    return ErrorCode.ANOTHER_SHIP_TOO_CLOSE;
                }
            }
        }
        // Placing ship.
        for (Position cell : shipCells) {
            set(cell, SYMBOL_SHIP_CELL);
        }
        return null;
    }

    public boolean attackCell(Position pos) {
        if (get(pos) == SYMBOL_SHIP_CELL || get(pos) == SYMBOL_HIT_CELL) {
            set(pos, SYMBOL_HIT_CELL);
            return true;
        } else {
            set(pos, SYMBOL_MISS_CELL);
            return false;
        }
    }

    public Iterator<Character> iterator() {
        return new Iterator<Character>() {

            Position currPos = new Position(0, colNumber);

            @Override
            public boolean hasNext() {
                return currPos.row < rowNumber || currPos.col < colNumber;
            }

            @Override
            public Character next() throws NoSuchElementException {
                if (hasNext()) {
                    currPos = currPos.iterate(rowNumber);
                    return get(currPos);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
