package solver;

public class Matrix {

    private int rowNumber;
    private int colNumber;
    Complex[][] cells;

    public Matrix(int rowNumber, int colNumber) {
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        cells = new Complex[rowNumber][colNumber];
    }

    public Matrix(Complex[][] matrix) {
        this.rowNumber = matrix.length;
        this.colNumber = matrix[0].length;
        cells = new Complex[rowNumber][colNumber];

        for (int i = 0; i < rowNumber; ++i) {
            for (int j = 0; j < colNumber; ++j) {
                cells[i][j] = matrix[i][j];
            }
        }
    }

    public boolean isRowNull(int row) {
        for (int j = 0; j < colNumber; ++j) {
            if (!cells[row][j].isZero()) {
                return false;
            }
        }
        return true;
    }

    public void swapRows(int row1, int row2) {
        if (row1 != row2) {
            Complex temp;
            for (int i = 0; i < colNumber; ++i) {
                temp = cells[row1][i];
                cells[row1][i] = cells[row2][i];
                cells[row2][i] = temp;
            }
        }
    }

    public void addRowToRowMultipliedBy(int what, Complex num, int to) {
        for (int i = 0; i < colNumber; ++i) {
            cells[to][i] = cells[to][i].add(num.multiply(cells[what][i]));
        }
    }

    public Complex getCell(int row, int col) {
        return cells[row][col];
    }

    public void setCell(int row, int col, Complex value) {
        cells[row][col] = value;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rowNumber; ++i) {
            for (int j = 0; j < colNumber; ++j) {
                builder.append(cells[i][j] + " ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public Matrix copyOf() {
        Matrix result = new Matrix(rowNumber, colNumber);
        for (int i = 0; i < rowNumber; ++i) {
            for (int j = 0; j < colNumber; ++j) {
                result.cells[i][j] = this.cells[i][j];
            }
        }
        return result;
    }
}
