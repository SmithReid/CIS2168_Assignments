// Code written by Reid Smith
// Version began 1/25/2021
// Version 1.3

/* 
0s will denote unknown
1s will denote FOX
2s will denote RABBIT
3s will denote BUSHes
*/

import java.util.HashMap;

public class Rabbit extends Animal {
    private HashMap<Integer, Integer> moves = new HashMap<>();

    private int turnNumber = 0;

    private int lastFoxDirection;
    private int lastDistanceToFox;
    private int lastFoxTurn;
    private boolean haveSeenFox = false;

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }

    private void collectFoxData(int direction) {
        int visible = look(direction);
        int distance = distance(direction);

        if (visible == 1) {
            lastFoxDirection = direction;
            lastDistanceToFox = distance;
            lastFoxTurn = turnNumber;
            haveSeenFox = true;
        }
    }

    private void considerMove(int direction) {
        if (!canMove(direction)) {
            moves.put(direction, 0);
            return;
        }

        int weight = 0;
        if (direction == Model.turn(lastFoxDirection, 5))      weight = 1000;
        else if (direction == Model.turn(lastFoxDirection, 3)) weight = 900;
        else if (direction == Model.turn(lastFoxDirection, 4)) weight = 800;
        else if (direction == Model.turn(lastFoxDirection, 6)) weight = 700;
        else if (direction == Model.turn(lastFoxDirection, 2)) weight = 600;
        else if (direction == Model.turn(lastFoxDirection, 1)) weight = 500;
        else if (direction == Model.turn(lastFoxDirection, 7)) weight = 400;
        else weight = 300;

        weight += 32 * distance(direction);



        moves.put(direction, weight);
    }

    int decideMove() {
        for (int i = 0; i < 8; i++) {
            collectFoxData(i);
        }
        for (int i = 0; i < 8; i++) {
            considerMove(i);
        }
        int output = 8;
        int bestMoveScore = 0;
        for (int i = 0; i < 8; i++) {
            if (moves.get(i) > 0 && moves.get(i) > bestMoveScore) {
                bestMoveScore = moves.get(i);
                output = i;
            }
        }

        turnNumber++;
        return output;
    }
}