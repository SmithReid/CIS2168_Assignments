// Code written by Reid Smith
// Version began 1/25/2021
// Version 1.2

/* 
0s will denote unknown
1s will denote FOX
2s will denote RABBIT
3s will denote BUSHes
*/

import java.util.HashMap;

public class Rabbit extends Animal {
    private boolean locationKnown = false;

    private int rabbitRow = 1000;
    private int rabbitColumn = 1000;

    private boolean haveSeenFox;
    private int runAway = 8;
    private int lastDistanceToFox = 1000;
    private int lastFoxTurn = 0;

    private int turnNumber = 0;
    private int lastTurn = 8;
    private int lastCornerTurn = -1000;

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
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

        if (rabbitRow < 1000 && rabbitColumn < 1000) {
            locationKnown = true;
        }
    }

    private void collectFoxData(int direction) {
        int visible = look(direction);
        int distance = distance(direction);

        if (visible == 1) {
            runAway = direction;
            lastDistanceToFox = distance;
            lastFoxTurn = turnNumber;
            haveSeenFox = true;
        }
    }

    private boolean bushCorner() {
        int adjacentObjects = 0;
        for (int i = 7; i >= 0; i--) {
            if (distance(i) == 1) {
                adjacentObjects++;
            }
            if (adjacentObjects > 0) {
                runAway = i;
            }
        }
        return (adjacentObjects > 1);
    }

    private boolean corner() {
        if (rabbitRow == 0 && rabbitColumn == 0) {
            runAway = 7;
            lastCornerTurn = turnNumber;
            return true;
        } else if (rabbitRow == 0 && rabbitColumn == 19) {
            runAway = 1;
            lastCornerTurn = turnNumber;
            return true;
        } else if (rabbitRow == 19 && rabbitColumn == 0) {
            runAway = 5;
            lastCornerTurn = turnNumber;
            return true;
        } else if (rabbitRow == 19 && rabbitColumn == 19) {
            runAway = 3; 
            lastCornerTurn = turnNumber;
            return true;
        }
        return false;
    }
    
    private int repeatLastTurn() {
        return turnAndMove(lastTurn, new int[] {0, 7, 6, 1, 5, 2, 3, 4});
    }

    private int turnAndMove(int base, int[] possibleMoves) {
        int i = 0;
        int intendedDirection = Model.turn(base, possibleMoves[i]);
        while (!canMove(intendedDirection)) {
            i++;
            intendedDirection = Model.turn(base, possibleMoves[i]);
        }
        return intendedDirection;
    }

    private int checkAndMove(int[] possibleMoves) {
        int i = 0;
        while (!canMove(possibleMoves[i])) {
            i++;
        }
        return possibleMoves[i];
    }
    
    private int moveRabbit(int direction) {
        if (!locationKnown) {
            return direction;
        }

        int[] deltas = getDirections(direction);
        int yStep = deltas[0];
        int xStep = deltas[1];

        rabbitRow += yStep;
        rabbitColumn += xStep;

        turnNumber++;
        lastTurn = direction;
        return direction;
    }

    private int dontMoveRabbit() {
        turnNumber++;
        return 8;
    }

    int decideMove() {
        if (!locationKnown) {
            locateRabbit();
            for (int i = 0; i < 8; i++) {
                collectFoxData(i);
            }
            if (!locationKnown) {
                if ((lastFoxTurn == turnNumber) || 
                            (haveSeenFox && 
                            turnNumber - lastFoxTurn < 4 && 
                            lastDistanceToFox < 7)) {
                    return moveRabbit(turnAndMove(runAway, 
                                new int[] {5, 3, 4, 6, 2, 1, 7, 0}));
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                collectFoxData(i);
            }
            if (turnNumber - lastCornerTurn < 3) {
                return moveRabbit(repeatLastTurn());
            }
            if ((lastFoxTurn == turnNumber) || 
                        (turnNumber - lastFoxTurn < 3 && lastDistanceToFox < 5) ||
                        corner()) {
                return moveRabbit(turnAndMove(runAway, 
                                new int[] {5, 3, 4, 6, 2, 1, 7, 0}));
            } else {
                return dontMoveRabbit(); // this is really all that might be worth changing.
            }
        }
        return dontMoveRabbit();
    }
}




