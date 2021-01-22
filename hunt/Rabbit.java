import java.util.Arrays;

public class Rabbit extends Animal {
    private boolean haveSeenFox = false;
    private int lastDirectionToFox = 0;
    private boolean adjacentToWall = false;
    private int lastDirectionToAdjacentWall = 0;

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }

    int checkAndMove(int base, int[] possibleMoves) {
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
            }
        }
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.EDGE && this.distance(i) == 1) {
                adjacentToWall = true;
                lastDirectionToAdjacentWall = i;
            }
        }
        if (haveSeenFox) {
            if (adjacentToWall) {
                return checkAndMove(lastDirectionToAdjacentWall, new int[] {2, 6, 4, 1, 3, 5, 7, 0});
            }
            return checkAndMove(lastDirectionToFox, new int[] {3, 5, 1, 7, 2, 4, 6, 0});
        }
        return Model.STAY;
    }
}
