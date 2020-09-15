import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        
        int subDim = scanner.nextInt();
        int dim = subDim * subDim;
        
        int[][] matrix = new int[dim][dim];
        int inputCounter = 0;
        while (true) {
            if (inputCounter == dim * dim) {
                if (scanner.hasNext()) {
                    System.out.println("NO");
                    return;
                } else {
                    break;
                }
            } else {
                if (!scanner.hasNext()) {
                    System.out.println("NO");
                    return;
                } else {
                    inputCounter += 1;
                    int input = scanner.nextInt();
                    if (input < 1 || input > dim) {
                        System.out.println("NO");
                        return;
                    } else {
                        matrix[(inputCounter - 1) / dim][(inputCounter - 1) % dim] = input;
                    }                 
                }
            }
        }
        
        for (int k = 0; k < dim * dim; ++k) {
            for (int j = k % dim + 1; j < dim; ++j) {
                if (matrix[k / dim][j] == matrix[k / dim][k % dim]) {
                    System.out.println("NO");
                    return;
                }
            }
            
            for (int i = k / dim + 1; i < dim; ++i) {
                if (matrix[i][k % dim] == matrix[k / dim][k % dim]) {
                    System.out.println("NO");
                    return;
                }
            }
        }
        
        for (int k = 0; k < subDim * subDim; ++k) {
            for (int i = 0; i < subDim * subDim; i++) {
                for (int j = 0; j < subDim * subDim; j++) {
                    if (i != j &&
                        matrix[k / subDim * subDim + i / subDim][k % subDim * subDim + i % subDim] == 
                        matrix[k / subDim * subDim + j / subDim][k % subDim * subDim + j % subDim]) {
                            
                        System.out.println("NO");
                        return;
                    }
                }
            }
        }
        
        System.out.println("YES");
    }
}
