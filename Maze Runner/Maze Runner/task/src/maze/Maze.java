package maze;

import java.util.Random;

public class Maze {

    public static final char SPACE_SYMBOL = ' ';
    public static final char WALL_SYMBOL = '\u2588';
    public static final char PATH_SYMBOL = '/';

    private int[][] walls;

    private final int height;
    private final int length;

    public Maze() {
        height = 10;
        length = 10;
        walls = new int[height][length];
    }

    public Maze(int height, int length) {

        this.height = height;
        this.length = length;
        walls = new int[height][length];

        generatePrimMaze();
    }

    public Maze(String str) {

        String[] lines = str.split("\n");
        height = lines.length;
        length = lines[0].length() / 2;

        walls = new int[height][length];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < length; ++j) {
                walls[i][j] = lines[i].charAt(2 * j) == WALL_SYMBOL ? 1 : 0;
            }
        }
    }


    public void generateTestMaze() {
        walls = new int[][] {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                             {0, 0, 1, 0, 1, 0 ,1, 0, 0, 1},
                             {1, 0, 1, 0, 0, 0, 1, 0 ,1, 1},
                             {1, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                             {1, 0, 1, 0, 0, 0, 0, 0, 1, 1},
                             {1, 0, 1, 0, 1, 1, 1, 0 ,1, 1},
                             {1, 0, 1, 0, 1, 0, 0, 0, 1, 1},
                             {1, 0, 1, 0, 1, 1, 1, 0, 1, 1},
                             {1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
                             {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
    }

    public void generatePrimMaze() {

        // Fill whole maze with walls.
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < length; ++j) {
                walls[i][j] = 1;
            }
        }

        // Generate internal maze.
        WeightedGraph graph = new WeightedGraph();
        int rows = (height - 1) / 2;
        int cols = (length - 1) / 2;

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                graph.addVertex(i * cols + j);
            }
        }

        Random random = new Random();
        final int maxWeight = 20;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j)
            {
                if (j < cols - 1) {
                    graph.addEdge(i * cols + j, i * cols + (j + 1), random.nextInt(maxWeight) + 1);
                }

                if (i < rows - 1) {
                    graph.addEdge(i * cols + j, (i + 1) * cols + j, random.nextInt(maxWeight) + 1);
                }
            }
        }

        WeightedGraph mst = graph.getMinimalSpanningTree();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (mst.ifContainsEdge(i * cols + j, i* cols + (j + 1))) {
                    walls[1 + i * 2][1 + j * 2] = 0;
                    walls[1 + i * 2][1 + j * 2 + 1] = 0;
                    walls[1 + i * 2][1 + j * 2 + 2] = 0;
                }

                if (mst.ifContainsEdge(i * cols + j, (i + 1) * cols + j)) {
                    walls[1 + i * 2][1 + j * 2] = 0;
                    walls[1 + i * 2 + 1][1 + j * 2] = 0;
                    walls[1 + i * 2 + 2][1 + j * 2] = 0;
                }
            }
        }

        // Generate entrance and exit.
        int possibleEntrancesNumber = 0;
        for (int i = 1; i < height - 1; ++i) {
            possibleEntrancesNumber += walls[i][1] == 0 ? 1 : 0;
        }

        int possibleExitsNumber = 0;
        for (int i = 1; i < height - 1; ++i) {
            possibleExitsNumber += walls[i][length - 2] == 0 ? 1 : 0;
        }

        int entrance = random.nextInt(possibleEntrancesNumber);
        int exit = random.nextInt(possibleExitsNumber);

        for (int i = 1, counter = -1; i < height; ++i) {
            if (walls[i][1] == 0) {
                ++counter;
            }
            if (counter == entrance) {
                walls[i][0] = 0;
                break;
            }
        }

        for (int i = 1, counter = -1; i < height; ++i) {
            if (walls[i][length - 2 - (length + 1) % 2] == 0) {
                ++counter;
            }
            if (counter == entrance) {
                walls[i][length - 1] = 0;
                walls[i][length - 2] = 0;
                break;
            }
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int[] row : walls) {
            for (int cell : row) {
                result.append(cell == 1 ? WALL_SYMBOL : SPACE_SYMBOL);
                result.append(cell == 1 ? WALL_SYMBOL : SPACE_SYMBOL);
            }
            result.append("\n");
        }
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    public String toStringVerbatim() {
        StringBuilder result = new StringBuilder();

        for (int[] row : walls) {
            for (int cell : row) {
                result.append(cell);
            }
            result.append("\n");
        }
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    public String toStringWithSolution() {
        int[][] extended = new int[length][height];
        int[][] result = new int[length][height];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < length; ++j) {
                extended[i][j] = -walls[i][j];
                result[i][j] = walls[i][j];
            }
        }

        // Вход помечаем через 1, выход через -2
        int[] marks = {1, -2};
        int mark = 0;
        int x = 0;
        int y = 0;
        int dx = 1;
        int dy = 0;
        for (int i = 0; i < 2 * (length + height - 1); ++i) {
            if (extended[y][x] == 0) {
                extended[y][x] = marks[mark];
                ++mark;
            }

            if (x + dx < 0 || x + dx >= length || y + dy < 0 || y + dy >= height) {
                int temp = dy;
                dy = dx;
                dx = -temp;
            }

            x += dx;
            y += dy;
        }

        // Волновой алгоритм поиска.
        // Сложность n^2, n-число ячеек лабиринта.
        int wave = 1;
        int exitX;
        int exitY;

        forward:
        while (true) {
            for (int i = 0; i < height; ++i) {
                for (int j = 0; j < length; ++j) {
                    if (extended[i][j] == wave) {
                        if (i - 1 >= 0) {
                            if (extended[i - 1][j] == -2) {
                                exitY = i - 1;
                                exitX = j;
                                break forward;
                            } else if (extended[i - 1][j] == 0) {
                                extended[i - 1][j] = wave + 1;
                            }
                        }
                        if (i + 1 < height) {
                            if (extended[i + 1][j] == -2) {
                                exitY = i + 1;
                                exitX = j;
                                break forward;
                            } else if (extended[i + 1][j] == 0) {
                                extended[i + 1][j] = wave + 1;
                            }
                        }
                        if (j - 1 >= 0) {
                            if (extended[i][j - 1] == -2) {
                                exitY = i;
                                exitX = j - 1;
                                break forward;
                            } else if (extended[i][j - 1] == 0) {
                                extended[i][j - 1] = wave + 1;
                            }
                        }
                        if (j + 1 < length) {
                            if (extended[i][j + 1] == -2) {
                                exitY = i;
                                exitX = j + 1;
                                break forward;
                            } else if (extended[i][j + 1] == 0) {
                                extended[i][j + 1] = wave + 1;
                            }
                        }
                    }
                }
            }

            ++wave;
        }

        result[exitY][exitX] = 2;

        while (wave > 0) {
            if (exitY - 1 >= 0 && extended[exitY - 1][exitX] == wave) {
                exitY = exitY - 1;
            } else if (exitY + 1 < height && extended[exitY + 1][exitX] == wave) {
                exitY = exitY + 1;
            } else if (exitX - 1 >= 0 && extended[exitY][exitX - 1] == wave) {
                exitX = exitX - 1;
            } else if (exitX + 1 < length && extended[exitY][exitX + 1] == wave) {
                exitX = exitX + 1;
            }
            result[exitY][exitX] = 2;
            --wave;
        }

        // Печатаем результат
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j <length; ++j) {
                switch (result[i][j]) {
                    case 0:
                        builder.append(Maze.SPACE_SYMBOL);
                        builder.append(Maze.SPACE_SYMBOL);
                        break;
                    case 1:
                        builder.append(Maze.WALL_SYMBOL);
                        builder.append(Maze.WALL_SYMBOL);
                        break;
                    case 2:
                        builder.append(Maze.PATH_SYMBOL);
                        builder.append(Maze.PATH_SYMBOL);
                        break;
                }
            }
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private static String toStringVerbatim(int[][] array) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                builder.append(Integer.toString(array[i][j]));
            }
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
