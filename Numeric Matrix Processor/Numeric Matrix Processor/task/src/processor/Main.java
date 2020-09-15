package processor;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Matrix matrix1 = null;
        int matrix1rowNumber = 0;
        int matrix1colNumber = 0;
        Matrix matrix2 = null;
        int matrix2rowNumber = 0;
        int matrix2colNumber = 0;
        Matrix result = null;
        double scalar = 0;

        while (true) {
            System.out.println("1. Add matrices");
            System.out.println("2. Multiply matrix to a constant");
            System.out.println("3. Multiply matrices");
            System.out.println("4. Transpose matrix");
            System.out.println("5. Calculate a determinant");
            System.out.println("6. Inverse matrix");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            int decision = scanner.nextInt();
            int transposeType = 0;

            switch (decision) {
                case 0:
                    return;
                case 1:
                case 3:
                    System.out.print("Enter size of first matrix: ");
                    matrix1rowNumber = scanner.nextInt();
                    matrix1colNumber = scanner.nextInt();
                    System.out.println("Enter first matrix: ");
                    matrix1 = readMatrix(scanner, matrix1rowNumber, matrix1colNumber);

                    System.out.print("Enter size of second matrix: ");
                    matrix2rowNumber = scanner.nextInt();
                    matrix2colNumber = scanner.nextInt();
                    System.out.println("Enter first second: ");
                    matrix2 = readMatrix(scanner, matrix2rowNumber, matrix2colNumber);
                    break;
                case 2:
                    System.out.print("Enter size of matrix: ");
                    matrix1rowNumber = scanner.nextInt();
                    matrix1colNumber = scanner.nextInt();
                    System.out.println("Enter matrix: ");
                    matrix1 = readMatrix(scanner, matrix1rowNumber, matrix1colNumber);

                    System.out.print("Enter constant: ");
                    scalar = scanner.nextDouble();
                    break;
                case 4:
                    System.out.println("1. Main diagonal");
                    System.out.println("2. Side diagonal");
                    System.out.println("3. Vertical line");
                    System.out.println("4. Horizontal line");
                    System.out.print("Your choice: ");
                    transposeType = scanner.nextInt();

                    System.out.print("Enter size of matrix: ");
                    matrix1rowNumber = scanner.nextInt();
                    matrix1colNumber = scanner.nextInt();
                    System.out.println("Enter matrix: ");
                    matrix1 = readMatrix(scanner, matrix1rowNumber, matrix1colNumber);
                    break;
                case 5:
                case 6:
                    System.out.print("Enter size of matrix: ");
                    matrix1rowNumber = scanner.nextInt();
                    matrix1colNumber = scanner.nextInt();
                    System.out.println("Enter matrix: ");
                    matrix1 = readMatrix(scanner, matrix1rowNumber, matrix1colNumber);
                    break;
                default:
                    continue;
            }

            try {
                switch (decision) {
                    case 1:
                        result = matrix1.add(matrix2);
                        System.out.println("The addition result is:");
                        System.out.println(result.toString());
                        break;
                    case 3:
                        result = matrix1.multiply(matrix2);
                        System.out.println("The multiplication result is:");
                        System.out.println(result.toString());
                        break;
                    case 2:
                        result = matrix1.multiply(scalar);
                        System.out.println("The multiplication result is:");
                        System.out.println(result.toString());
                        break;
                    case 4:
                        switch (transposeType) {
                            case 1:
                                result = matrix1.transposeMain();
                                break;
                            case 2:
                                result = matrix1.transposeSide();
                                break;
                            case 3:
                                result = matrix1.transposeVertical();
                                break;
                            case 4:
                                result = matrix1.transposeHorizontal();
                                break;
                        }
                        System.out.println("The result is: ");
                        System.out.println(result.toString());
                        break;
                    case 5:
                        System.out.println("The result is: ");
                        System.out.println(matrix1.getDeterminant());
                        break;
                    case 6:
                        System.out.println("The result is: ");
                        System.out.println(matrix1.inverse());
                        break;
                }

            } catch (ArithmeticException e) {
                System.out.println("ERROR");
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }

            System.out.println();
        }

    }

    private static Matrix readMatrix(Scanner scanner, int rowNumber, int colNumber) {
        Matrix result = new Matrix(rowNumber, colNumber);
        for (int i = 0; i < rowNumber; ++i) {
            for (int j = 0; j < colNumber; ++j) {
                result.setElement(i, j, scanner.nextDouble());
            }
        }
        return result;
    }
}
