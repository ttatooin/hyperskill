package solver;

public class LinearEquation {

    Matrix coefficients;
    Matrix freeCol;

    int equationsNum;
    int variablesNum;

    public LinearEquation(Matrix extended) {
        equationsNum = extended.getRowNumber();
        variablesNum = extended.getColNumber() - 1;
        coefficients = new Matrix(equationsNum, variablesNum);
        freeCol = new Matrix(equationsNum, 1);
        for (int i = 0; i < equationsNum; ++i) {
            freeCol.setCell(i, 0, extended.getCell(i, variablesNum));
            for (int j = 0; j < variablesNum; ++j) {
                coefficients.setCell(i, j, extended.getCell(i, j));
            }
        }
    }

    public void makeEchelon() {
        /*Debug*/ System.out.println("Matrix before echeloning:");
        /*Debug*/ System.out.println(coefficients.toString());
        int currentRow = 0;
        int currentCol = 0;
        while (currentCol < variablesNum) {
            swapEquations(currentRow, findGreatestAmongBelow(currentRow, currentCol));
            if (coefficients.getCell(currentRow, currentCol).isZero()) {
                ++currentCol;
                continue;
            } else {
                for (int i = currentRow + 1; i < equationsNum; ++i) {
                    Complex factor = coefficients.getCell(currentRow, currentCol).reverse().multiply(coefficients.getCell(i, currentCol)).negate();
                    addEquations(currentRow, factor, i);
                    coefficients.setCell(i, currentCol, new Complex(0, 0));
                }
                ++currentRow;
                ++currentCol;
            }
            //*Debug*/ System.out.println(coefficients.toString());
        }
        /*Debug*/ System.out.println("Echelon part 1 completed.");
        /*Debug*/ System.out.println(coefficients.toString());
        --currentRow;
        while (currentRow > 0) {
            currentCol = 0;
            while (currentCol < variablesNum && coefficients.getCell(currentRow, currentCol).isZero()) {
                ++currentCol;
            }
            for (int i = currentRow - 1; i >= 0; --i) {
                Complex factor = coefficients.getCell(currentRow, currentCol).reverse().multiply(coefficients.getCell(i, currentCol)).negate();
                addEquations(currentRow, factor, i);
                coefficients.setCell(i, currentCol, new Complex(0, 0));
            }
            --currentRow;
        }
        /*Debug*/ System.out.println("Echelon part 2 completed.");
        /*Debug*/ System.out.println(coefficients.toString());
    }

    private int findGreatestAmongBelow(int row, int col) {
        int result = row;
        double greatestModule = 0;
        for (int i = row; i < equationsNum; ++i) {
            if (greatestModule < coefficients.getCell(i, col).absolute()) {
                greatestModule = coefficients.getCell(i, col).absolute();
                result = i;
            }
        }
        return result;
    }

    private void swapEquations(int eq1, int eq2) {
        if (eq1 != eq2) {
            coefficients.swapRows(eq1, eq2);
            freeCol.swapRows(eq1, eq2);
            System.out.println("R" + eq1 + " <-> R" + eq2);
        }
    }

    private void addEquations(int eq1, Complex factor, int eq2) {
        if (eq1 != eq2) {
            coefficients.addRowToRowMultipliedBy(eq1, factor, eq2);
            freeCol.addRowToRowMultipliedBy(eq1, factor, eq2);
            System.out.println(factor + " * R" + eq1 + " + R" + eq2 + " -> R" + eq2);
        }
    }

    // No solutions > return null
    // Infinite solutions > return array with length 0
    public Complex[] solve() {
        makeEchelon();
        int lastNotNullRow = -1;
        while (lastNotNullRow < equationsNum - 1 && !coefficients.isRowNull(lastNotNullRow + 1)) {
            ++lastNotNullRow;
        }
        for (int j = lastNotNullRow + 1; j < equationsNum; ++j) {
            if (!freeCol.isRowNull(j)) {
                return null;
            }
        }
        if (lastNotNullRow < variablesNum - 1) {
            return new Complex[0];
        } else {
            Complex[] solution = new Complex[variablesNum];
            for (int k = 0; k < variablesNum; ++k) {
                solution[k] = coefficients.getCell(k, k).reverse().multiply(freeCol.getCell(k, 0));
            }
            return solution;
        }
    }

}
