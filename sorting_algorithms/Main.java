import java.util.Random;
import java.util.Arrays;
import java.io.PrintStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class Main {
    private static final int DATA_SIZE_LIMIT = 18; // 2 ^ DATA_SIZE_LIMIT will be used

    private static int getPow(int base, int exp) {
        int output = 1;
        for (int i = 0; i <= exp; i++) {
            output = output * base;
        }
        return output;
    }

    private static double[] generateData(int size) {
        double[] output =  new double[size];
        Random rd = new Random();
        for (int i = 0; i < output.length; i++) {
            output[i] = rd.nextDouble();
        }
        return output;
    }

    private static boolean isSorted(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1])
                return false;
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(new File("outfiles/data.csv"), true));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        ps.println(dtf.format(now) + ",,,");
        ps.println("sorterName,dataSize,runTime(ms),");
        for (int i = 6; i <= DATA_SIZE_LIMIT; i++) {
            int dataSize = getPow(2, i);

            Sorter[] sorters = {new QuickSort(generateData(dataSize)), 
                                new InsertionSort(generateData(dataSize)), 
                                new TimSort(generateData(dataSize))};

            // without the next two lines, in testing, 
                // results were skewed by as much as a millisecond
            Sorter dummy = new Sorter();
            dummy.run();

            for (Sorter sorter : sorters) {
                System.out.println(sorter.name + ": ");
                long startTime = System.nanoTime();
                sorter.run();
                if (!isSorted(sorter.data))
                    System.out.println("DATA NOT SORTED.");
                long ns = System.nanoTime() - startTime;
                double runTime = (float) ns / 1000.0;
                System.out.println(sorter.name + " " + dataSize + 
                                ": " + runTime + "ms");
                System.out.println();
                ps.println(sorter.name + "," + dataSize + "," + runTime + ",");
            }
        }
        ps.println(",,,");
        ps.close();
    }
}