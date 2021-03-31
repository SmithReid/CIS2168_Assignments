public class Main {
    private static final int DATA_SIZE_LIMIT = 12; // 2 ^ DATA_SIZE_LIMIT will be used

    private static int getPow(int base, int exp) {
        int output = 1;
        for (int i = 0; i <= exp; i++) {
            output = output * base;
        }
        return output;
    }

    private static int[] generateData(int size) {
        return new int[size];
    }

    public static void main(String[] args) {
        for (int i = 6; i < DATA_SIZE_LIMIT; i++) {
            int dataSize = getPow(2, DATA_SIZE_LIMIT);
            int[] randomData = generateData(dataSize);

            Sorter[] sorters = {new QuickSort(randomData), 
                                new InsertionSort(randomData), 
                                new TimSort(randomData)};

            // without the next two lines, in testing, 
                // results were skewed by as much as a millisecond
            Sorter dummy = new Sorter();
            dummy.run();

            for (Sorter sorter : sorters) {
                System.out.println(sorter.name + ": ");
                long startTime = System.nanoTime();
                sorter.run();
                long runTime = System.nanoTime() - startTime;
                System.out.println(sorter.name + " " + dataSize + 
                                " data size runtime (ns): " + runTime);
                System.out.println();
            }
        }
    }
}