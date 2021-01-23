// @author Reid Smith
// Version began 1/23/2020
// Version 1.1 (Significant restart using much of the structure of 1.0)

import java.util.HashMap;
import java.lang.Integer;

import java.util.Arrays; // debugging only likely

public class Rabbit extends Animal {
    private int turnNumber = -1;

    private int[][] board = new int[20][20];

    private HashMap<Integer, Integer> visibleMap = new HashMap<>();
    private HashMap<Integer, Integer> distanceMap = new HashMap<>();

    private int rabbitRow = Integer.MAX_VALUE;
    private int rabbitColumn = Integer.MAX_VALUE;

    private boolean locationKnown = false;
    private boolean rabbitEdge = false;
    private boolean rabbitCorner = false;

    private boolean haveSeenFox = false;
    private int lastDirectionToFox;

    private int lastMove;

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }

    private String boardToString() {
        String output = "";
        for (int i = 0; i < board.length; i++) {
            output += Arrays.toString(board[i]) + "\n";
        }
        return output;
    }

    private int[] getDirections(int i) {
        while (i >= 8) {
            i -= 8;
        }

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

    private void locateRabbit() {
        HashMap<Integer, Integer> edges = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            if (look(i) == 0) {
                edges.put(i, distance(i));
            }
        }

        if (edges.size() > 1) {
            if (edges.keySet().contains(0)) {
                rabbitRow = edges.get(0) - 1;
            }
            if (edges.keySet().contains(4)) {
                rabbitRow = 20 - edges.get(4);
            }

            if (edges.keySet().contains(2)) {
                rabbitColumn = 20 - edges.get(2);
            }
            if (edges.keySet().contains(6)) {
                rabbitColumn = edges.get(6) - 1;
            }
        }

        if (rabbitRow < Integer.MAX_VALUE && rabbitColumn < Integer.MAX_VALUE) {
            locationKnown = true;
            board[rabbitRow][rabbitColumn] = 2;
        }
    }

    private void collectData(int direction) {
        int visible = look(direction);
        int distance = distance(direction);
        visibleMap.put(direction, visible);
        distanceMap.put(direction, distance);

        if (visible == 1) {
            haveSeenFox = true;
            lastDirectionToFox = direction;
        }
    }

    private void setEdgesAndCorners() {
        if ((rabbitRow == 0 && rabbitColumn == 0) ||
                (rabbitRow == 19 && rabbitColumn == 0) ||
                (rabbitRow == 0 && rabbitColumn == 19) ||
                (rabbitRow == 19 && rabbitColumn == 19)) {
            rabbitCorner = true;
        } else {
            rabbitCorner = false;
        }

        System.out.println("Corner? " + rabbitCorner);

        if (rabbitRow == 0 || rabbitRow == 19 || rabbitColumn == 0 || rabbitColumn == 19) {
            rabbitEdge = true;
        } else {
            rabbitEdge = false;
        }

        System.out.println("Edge? " + rabbitEdge);
    }

    private int turnAndMove(int base, int[] possibleMoves) {
        int i = 0;
        int intendedDirection = Model.turn(base, possibleMoves[i]);
        while (!canMove(intendedDirection)) {
            i++;
            if (i >= possibleMoves.length) {
                return random(0, 8);
            }
            intendedDirection = Model.turn(base, possibleMoves[i]);
        }
        return intendedDirection;
    }

    private int checkAndMove(int[] possibleMoves) {
        int i = 0;
        while (!canMove(possibleMoves[i])) {
            i++;
            if (i >= possibleMoves.length) {
                return random(0, 8);
            }
        }
        return possibleMoves[i];
    }
    
    private int moveRabbit(int direction) {
        int[] deltas = getDirections(direction);
        int yStep = deltas[0];
        int xStep = deltas[1];

        board[rabbitRow][rabbitColumn] = 4;

        rabbitRow += yStep;
        rabbitColumn += xStep;
        
        board[rabbitRow][rabbitColumn] = 2;

        lastMove = direction;
        return direction;
    }

    private int dontMoveRabbit() {
        return 8;
    }

    int decideMove() {
        System.out.println(boardToString());

        turnNumber++;

        if (!locationKnown) {
            locateRabbit();
            for (int i = 0; i < 8; i++) {
                collectData();
            }
            updateBoard();
        
            if (!locationKnown()) {
                if (!haveSeenFox) {
                    return moveRabbit(random(0, 8));
                } else {
                    return moveRabbit(turnAndMove(lastDirectionToFox, 
                                        new int[] {3, 5, 4, 1, 7, 2, 6, 0}));
                }
            } 
        } else { // we DO know the location of the rabbit
            for (int i = 0; i < 8; i++) {
                collectData();
            }
            updateBoard();
            
        }


        return moveRabbit(random(0, 8));
    }
}


