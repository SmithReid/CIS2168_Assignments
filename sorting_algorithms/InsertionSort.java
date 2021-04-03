public class InsertionSort extends Sorter {
    public InsertionSort(double[] data) {
        super(data);
        this.name = "InsertionSort";
    }

    public void run() {
        int n = data.length;
        for (int i = 1; i < n; ++i) {
            double key = data[i];
            int j = i - 1;
 
            // Move elements of arr[0..i-1], that are
               // greater than key, to one position ahead
               // of their current position
            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j = j - 1;
            }
            data[j + 1] = key;
        }
    }
}

/*
https://www.geeksforgeeks.org/insertion-sort/
// Java program for implementation of Insertion Sort
class InsertionSort {
    // Function to sort array using insertion sort
    void sort(int arr[])
    {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
 
            // Move elements of arr[0..i-1], that are
               // greater than key, to one position ahead
               // of their current position
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }
 
    // A utility function to print array of size n
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
 
        System.out.println();
    }
 
    // Driver method
    public static void main(String args[])
    {
        int arr[] = { 12, 11, 13, 5, 6 };
 
        InsertionSort ob = new InsertionSort();
        ob.sort(arr);
 
        printArray(arr);
    }
} // This code is contributed by Rajat Mishra. 
*/