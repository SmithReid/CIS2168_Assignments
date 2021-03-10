public class SudokuSolver {
    int[][] board;

    public SudokuSolver(int[][] board) throws Exception {
        if (board.length != 9 || board[0].length != 9) {
            throw new Exception("This is not sudoku!");
        }
        this.board = board;
    }

    public void solve() {
        ;
    }

    public int get3Digits() {
        String number = String.valueOf(board[0][0]);
        number += String.valueOf(board[0][1]);
        number += String.valueOf(board[0][2]);
        return Integer.parseInt(number);
    }
}