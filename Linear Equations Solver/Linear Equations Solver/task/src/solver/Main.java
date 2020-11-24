package solver;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        File inFile = null;
        File outFile = null;
        Matrix extended;

        for (int i = 0; i < args.length; ++i) {
            if (args[i] == "-in") {
                if (i + 1 < args.length) {
                    inFile = new File(args[i + 1]);
                    ++i;
                } else {
                    System.out.println("No input file specified.");
                    return;
                }
            } else if (args[i] == "-out") {
                if (i + 1 < args.length) {
                    outFile = new File(args[i + 1]);
                    ++i;
                } else {
                    System.out.println("No output file specified.");
                    return;
                }
            }
        }

        if (inFile == null) {
            return;
        }

        try (Scanner scanner = new Scanner(inFile)) {
            /*Debug*/ System.out.println("Raw data:");
            int cols = scanner.nextInt();
            int rows = scanner.nextInt();
            extended = new Matrix(rows, cols + 1);
            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols + 1; ++j) {
                    String number = scanner.next();
                    /*Debug*/ System.out.print(number + " ");
                    extended.setCell(i, j, new Complex(number));
                }
                /*Debug*/ System.out.println();
            }
        } catch (FileNotFoundException exc) {
            System.out.println("Input file do not found.");
            return;
        }

        /*debug*/ System.out.println(extended.toString());

        System.out.println("Start solving the equation.");
        LinearEquation equation = new LinearEquation(extended);
        Complex[] solution = equation.solve();

        //*debug*/ System.out.println(equation.toString());
        //*debug*/ System.out.println(solution == null ? "null" : solution.length);

        if (solution == null) {
            System.out.println("No solution exists.");
        } else if (solution.length == 0) {
            System.out.println("Infinite number of solutions");
        } else {
            System.out.print("The solution is: (");
            for (int k = 0; k < solution.length - 1; ++k) {
                System.out.print(solution[k] + ", ");
            }
            System.out.println(solution[solution.length - 1] + ")");
        }

        try (FileWriter writer = new FileWriter(outFile)) {
            if (solution == null) {
                writer.append("No solutions");
            } else if (solution.length == 0) {
                writer.append("Infinitely many solutions");
            } else {
                for (int k = 0; k < solution.length - 1; ++k) {
                    writer.append(solution[k].toString());
                    writer.append("\n");
                }
                writer.append(solution[solution.length - 1].toString());
            }

        } catch (IOException esc) {
            System.out.println("Error while writing to file.");
        }
    }
}
