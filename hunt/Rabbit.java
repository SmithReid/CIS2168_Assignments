public class Rabbit extends Animal {


    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }
    
    int decideMove() {
		private boolean canSeeFox = false;
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.FOX) {
                canSeeFoxNow = haveSeenFox = true;
                directionToFox = i;
                distanceToFox = distance(i);
            }
        }
    }
}
