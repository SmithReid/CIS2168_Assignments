public class EightQueens {
    int[][] board;

    public EightQueens() {
        board = new int[8][8];
        solve(0);
    }

    private boolean moveIsValid(int move, int xPos, int yPos) {
        int sum = 0;
        // row
        for (int i = 0; i < 8; i++) 
            sum += board[yPos][i];
        // col
        for (int i = 0; i < 8; i++) 
            sum += board[i][xPos];
        // negative slope diagonal
        /*
        if (xPos > yPos) {
            for (int i = xPos + yPos, j = 0; i < 8 && j < 8; i++, j++)
                sum += board[i][j];
        } else {
            for (int i = 0, j = yPos - xPos; i < 8 && j < 8; i++, j++)
                sum += board[i][j];
        }
        // positive slope diagonal
        if (xPos + yPos < 7) {
            for (int i = 0, j = xPos + yPos; i > 0 && j < 8; i--, j++)
                sum += board[i][j];
        } else { // we are in the lower half
            for (int i = xPos - yPos, j = 0; i > 0 && j < 8; i--, j++)
                sum += board[i][j];
        }
        */
        return (sum == 0);
    }

    private int countQueens() {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sum += board[i][j];
            }
        }
        return sum;
    }


    private boolean mySolve(int col){
        if(col == 8 ){return true;}

        for(int row =0 ; row < 8 ; row++){

        }




    }

    private boolean solve(int pos) {
        System.out.println(this);
        int queenCount = countQueens();
        if (queenCount == 8) 
            return true;
        if (pos > 63) 
            return false;

        int xPos = pos % 8;
        int yPos = pos / 8;

        for (int i = 0; i <= 1; i++) {
            if (moveIsValid(i, xPos, yPos)) {
                board[yPos][xPos] = 1;
                if (solve(pos + 1)) {
                    return true;
                }
            }
            board[xPos][yPos] = 0;
        }
        return false;
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                output += board[i][j] + " ";
            }
            output += "\n";
        }
        return output; 
    }
}






