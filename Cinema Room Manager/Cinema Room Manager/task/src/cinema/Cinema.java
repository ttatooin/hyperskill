package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int cols = scanner.nextInt();
        System.out.println();

        Theater theater = new Theater(rows, cols);

        mainCycle:
        while (true) {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println(theater.getSeatsScheme());
                    System.out.println();
                    break;
                case 2:
                    int seatRow;
                    int seatCol;
                    while (true) {
                        System.out.println("Enter a row number:");
                        seatRow = scanner.nextInt();
                        System.out.println("Enter a seat number in that row:");
                        seatCol = scanner.nextInt();
                        if (seatRow < 1 || seatRow > theater.rows || seatCol < 1 || seatCol > theater.rows) {
                            System.out.println("Wrong input!\n");
                            continue;
                        } else if (theater.buyTicket(seatRow, seatCol) == false) {
                            System.out.println("That ticket has already been purchased!\n");
                            continue;
                        } else {
                            break;
                        }
                    }
                    System.out.println("Ticket price: $" + theater.getTicketPrice(seatRow, seatCol));
                    System.out.println();
                    continue mainCycle;
                case 3:
                    System.out.println(theater.getStatistics());
                    System.out.println();
                    continue mainCycle;
                case 0:
                    break mainCycle;
                default:
                    System.out.println("Incorrect input");
                    continue mainCycle;
            }
        }
    }
}