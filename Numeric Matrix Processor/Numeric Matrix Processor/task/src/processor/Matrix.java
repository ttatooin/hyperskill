package processor;

public class Matrix {

    protected int rowNumber;
    protected int colNumber;
    double[][] elements;

    public Matrix(int rowNumber, int colNumber) {
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        elements = new double[rowNumber][colNumber];
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public void setElement(int row, int col, double value) {
        elements[row][col] = value;
    }

    public double getElement(int row, int col) {
        return elements[row][col];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (double[] row : elements) {
            for (double element : row) {
                builder.append(element);
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String toIntString() {
        StringBuilder builder = new StringBuilder();
        for (double[] row : elements) {
            for (double element : row) {
                builder.append((int)element + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String toStringLine() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (double[] row : elements) {
            for (double element : row) {
                builder.append(element);
                builder.append(" ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public Matrix add(Matrix matrix) throws ArithmeticException {
        if (rowNumber != matrix.rowNumber || colNumber != matrix.colNumber) {
            throw new ArithmeticException();
        }

        Matrix result = new Matrix(rowNumber, colNumber);

        for (int i = 0; i < rowNumber; ++i) {
            for (int j = 0; j < colNumber; ++j) {
                result.setElement(i, j, elements[i][j] + matrix.elements[i][j]);
            }
        }

        return result;
    }

    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(rowNumber, colNumber);

        for (int i = 0; i < rowNumber; ++i) {
            for (int j = 0; j < colNumber; ++j) {
                result.setElement(i, j, elements[i][j] * scalar);
            }
        }

        return result;
    }

    public Matrix multiply(Matrix matrix) throws ArithmeticException {
        if (colNumber != matrix.rowNumber) {
            throw new ArithmeticException();
        }

        Matrix result = new Matrix(rowNumber, matrix.colNumber);

        for (int i = 0; i < result.rowNumber; ++i) {
            for (int j = 0; j < result.colNumber; ++j) {
                double element = 0;
                for (int k = 0; k < colNumber; ++k) {
                    element += elements[i][k] * matrix.elements[k][j];
                }
                result.setElement(i, j, element);
            }
        }

        return result;
    }

    public Matrix transposeMain() {
        Matrix result = new Matrix(colNumber, rowNumber);

        for (int i = 0; i < result.rowNumber; ++i) {
            for (int j = 0; j < result.colNumber; ++j) {
                result.setElement(i, j, elements[j][i]);
            }
        }

        return result;
    }

    public Matrix transposeSide() {
        Matrix result = new Matrix(colNumber, rowNumber);

        for (int i = 0; i < result.rowNumber; ++i) {
            for (int j = 0; j < result.colNumber; ++j) {
                result.setElement(i, j, elements[rowNumber - 1 - j][colNumber - 1 - i]);
            }
        }

        return result;
    }

    public Matrix transposeVertical() {
        Matrix result = new Matrix(rowNumber, colNumber);

        for (int i = 0; i < result.rowNumber; ++i) {
            for (int j = 0; j < result.colNumber; ++j) {
                result.setElement(i, j, elements[i][colNumber - 1 - j]);
            }
        }

        return result;
    }

    public Matrix transposeHorizontal() {
        Matrix result = new Matrix(rowNumber, colNumber);

        for (int i = 0; i < result.rowNumber; ++i) {
            for (int j = 0; j < result.colNumber; ++j) {
                result.setElement(i, j, elements[rowNumber - 1 - i][j]);
            }
        }

        return result;
    }

    public double getDeterminant() throws ArithmeticException {
        if (rowNumber != colNumber) {
            throw new ArithmeticException("Can't calculate deteminant is matrix is not square.");
        }

        if (rowNumber == 1) {
            return elements[0][0];
        } else {
            double result = 0;
            for (int j = 0; j < colNumber; ++j) {
                result += (j % 2 == 0 ? 1 : -1) * elements[0][j] * getMinor(0, j).getDeterminant();
            }
            return result;
        }
    }

    public Matrix getMinor(int row, int col) {
        Matrix result = new Matrix(rowNumber - 1, colNumber - 1);

        for (int i = 0; i < result.rowNumber; ++i) {
            for (int j = 0; j < result.colNumber; ++j) {
                result.elements[i][j] = elements[i < row ? i : i + 1][j < col ? j : j + 1];
            }
        }

        return result;
    }

    public Matrix inverse() throws ArithmeticException{
        if (rowNumber != colNumber) {
            throw new ArithmeticException("Can't calculate deteminant is matrix is not square.");
        }

        Matrix result = new Matrix(rowNumber, colNumber);

        for (int i = 0; i < rowNumber; ++i) {
            for (int j = 0; j < colNumber; ++j) {
                result.elements[i][j] = ((i + j) % 2 == 0 ? 1 : -1) * getMinor(j, i).getDeterminant();
            }
        }

        return result.multiply(1 / getDeterminant());
    }
}
