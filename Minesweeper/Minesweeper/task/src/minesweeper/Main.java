package minesweeper;

import java.util.Random;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many time do you want on the field? ");
        int minesNumber = scanner.nextInt();
        System.out.println();

        char[][] field = generateRandomField(9, 9, minesNumber);
        revealAllNeighbors(field);

        char[][] userField = new char[field.length][field[0].length];
        clearUserField(userField);

        int markersOverall = 0;
        int markersCorrect = 0;
        int unexploredCells = field.length * field[0].length;

        boolean isFirstMove = true;

        while (markersCorrect != markersOverall && markersCorrect != minesNumber && unexploredCells > minesNumber) {
            printGameField9x9(userField);
            int xCoord;
            int yCoord;
            String command;

            while (true) {
                System.out.print("Set/unset mines marks or claim a cell as free: ");
                xCoord = scanner.nextInt();
                yCoord = scanner.nextInt();
                command = scanner.next();
                if (userField[yCoord][xCoord] >= '0' && userField[yCoord][xCoord] <= '9') {
                    System.out.println("There is a number here!");
                    continue;
                }
                break;
            }

            switch (command) {
                case "free":
                    if (isFirstMove) {
                        while (field[yCoord][xCoord] == 'X') {
                            field = generateRandomField(9, 9, minesNumber);
                        }
                    }
                    if (field[yCoord][xCoord] == 'X'){
                        System.out.println("You stepped on a mine and failed!");
                        return;
                    } else {
                        unexploredCells = openCell(userField, field, xCoord, yCoord);
                    }
                    break;

                case "mine":
                    if (userField[yCoord][xCoord] == '.') {
                        userField[yCoord][xCoord] = '*';
                        markersOverall += 1;
                        markersCorrect += (field[yCoord][xCoord] == 'X') ? 1 : 0;
                    } else if (userField[yCoord][xCoord] == '*') {
                        userField[yCoord][xCoord] = '.';
                        markersOverall -= 0;
                        markersCorrect -= (field[yCoord][xCoord] == 'X') ? 1 : 0;
                    }
                    break;
                default:
                    continue;
            }

            System.out.println();
        }

        printGameField9x9(userField);
        System.out.println("Congratulations! You found all mines!");
    }

    public static void printField(char[][] field) {
        for (char[] row : field) {
            for (char element : row) {
                System.out.print(element);
            }
            System.out.println();
        }
    }

    public static char[][] generateRandomField(int sizeX, int sizeY, int minesNumber) {
        char[][] result = new char[sizeY][sizeX];

        Random random = new Random();

        for (int i = 0; i < sizeY; ++i) {
            for (int j = 0; j < sizeX; ++j) {
                result[i][j] = '.';
            }
        }

        while (minesNumber > 0) {
            int randomX = random.nextInt(sizeX);
            int randomY = random.nextInt(sizeY);
            if (result[randomY][randomX] == '.') {
                result[randomY][randomX] = 'X';
                --minesNumber;
            } else {
                continue;
            }
        }

        return result;
    }

    public static int countSurroundingMines(int coordX, int coordY, char[][] field) {
        int result = 0;

        int dimY = field.length;
        int dimX = field[0].length;

        result += (coordX - 1 >= 0 && coordY - 1 >= 0 && field[coordY - 1][coordX - 1] == 'X') ? 1 : 0;
        result += (coordY - 1 >= 0 && field[coordY - 1][coordX] == 'X') ? 1 : 0;
        result += (coordX + 1 < dimX  && coordY - 1 >= 0 && field[coordY - 1][coordX + 1] == 'X') ? 1 : 0;

        result += (coordX - 1 >= 0 && field[coordY][coordX - 1] == 'X') ? 1 : 0;
        result += (coordX + 1 < dimX && field[coordY][coordX + 1] == 'X') ? 1 : 0;

        result += (coordX - 1 >= 0 && coordY + 1 < dimY && field[coordY + 1][coordX - 1] == 'X') ? 1 : 0;
        result += (coordY + 1 < dimY && field[coordY + 1][coordX] == 'X') ? 1 : 0;
        result += (coordX + 1 < dimX && coordY + 1 < dimY && field[coordY + 1][coordX + 1] == 'X') ? 1 : 0;

        return result;
    }

    public static void revealAllNeighbors(char[][] field) {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                if (field[i][j] != 'X') {
                    int minesQuantity = countSurroundingMines(j, i, field);
                    if (minesQuantity > 0) {
                        field[i][j] = (char) ('0' + minesQuantity);
                    }
                }
            }
        }
    }

    public static void printGameField9x9(char[][] field) {
        System.out.println(" |123456789|");
        System.out.println("-|_________|");
        for (int i = 0; i < 9; ++i) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < 9; ++j) {
                System.out.print(field[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|_________|");
    }

    public static char[][] setNewUserField(char[][] field) {
        char[][] result = new char[field.length][field[0].length];
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                result[i][j] = (field[i][j] != 'X') ? field[i][j] : '.';
            }
        }
        return result;
    }

    public static void clearUserField(char[][] field) {
        for (int j = 0; j < field.length; ++j) {
            for (int i = 0; i < field[0].length; ++i) {
                field[j][i] = '.';
            }
        }
    }

    public static int openCell(char[][] field, char[][] userField, int xCoord, int yCoord) {
        if (field[yCoord][xCoord] >= '0' && field[yCoord][xCoord] <= '9') {
            userField[yCoord][xCoord] = field[yCoord][xCoord];
        } else {
            boolean isUpdateRequired = false;
            do {
                for (int j = 0; j < userField.length; ++j) {
                    for (int i = 0; i < userField[0].length; ++i) {
                        if (hasZeroCell(i, j, field)) {
                            userField[j][i] = (field[j][i] == '.') ? '/' : field[j][i];
                            if (userField[j][i] == '/') {
                                isUpdateRequired = true;
                            }
                        }
                    }
                }
            } while (isUpdateRequired);
        }


        int unexploredCell = 0;
        for (char[] row : userField) {
            for (char cell : row) {
                if (cell == '.') {
                    ++unexploredCell;
                }
            }
        }
        return unexploredCell;
    }

    private static boolean hasZeroCell(int xCoord, int yCoord, char[][] field) {
        int dimY = field.length;
        int dimX = field[0].length;

        return xCoord - 1 >= 0 && yCoord - 1 >= 0 && field[yCoord - 1][xCoord - 1] == '.' ||
                yCoord - 1 >= 0 && field[yCoord - 1][xCoord] == '.' ||
                xCoord + 1 < dimX && yCoord - 1 >= 0 && field[yCoord - 1][xCoord + 1] == '.' ||
                xCoord - 1 >= 0 && field[yCoord][xCoord - 1] == '.' ||
                xCoord + 1 >= 0 && field[yCoord][xCoord + 1] == '.' ||
                xCoord - 1 >= 0 && yCoord + 1 <= dimY && field[yCoord + 1][xCoord - 1] == '.' ||
                yCoord + 1 <= dimY && field[yCoord + 1][xCoord] == '.'||
                xCoord + 1 >= 0 && yCoord + 1 <= dimY && field[yCoord + 1][xCoord + 1] == '.';
    }
}
