//Code Written by Reid Smith
// Version began 1/22/2021
// Version 1.0

import java.util.HashMap;
import java.lang.Integer;

import java.util.Arrays; // debugging only likely

public class Rabbit extends Animal {
    private int turnNumber = -1;
    private boolean locationKnown = false;
    private int rabbitRow = Integer.MAX_VALUE;
    private int rabbitColumn = Integer.MAX_VALUE;

    /* When we collectData each step, 

    5s will denote unknown
    4s will denote empty

    0s will denote EDGEs
    1s will denote FOX
    2s will denote RABBIT
    3s will denote BUSHes
    */
    private int[][] board = new int[20][20];

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (i == 0 || j == 0 || i == 19 || j ==19) {
                    board[i][j] = 0;
                } else {
                    board[i][j] = 5;
                }
            }
        }
    }

    String boardToString() {
        String output = "";
        for (int i = 0; i < board.length; i++) {
            output += Arrays.toString(board[i]) + "\n";
        }
        return output;
    }

    int[] getDirections(int i) {
        int yStep = 0;
        int xStep = 0;
        if (i == 0 || i == 7 || i == 1) {
            yStep = -1;
        } else if (i == 2 || i == 6) {
            ; // do nothing with yStep
        } else {
            yStep = 1;
        }

        if (i == 1 || i == 2 || i == 3) {
            xStep = 1;
        } else if (i == 0 || i == 4) {
            ; // do nothing with xStep
        } else {
            xStep = -1;
        }
        return new int[] {yStep, xStep};
    }

    void locateRabbit() {
        HashMap<Integer, Integer> edges = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            if (look(i) == 0) {
                edges.put(i, distance(i) - 1);
            }
        }

        if (edges.size() > 1) {
            if (edges.keySet().contains(0)) {
                rabbitRow = edges.get(0);
            }
            if (edges.keySet().contains(4)) {
                rabbitRow = 19 - edges.get(4);
            }

            if (edges.keySet().contains(2)) {
                rabbitColumn = 19 - edges.get(2);
            }
            if (edges.keySet().contains(6)) {
                rabbitColumn = edges.get(6);
            }
        }

        if (rabbitRow < Integer.MAX_VALUE && rabbitColumn < Integer.MAX_VALUE) {
            locationKnown = true;
            board[rabbitRow][rabbitColumn] = 2;
        }
    }

    void collectData(int direction) {
        // TODO: implement this properly. 

        int visible = look(direction);
        int distance = distance(direction);

        int[] steps = getDirections(direction);
        int yStep = steps[0];
        int xStep = steps[1];

        int yToSet = rabbitRow;
        int xToSet = rabbitColumn;

        for (int i = 0; i < distance - 2; i++) {
            yToSet += yStep;
            xToSet += xStep;
            board[yToSet][xToSet] = 4;
        }
        yToSet += yStep;
        xToSet += xStep;
        board[yToSet][xToSet] = visible;

        System.out.println(boardToString());

    }

    int decideMove() {
        turnNumber++;
        for (int i = 0; i < 8; i++) {
            collectData(i);
        }
        if (!locationKnown) {
            locateRabbit();
        }
        if (!locationKnown) {
            return random(0, 8);
        }
        return Model.STAY;

    }
}