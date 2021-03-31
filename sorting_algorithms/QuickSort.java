public class QuickSort extends Sorter {
    public QuickSort(int[] data) {
        super();
        this.name = "QuickSort";
    }

    public int[] run() {
        return this.data;
    }
}




/*
https://www.geeksforgeeks.org/quick-sort/
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