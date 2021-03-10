import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    // I know the "throws" statement from the next line is generally bad practice
    // I'm choosing not to write an essay justifying it here
    public static void main(String[] args) throws FileNotFoundException, Exception { 
        EightQueens eq = new EightQueens();
        System.out.println(eq);

        Scanner sc = new Scanner(new File("sudoku.txt"));

        int sum = 0;

        for (int nPuzzles = 0; nPuzzles < 50; nPuzzles++) {
            int[][] board = new int[9][9];
            String line = sc.nextLine(); // This is a header line for one puzzle
            for (int i = 0; i < 9; i++) {
                line = sc.nextLine(); // This is a puzzle line
                for (int j = 0; j < 9; j++) {
                    board[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
                }
            }

            SudokuSolver ss = new SudokuSolver(board);
            ss.solve();
            sum += ss.get3Digits();
        }
        System.out.println(sum);
    }
}