import java.util.Arrays;

public class Rabbit extends Animal {
    private boolean haveSeenFox = false;
    private int lastDirectionToFox = 0;
    private boolean adjacentToWall = false;
    private int directionToAdjacentWall = 0;

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }

    int checkAndMove(int base, int[] possibleMoves) {
        for (int i = 0; i < possibleMoves.length; i++) {
            if (this.canMove(base + possibleMoves[i])) {
                // System.out.println(base + possibleMoves[i] + " moving.");
                return base + possibleMoves[i];
            }
        }
        // System.out.println(base + Arrays.toString(possibleMoves) + "Checked. Not moving.");
        return Model.STAY;
    }
    
    int decideMove() {
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.FOX) {
                haveSeenFox = true;
                lastDirectionToFox = i;
            }
        }
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.EDGE && this.distance(i) == 0) {
                adjacentToWall = true;
                directionToAdjacentWall = i;
            }
        }
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.EDGE && this.distance(i) > 0) {
                adjacentToWall = false;
            }
        }
        if (haveSeenFox) {
            if (adjacentToWall) {
                return checkAndMove(directionToAdjacentWall, new int[] {2, 6});
            }
            return checkAndMove(lastDirectionToFox, new int[] {3, 5, 1, 7});
        }
        return Model.STAY;
    }
}
