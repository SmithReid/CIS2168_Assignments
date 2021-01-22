// import java.util.Arrays; // Arrays.toString() used for debugging
import java.lang.Integer;

public class Rabbit extends Animal {
    private boolean haveSeenFox = false;
    private int lastDirectionToFox = 0;
    private boolean adjacentToWall = false;
    private int lastDirectionToAdjacentWall = 0;
    private int turnsSinceSeenFox = Integer.MAX_VALUE;
    private int lastDistanceToFox = Integer.MAX_VALUE;
    private boolean repeatSighting = false;
    private boolean corner = false;
    private int[] cornerDirections = new int[2];

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }

    int checkAndMove(int base, int[] possibleMoves) {
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
        return intendedDirection;
    }
    
    int decideMove() {
        if (look(lastDirectionToAdjacentWall) != Model.EDGE || 
            distance(lastDirectionToAdjacentWall) > 1) {
            adjacentToWall = false;
        }
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.FOX) {
                if (turnsSinceSeenFox == 1) {
                    repeatSighting = true;
                }
                haveSeenFox = true;
                lastDirectionToFox = i;
                turnsSinceSeenFox = 0;
                lastDistanceToFox = distance(i);
            }
        }
        turnsSinceSeenFox++;
        if (turnsSinceSeenFox > 7) {
            repeatSighting = false;
        }

        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.EDGE && this.distance(i) == 1) {
                adjacentToWall = true;
                lastDirectionToAdjacentWall = i;
                for (int j = i; j < 8; j += 2) {
                    if (distance(j) < 4) {
                        corner = true;
                        cornerDirections = new int[] {i, j};
                    }
                }
            }
        }

        int countAdjacentBushes = 0;
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.BUSH && distance(i) == 1) {
                countAdjacentBushes++;
            }
        }

        if (countAdjacentBushes > 1 && turnsSinceSeenFox > 1) {
            return Model.STAY;
        }
        if (turnsSinceSeenFox < 5) {
            if (corner) {
                if (cornerDirections[0] - cornerDirections[1] == 2) {
                    return checkAndMove(cornerDirections[0], new int[] {3, 2, 4, 5, 6, 7, 1, 0});
                } else if (cornerDirections[0] - cornerDirections[1] == 6) {
                    return checkAndMove(cornerDirections[1], new int[] {3, 2, 4, 5, 6, 7, 1, 0});
                }
            }

            if (lastDistanceToFox < 3) {
                return checkAndMove(lastDirectionToFox, new int[] {3, 5, 4, 2, 6, 1, 7, 0});
            }
            if (repeatSighting) {
                return checkAndMove(lastDirectionToFox, new int[] {5, 3, 4, 7, 1, 6, 2, 0});
            }

// playing with running away from the fox and also walls, if both are visible.
            if (adjacentToWall && lastDirectionToFox - lastDirectionToAdjacentWall < 4) {
                return checkAndMove(lastDirectionToFox, new int[] {2, 1, 3, 4, 5, 6, 7, 0});
            } else if (adjacentToWall && lastDirectionToFox - lastDirectionToAdjacentWall == 4) {
                if (lastDistanceToFox > 3) {
                    return checkAndMove(lastDirectionToFox, new int[] {2, 6, 1, 7, 0, 3, 4, 5});
                } else {
                    return checkAndMove(lastDirectionToFox, new int[] {2, 6, 1, 7, 3, 4, 5, 0});
                }
            } else if (adjacentToWall) {
                return checkAndMove(lastDirectionToAdjacentWall, 
                                new int[] {2, 3, 4, 6, 5, 1, 7, 0});
            }

            // if this block is reached, the rabbit is not adjacent to a wall
            if (haveSeenFox) {
                return checkAndMove(lastDirectionToFox, new int[] {3, 5, 4, 1, 7, 2, 6, 0});
            }
        }
        return Model.STAY;
    }
}
