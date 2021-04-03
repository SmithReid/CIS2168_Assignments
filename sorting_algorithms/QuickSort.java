import java.util.Arrays;

public class QuickSort extends Sorter {
    public QuickSort(double[] data) {
        super(data);
        this.name = "QuickSort";
    }

    private int partition(int low, int high) {
        double pivot = this.data[high];
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {
            if (this.data[j] < pivot) {
                i++;
                double temp = this.data[i];
                this.data[i] = this.data[j];
                this.data[j] = temp;
            }
        }
        double temp = this.data[i + 1];
        this.data[i + 1] = this.data[high];
        this.data[high] = temp;
        return i + 1;
    }

    private void sortArray(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);

            sortArray(low, pi - 1); // before pi
            sortArray(pi + 1, high); // after pi
        }
    }

    public void run() {
        sortArray(0, this.data.length - 1);
    }
}




/*
https://www.geeksforgeeks.org/quick-sort/
// This function takes last element as pivot, places
// the pivot element at its correct position in sorted
// array, and places all smaller (smaller than pivot)
// to left of pivot and all greater elements to right
// of pivot
partition (arr[], low, high)
{
    // pivot (Element to be placed at right position)
    pivot = arr[high];  
 
    i = (low - 1)  // Index of smaller element and indicates the 
                   // right position of pivot found so far

    for (j = low; j <= high- 1; j++)
    {
        // If current element is smaller than the pivot
        if (arr[j] < pivot)
        {
            i++;    // increment index of smaller element
            swap arr[i] and arr[j]
        }
    }
    swap arr[i + 1] and arr[high])
    return (i + 1)
}

low  --> Starting index,  high  --> Ending index
quickSort(arr[], low, high)
{
    if (low < high)
    {
        // pi is partitioning index, arr[pi] is now
           // at right place
        pi = partition(arr, low, high);

        quickSort(arr, low, pi - 1);  // Before pi
        quickSort(arr, pi + 1, high); // After pi
    }
}
*/