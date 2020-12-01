package cinema;

public class Theater {

    private final boolean[][] seats;
    public final int rows;
    public final int cols;

    public Theater(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        seats = new boolean[rows][cols];
    }

    public String getSeatsScheme() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cinema:\n");
        builder.append(" ");
        for (int k = 0; k < cols; ++k) {
            builder.append(" ");
            builder.append(k + 1);
        }
        builder.append("\n");
        for (int n = 0; n < rows;  ++n) {
            builder.append(n + 1);
            for (int k = 0; k < cols; ++k) {
                builder.append(" ");
                builder.append(seats[n][k] ? "B" : "S");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String printHighlightSeat(int row, int col) {
        StringBuilder builder = new StringBuilder();
        builder.append("Cinema:\n");
        builder.append(" ");
        for (int k = 0; k < cols; ++k) {
            builder.append(" ");
            builder.append(k + 1);
        }
        builder.append("\n");
        for (int n = 0; n < rows;  ++n) {
            builder.append(n + 1);
            for (int k = 0; k < cols; ++k) {
                builder.append(" ");
                if (n + 1 == row && k + 1 == col) {
                    builder.append("B");
                } else {
                    builder.append("S");
                }

            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public boolean buyTicket(int row, int col) {
        if (seats[row - 1][col - 1]) {
            return false;
        } else {
            seats[row - 1][col - 1] = true;
            return true;
        }
    }

    public int getMaxProfit() {
        int result = 0;
        for (int n = 0; n < rows; ++n) {
            for (int k = 0; k < cols; ++k) {
                result += getTicketPrice(n + 1, k + 1);
            }
        }
        return result;
    }

    public int getCurrentProfit() {
        int result = 0;
        for (int n = 0; n < rows; ++n) {
            for (int k = 0; k < cols; ++k) {
                if (seats[n][k]) {
                    result += getTicketPrice(n + 1, k + 1);
                }
            }
        }
        return result;
    }

    public int getTicketPrice(int row, int col) {
        if (rows * cols <= 60) {
            return 10;
        } else {
            if (row <= rows / 2) {
                return 10;
            } else {
                return 8;
            }
        }
    }

    public int getSoldTicketsNumber() {
        int result = 0;
        for (boolean[] rowSeats : seats) {
            for (boolean seat : rowSeats) {
                result += seat ? 1 : 0;
            }
        }
        return result;
    }

    public String getStatistics() {
        StringBuilder builder = new StringBuilder();

        builder.append("Number of purchased tickets: ");
        builder.append(getSoldTicketsNumber());
        builder.append("\n");

        builder.append("Percentage: ");
        builder.append(String.format("%.2f", getSoldTicketsNumber() * 100.0 / (rows * cols)));
        builder.append("%\n");

        builder.append("Current income: $");
        builder.append(getCurrentProfit());
        builder.append("\n");

        builder.append("Total income: $");
        builder.append(getMaxProfit());
        builder.append("\n");

        return builder.toString();
    }

}
