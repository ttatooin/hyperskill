import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        
        int dim1 = 0;
        int dim2 = 0;
             
        StringBuilder rawData = new StringBuilder();
        while (scanner.hasNextInt()) {
            rawData.append(scanner.nextLine());
            rawData.append(" ");
            dim1 += 1;
        }
        
        String[] rawArray = rawData.toString().split(" ");
        dim2 = rawArray.length / dim1;
        
        int[][] matrix = new int[dim1][dim2];
        
        for (int i = 0; i < rawArray.length; ++i) {
            matrix[i / dim2][i % dim2] = Integer.parseInt(rawArray[i]);
        }
        
        int[][] result = new int[dim1][dim2];
        
        for (int i = 0; i < dim1; ++i) {
            for (int j = 0; j < dim2; ++j) {
                result[i][j] = matrix[Math.floorMod(i + 1, dim1)][j];
                result[i][j] += matrix[Math.floorMod(i - 1, dim1)][j];
                result[i][j] += matrix[i][Math.floorMod(j + 1, dim2)];
                result[i][j] += matrix[i][Math.floorMod(j - 1, dim2)];
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }
}
