package life;

import java.util.Random;

public class Game {

    // public static final char LIVE_CELL = 'O';
    // public static final char DEAD_CELL = ' ';

    boolean[][] cells;
    boolean[][] cellsBuffer;

    private final int size;

    private long seed;
    private Random random;

    private int generation = 0;


    public Game(int size, long seed) {
        this.size = size;
        cells = new boolean[size][size];
        cellsBuffer = new boolean[size][size];

        this.seed = seed;
        random = new Random(seed);
    }

    public Game(int size) {
        this.size = size;
        cells = new boolean[size][size];
        cellsBuffer = new boolean[size][size];

        random = new Random();
    }

    public void fillRandom(int liveCells) {
        while (liveCells > 0) {
            int candidate = random.nextInt(size * size);
            int x = candidate % size;
            int y = candidate / size;

            if (random.nextBoolean()) {
                --liveCells;
                setCell(x, y, true);
            } else {
                continue;
            }
        }
    }

    public void fillRandom() {
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                setCell(x, y, random.nextBoolean());
            }
        }
    }


    public int getSize() {
        return size;
    }

    public int getGeneration() {
        return generation;
    }

    public int getAliveNumber() {
        int result = 0;

        for (boolean[] row : cells) {
            for (boolean cell : row) {
                result += cell ? 1 : 0;
            }
        }

        return result;
    }


    public boolean isAliveAt(int x, int y) {
        x = Math.floorMod(x, size);
        y = Math.floorMod(y, size);

        return cells[y][x];
    }

    public void setCell(int x, int y, boolean value) {
        cells[y][x] = value;
    }

    public int countLiveCellsAround(int x, int y) {
        int result = 0;

        result += isAliveAt(x - 1, y - 1) ? 1 : 0;
        result += isAliveAt(x, y - 1) ? 1 : 0;
        result += isAliveAt(x + 1, y - 1) ? 1 : 0;

        result += isAliveAt(x - 1, y) ? 1 : 0;
        result += isAliveAt(x + 1, y) ? 1 : 0;

        result += isAliveAt(x - 1, y + 1) ? 1 : 0;
        result += isAliveAt(x, y + 1) ? 1 : 0;
        result += isAliveAt(x + 1, y + 1) ? 1 : 0;

        return result;
    }


    public void nextGeneration() {
        ++generation;

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                cellsBuffer[i][j] = false;
                int neighbours = countLiveCellsAround(j, i);
                if (neighbours < 2 || neighbours > 3) {
                    cellsBuffer[i][j] = false;
                } else if (neighbours == 3) {
                    cellsBuffer[i][j] = true;
                } else if (neighbours == 2) {
                    cellsBuffer[i][j] = isAliveAt(j, i);
                }

            }
        }

        boolean[][] temp = cells;
        cells = cellsBuffer;
        cellsBuffer = temp;

    }
}
