package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        boolean stillRunning = true;

        Scanner scanner = new Scanner(System.in);
        Maze maze = null;

        mainCycle:
        while (stillRunning) {
            System.out.println("=== Menu ===");
            System.out.println("1. Generate a new maze");
            System.out.println("2. Load a maze");
            if (maze != null) {
                System.out.println("3. Save the maze");
                System.out.println("4. Display the maze");
                System.out.println("5. Find the escape");
        }
            System.out.println("0. Exit");

            try {
                int choice = Integer.parseInt(scanner.next());
                switch (choice) {
                    case 0:
                        stillRunning = false;
                        System.out.println("Bye!");
                        break;

                    case 1:
                        System.out.println("Enter the size of a new maze");
                        int size = scanner.nextInt();
                        maze = new Maze(size, size);
                        System.out.println(maze.toString());
                        break;

                    case 2:
                        String fileName = scanner.next();
                        try (Scanner fscanner = new Scanner(new File(fileName))) {
                            StringBuilder result = new StringBuilder();
                            while (fscanner.hasNextLine()) {
                                result.append(fscanner.nextLine());
                                result.append("\n");
                            }
                            result.deleteCharAt(result.length() - 1);
                            // Checking for format correctness.
                            for (int i = 0; i < result.length(); ++i) {
                                if (result.charAt(i) != Maze.SPACE_SYMBOL &&
                                        result.charAt(i) != Maze.WALL_SYMBOL &&
                                        result.charAt(i) != '\n') {
                                    System.out.println("Cannot load the maze. It has an invalid format: error 1");
                                    continue mainCycle;
                                }
                            }

                            String[] lines = result.toString().split("\n");
                            for (int i = 0; i < lines.length; ++i) {
                                if (lines[i].length() != 2 * lines.length) {
                                    System.out.println("Cannot load the maze. It has an invalid format: error 2");
                                    continue mainCycle;
                                }
                            }
                            // Loading
                            maze = new Maze(result.toString());
                        } catch (FileNotFoundException exc) {
                            System.out.println("The file " + fileName + " does not exist");
                            continue mainCycle;
                        }
                        break;

                    case 3:
                        if (maze == null) {
                            System.out.println("Incorrect option. Please try again");
                            continue mainCycle;
                        }
                        File file = new File(scanner.next());
                        try (FileWriter writer = new FileWriter(file, false);) {
                            writer.write(maze.toString());
                        } catch (IOException exc) {
                            System.out.println("Cannot save maze to file");
                            continue mainCycle;
                        }
                        break;

                    case 4:
                        if (maze == null) {
                            System.out.println("Incorrect option. Please try again");
                            continue mainCycle;
                        }
                        System.out.println(maze.toString());
                        break;

                    case 5:
                        if (maze == null) {
                            System.out.println("Incorrect option. Please try again");
                            continue mainCycle;
                        }
                        System.out.println(maze.toStringWithSolution());
                        break;

                    default:
                        System.out.println("Incorrect option. Please try again");
                        continue mainCycle;

                }
            } catch (NumberFormatException exp) {
                System.out.println("Incorrect option. Please try again");
                continue mainCycle;
            }
        }
    }
}
