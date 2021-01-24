// @author Reid Smith
// Version began 1/23/2020
// Version 1.1 (Significant restart using much of the structure of 1.0)

import java.util.HashMap;
import java.lang.Integer;
import java.util.Collections;
import java.util.Random;

import java.util.Arrays; // debugging only likely

/* When we collectData each step, 

4s will denote empty

0s will denote unknown
1s will denote FOX
2s will denote RABBIT
3s will denote BUSHes
*/

public class Rabbit extends Animal {
    private int turnNumber = -1;

    private HashMap<Integer, Integer> visibleMap = new HashMap<>();
    private HashMap<Integer, Integer> distanceMap = new HashMap<>();

    private int rabbitRow = Integer.MAX_VALUE;
    private int rabbitColumn = Integer.MAX_VALUE;

    private boolean locationKnown = false;
    private boolean rabbitEdge = false;
    private boolean rabbitCorner = false;

    private boolean haveSeenFox = false;
    private int lastDirectionToFox;
    private boolean runClockwise = false;
    private boolean foundCorner = false;

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }

    // https://www.journaldev.com/32661/shuffle-array-java with VERY minor modification
    private int[] shuffledOptions() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7};
        
        Random rand = new Random();
        
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
        return array;
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

    private int getQuadrant() {
        if (rabbitRow <= 9 && rabbitColumn <=9) {
            return 0;
        } else if (rabbitRow <= 9 && rabbitColumn >= 10) {
            return 1; 
        } else if (rabbitRow >= 10 && rabbitColumn <= 9) {
            return 2; 
        } else {
            return 3;
        }
    }

    private boolean runClockwise(int direction) {
        switch (getQuadrant()) {
            case 0: 
                return (4 <= direction && direction <= 7);
            case 1: 
                return !(2 <= direction && direction <= 5);
            case 2: 
                return !(3 <= direction && direction <= 6);
            default:
                return (1 <= direction && direction <= 4);
        }
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
        }
    }

/*

    private boolean rabbitCorner() {
        int adjacentObjects = 0;
        for (int i = 0; i < 8; i += 2) {
            if (distanceMap.get(i) == 1) {
                adjacentObjects++;
            }
        }
        if (adjacentObjects > 1) {
            return true;
        }
        return false;
    }

*/

    // outputs an array where length is number of objects next to rabbit at right angles
    // and values are the directions towards those objects. 
    // so, if rabbit is in corner in quadrant 0 with no adjacent bushes, 
        // output = {0, 6}
    private int[] adjacentRightObjects() {
        int count = 0;
        for (int i = 0; i < 8; i += 2) {
            if (distanceMap.get(i) == 1) {
                count++;
            }
        }
        int[] output = new int[count];
        int outputIndex = 0;
        for (int i = 0; i < 8; i += 2) {
            if (distanceMap.get(i) == 1) {
                output[outputIndex] = i;
                outputIndex++;
            }
        }
        return output;
    }

    private void collectData(int direction) {
        int visible = look(direction);
        int distance = distance(direction);
        visibleMap.put(direction, visible);
        distanceMap.put(direction, distance);

        if (visible == 1) {
            lastDirectionToFox = direction;

            if (!haveSeenFox) {
                runClockwise = runClockwise(lastDirectionToFox);
            }
            haveSeenFox = true;
        }
    }

    private int getFurthestEdge() {
        int maxDistance = 0;
        int output = 0;
        for (int i = 0; i < 8; i += 2) {
            if (visibleMap.get(i) == 0 && distanceMap.get(i) > maxDistance) {
                maxDistance = distanceMap.get(i);
                output = i;
            }
        }
        return output;
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
        
        return direction;
    }

    private int dontMoveRabbit() {
        return 8;
    }

    private int moveToCorner() {
        int quadrant = getQuadrant();
        if (quadrant == 0) {
            if (rabbitRow > rabbitColumn) {
                return checkAndMove(new int[] {7, 0, 6, 1, 5, 4, 2, 3});
            } else {
                return checkAndMove(new int[] {7, 6, 0, 5, 1, 4, 2, 3});
            }
        } else if (quadrant == 1) {
            if (rabbitRow > 19 - rabbitColumn) {
                return checkAndMove(new int[] {1, 0, 2, 7, 3, 6, 4, 5});
            } else {
                return checkAndMove(new int[] {1, 2, 0, 7, 3, 6, 4, 5});
            }
        } else if (quadrant == 3) {
            if (19 - rabbitRow > 19 - rabbitColumn) {
                return checkAndMove(new int[] {3, 4, 2, 7, 0, 1, 5, 6});
            } else {
                return checkAndMove(new int[] {3, 2, 4, 7, 0, 1, 5, 6});
            }
        } else {
            if (19 - rabbitRow > rabbitColumn) {
                return checkAndMove(new int[] {5, 6, 4, 1, 0, 2, 7, 3});
            } else {
                return checkAndMove(new int[] {5, 4, 6, 1, 0, 2, 7, 3});
            }  
        }
    }

    private int clockMove() {
        int[] adjacentRightObjects = adjacentRightObjects();
        int quadrant = getQuadrant();
        if (adjacentRightObjects.length > 1) { // if in corner
            if (runClockwise) {
                if (quadrant == 0) {
                    return checkAndMove(new int[] {1, 2, 3, 0, 4, 5, 6, 7});
                } else if (quadrant == 1) {
                    return checkAndMove(new int[] {3, 4, 5, 2, 1, 6, 7, 0});
                } else if (quadrant == 2) {
                    return checkAndMove(new int[] {5, 6, 7, 4, 0, 1, 2, 3});
                } else {
                    return checkAndMove(new int[] {7, 0, 1, 6, 5, 4, 2, 3});
                }
            } else {
                if (quadrant == 0) {
                    return checkAndMove(new int[] {5, 4, 3, 6, 2, 7, 0, 1});
                } else if (quadrant == 1) {
                    return checkAndMove(new int[] {7, 6, 5, 0, 4, 3, 2, 1});
                } else if (quadrant == 2) {
                    return checkAndMove(new int[] {1, 0, 7, 2, 6, 5, 4, 3});
                } else {
                    return checkAndMove(new int[] {3, 2, 1, 4, 0, 7, 6, 5});
                }
            }
        } else if (adjacentRightObjects.length == 1) {
            if (runClockwise) {
                return turnAndMove(adjacentRightObjects[0], 
                            new int[] {2, 1, 3, 0, 4, 5, 7, 6});
            } else {
                return turnAndMove(adjacentRightObjects[0], 
                            new int[] {6, 5, 7, 4, 0, 1, 3, 2});
            }
        } else {
            return moveToCorner();
        }

    }

