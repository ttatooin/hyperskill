package battleship;

import java.util.Scanner;

public class BattleshipGame {

    private Scanner scanner;
    private int currentPlayer;
    private Field[] fields;

    public BattleshipGame(Scanner scanner) {
        this.scanner = scanner;
        currentPlayer = 0;
        fields = new Field[] {new Field(10, 10), new Field(10, 10)};
    }

    private void swapPlayers() {
        currentPlayer = (currentPlayer + 1) % 2;
    }

    public int getCurrentPlayer() {
        return currentPlayer + 1;
    }

    public Field getCurrentField() {
        return fields[currentPlayer];
    }

    public Field getOpponentField() {
        return fields[(currentPlayer + 1) % 2];
    }

    private boolean isCorrectInputPosition(String input) {
        return input.matches("[A-J]([1-9]|10)");
    }

    private Position parsePosition(String input) {
        return new Position(1 + input.charAt(0) - 'A', Integer.parseInt(input.substring(1)));
    }

    public void initialize() {
        System.out.println("Player " + getCurrentPlayer() + ", place your ships on the game field.\n");
        System.out.println(getCurrentField().toStringRevealed());
        initializeShip("Aircraft Carrier", 5);
        initializeShip("Battleship", 4);
        initializeShip("Submarine", 3);
        initializeShip("Cruiser", 3);
        initializeShip("Destroyer", 2);
    }

    private void initializeShip(String name, int length) {
        System.out.println("Enter the coordinates of the " + name + " (" + length + " cells):\n");
        while (true) {
            String[] input = scanner.nextLine().trim().split("\\s+");
            System.out.println();
            if (input.length != 2 || !isCorrectInputPosition(input[0]) || !isCorrectInputPosition(input[1])) {
                System.out.println("Error! Incorrect input. Try again:\n");
                continue;
            } else {
                Position head = parsePosition(input[0]);
                Position tail = parsePosition(input[1]);
                if (head.row - tail.row != 0 && head.col - tail.col != 0) {
                    System.out.println("Error! Wrong ship location. Try again:\n");
                    continue;
                } else if (head.getLInfDistTo(tail) + 1 != length) {
                    System.out.println("Error! Wrong length of the " + name + "! Try again:\n");
                    continue;
                } else {
                    Field.ErrorCode error = getCurrentField().addShip(head, tail);
                    if (error != null) {
                        System.out.println("Error! " + error + " Try again:\n");
                        continue;
                    } else {
                        System.out.println(getCurrentField().toStringRevealed());
                        break;
                    }
                }
            }
        }
    }

    public void attack() {
        System.out.print(getOpponentField().toStringFogged());
        System.out.println("---------------------");
        System.out.println(getCurrentField().toStringRevealed());
        System.out.println("Player " + getCurrentPlayer() + ", it's your turn:\n");
        while (true) {
            String input = scanner.nextLine();
            System.out.println();
            if (!isCorrectInputPosition(input)) {
                System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                continue;
            } else {
                Position target = parsePosition(input);
                if (!getOpponentField().isPositionCorrect(target)) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                    continue;
                } else {
                    if (getOpponentField().attackCell(target)) {
                        if (!getOpponentField().existsAliveCell()) {
                            System.out.println("You sank the last ship. You won. Congratulations!\n");
                        } else if (!getOpponentField().isShipAlive(target)) {
                            System.out.println("You sank a ship! Specify a new target:\n");
                        } else {
                            System.out.println("You hit a ship!\n");
                        }
                    } else {
                        System.out.println("You missed.\n");
                    }
                    break;
                }

            }
        }
    }

    public void start() {
        initialize();
        System.out.println("Press Enter and pass the move to another player\n");
        scanner.nextLine();
        swapPlayers();
        initialize();
        System.out.println("Press Enter and pass the move to another player\n");
        scanner.nextLine();
        swapPlayers();
        while (getOpponentField().existsAliveCell()) {
            attack();
            System.out.println("Press Enter and pass the move to another player\n");
            scanner.nextLine();
            swapPlayers();
        }
    }

}





