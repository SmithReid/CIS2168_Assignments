// import java.util.Arrays; // Arrays.toString() used for debugging
import java.lang.Integer;

public class Rabbit extends Animal {
    private boolean haveSeenFox = false;
    private int lastDirectionToFox = 0;
    private boolean adjacentToWall = false;
    private int lastDirectionToAdjacentWall = 0;
    private int turnsSinceSeenFox = Integer.MAX_VALUE;
    private int lastDistanceToFox = Integer.MAX_VALUE;

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
                haveSeenFox = true;
                lastDirectionToFox = i;
                turnsSinceSeenFox = 0;
                lastDistanceToFox = distance(i);
            }
        }
        turnsSinceSeenFox++;
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.EDGE && this.distance(i) == 1) {
                adjacentToWall = true;
                lastDirectionToAdjacentWall = i;
            }
        }
        if (lastDistanceToFox < 3) {
            return checkAndMove(lastDirectionToFox, new int[] {3, 5, 4, 2, 6, 1, 7, 0});
        }
        if (haveSeenFox && turnsSinceSeenFox < 5) {
            if (adjacentToWall) {
                return checkAndMove(lastDirectionToAdjacentWall, new int[] {2, 6, 4, 1, 3, 5, 7, 0});
            }
            return checkAndMove(lastDirectionToFox, new int[] {3, 5, 4, 1, 7, 2, 6, 0});
        }
        return Model.STAY;
    }
}
