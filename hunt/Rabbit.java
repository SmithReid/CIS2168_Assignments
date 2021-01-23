//Code Written by Reid Smith
// Version began 1/22/2021
// Version 1.0

import java.util.HashMap;
import java.lang.Integer;

import java.util.Arrays; // debugging only likely

public class Rabbit extends Animal {
    private int turnNumber = -1;

    private boolean haveSeenFox = false;
    private int lastDirectionToFox = 0;

    private HashMap<Integer, Integer> visibleMap = new HashMap<>();
    private HashMap<Integer, Integer> distanceMap = new HashMap<>();

    private boolean rabbitCorner;
    private boolean rabbitEdge;

    private boolean locationKnown = false;
    private int rabbitRow = Integer.MAX_VALUE;
    private int rabbitColumn = Integer.MAX_VALUE;

    /* When we collectData each step, 

    4s will denote empty

    0s will denote unknown
    1s will denote FOX
    2s will denote RABBIT
    3s will denote BUSHes
    */
    private int[][] board = new int[20][20];

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j] = 0;
            }
        }
    }

    private String boardToString() {
        String output = "";
        for (int i = 0; i < board.length; i++) {
            output += Arrays.toString(board[i]) + "\n";
        }
        return output;
    }

    private int[] getDirections(int i) {
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

    private void collectData(int direction) {
        int visible = look(direction);
        int distance = distance(direction);
        visibleMap.put(direction, visible);
        distanceMap.put(direction, distance);

        if (visible == 1) {
            haveSeenFox = true;
            lastDirectionToFox = direction;
        }

        int[] steps = getDirections(direction);
        int yStep = steps[0];
        int xStep = steps[1];

        int yToSet = rabbitRow;
        int xToSet = rabbitColumn;

        while (0 <= yToSet && yToSet <= 19 && 0 <= xToSet && xToSet <= 19) {
            board[yToSet][xToSet] = 4;
            yToSet += yStep;
            xToSet += xStep;
        }
        board[rabbitRow][rabbitColumn] = 2;

        if ((rabbitRow == 0 && rabbitColumn == 0) ||
                (rabbitRow == 19 && rabbitColumn == 0) ||
                (rabbitRow == 0 && rabbitColumn == 19) ||
                (rabbitRow == 19 && rabbitColumn == 19)) {
            rabbitCorner = true;
        } else {
            rabbitCorner = false;
        }

        if (rabbitRow == 0 || rabbitRow == 19 || rabbitColumn == 0 || rabbitColumn == 19) {
            rabbitEdge = true;
        } else {
            rabbitEdge = false;
        }
    }

    private int turnAndMove(int base, int[] possibleMoves) {
        /* checkAndMove() is sort of a wrapper around Model.turn()
        with the added functionality of checking all moves in an array and choosing
        the first acceptable option. */
        int i = 0;
        int intendedDirection = Model.turn(base, possibleMoves[i]);
        while (!canMove(intendedDirection)) {
            i++;
            if (i >= possibleMoves.length) {
                return Model.STAY;
            }
            intendedDirection = Model.turn(base, possibleMoves[i]);
        }

        int[] deltas = getDirections(intendedDirection);
        int yStep = deltas[0];
        int xStep = deltas[1];

        rabbitColumn += yStep;
        rabbitRow += xStep;

        return intendedDirection;
    }

    private int checkAndMove(int[] possibleMoves) {
        int i = 0;
        while (!canMove(possibleMoves[i])) {
            i++;
            if (i >= possibleMoves.length) {
                return Model.STAY;
            }
        }

        int[] deltas = getDirections(possibleMoves[i]);
        int yStep = deltas[0];
        int xStep = deltas[1];

        rabbitColumn += yStep;
        rabbitRow += xStep;

        return possibleMoves[i];
    }

    private int moveToCorner() {
        if (rabbitEdge && !rabbitCorner) {
            int directionToAdjacentEdge = 0;
            for (int i = 0; i < 8; i += 2) {
                if (visibleMap.get(i) == 4 && distanceMap.get(i) == 1) {
                    directionToAdjacentEdge = i;
                }
            }
            if (distanceMap.get((directionToAdjacentEdge) + 2) - (distanceMap.get(directionToAdjacentEdge) + 6) > 0) {
                return turnAndMove(directionToAdjacentEdge, new int[] {6, 5, 7, 4, 0, 3, 1, 2});
            } else {
                return turnAndMove(directionToAdjacentEdge, new int[] {2, 1, 3, 4, 0, 1, 3, 6});
            }
        } else if (rabbitCorner) {
            return Model.STAY;
        } else {
            if (rabbitRow <= 9 && rabbitColumn <= 9) {
                return checkAndMove(new int[] {7, 0, 6, 5, 1, 4, 2, 3});
            } else if (rabbitRow >= 10 && rabbitColumn <= 9) {
                return checkAndMove(new int[] {5, 4, 6, 7, 3, 0, 2, 1});
            } else if (rabbitRow <= 9 && rabbitColumn >= 10) {
                return checkAndMove(new int[] {1, 0, 2, 3, 7, 4, 6, 5});
            } else {
                return checkAndMove(new int[] {3, 2, 4, 1, 5, 6, 0, 7});
            }
        }
    }

    private int moveAlongEdge() {
        return random(0, 8);
    }

    private int moveAwayFromCorner() {
        return random(0, 8);
    }

    int decideMove() {
        turnNumber++;
        if (!locationKnown) {
            locateRabbit();
        }
        if (!locationKnown) {
            return random(0, 8);
        } else {
            for (int i = 0; i < 8; i++) {
                collectData(i);
            }
            if (!haveSeenFox) {
                return moveToCorner();
            } else {
                if (rabbitCorner) {
                    if (lastDirectionToFox % 2 == 1) {
                        return moveAlongEdge();
                    } else {
                        return moveAwayFromCorner();
                    }
                }
            }
        }
        return Model.STAY;

    }
}