/**********************************************************************/

    int decideMove() {
        // System.out.println(boardToString());

        turnNumber++;

        if (!locationKnown) {
            locateRabbit();
            for (int i = 0; i < 8; i++) {
                collectData(i);
            }
            // updateBoard();
        
            if (!locationKnown) {
                if (!haveSeenFox) {
                    if (canMove(getFurthestEdge())) {
                        return getFurthestEdge();
                    }
                    return 8;
                } else {
                    return moveRabbit(turnAndMove(lastDirectionToFox, 
                                        new int[] {3, 5, 4, 1, 7, 2, 6, 0}));
                }
            } 
        } else { // we DO know the location of the rabbit
            for (int i = 0; i < 8; i++) {
                collectData(i);
            }
            // updateBoard();
            if (!haveSeenFox) {
                if (!foundCorner) {
                    if (adjacentRightObjects().length > 1) {
                        foundCorner = true;
                        dontMoveRabbit();
                    }
                    return moveRabbit(moveToCorner());
                } else {
                    dontMoveRabbit();
                }
            } else {
                if (adjacentRightObjects().length > 1) {
                    foundCorner = true;
                    return moveRabbit(clockMove());
                } else if (adjacentRightObjects().length == 1) {
                    return moveRabbit(clockMove());
                } else if (!foundCorner) {
                        return moveRabbit(moveToCorner());
                } else {
                    return dontMoveRabbit();
                }
            }
        }
        return moveRabbit(checkAndMove(shuffledOptions()));
    }

/**********************************************************************/


/*
    private int kite() {
        if (inCorner() && !visibleMap.contains(1)) {
            if (lastDirectionToFox % 2 == 1) {
                for (int i = 0; i < 8; i += 2) {
                    if (distanceMap.get(i) > 1) {
                        return turnAndMove(i, new int[] {0, 1, 7, 2, 6, 3, 5, 4});
                    }
                }
            }
        }

        if (rabbitRow <= 9 && rabbitColumn <= 9) {

        }
    }
*/
}




