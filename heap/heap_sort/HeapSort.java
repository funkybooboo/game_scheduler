package heap.heap_sort;

public abstract class HeapSort<E extends Comparable<? super E>> {

    public void sort(E[] arr) {
        int n = arr.length;
        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        // One by one extract an element from heap
        for (int i = n-1; i >= 0; i--) {
            // Move current root to end
            E temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            // call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    abstract public void heapify(E[] array, int sizeOfHeap, int rootIndex);

    protected static void printArray(Integer[] arr) {
        for (Integer integer : arr) System.out.print(integer + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] arr1 = {12, 11, 13, 5, 6, 7};
        Integer[] arr2 = {12, 11, 13, 5, 6, 7};

        HeapSort<Integer> minHeapSort = new MinHeapSort<>();
        HeapSort<Integer> maxHeapSort = new MaxHeapSort<>();
        minHeapSort.sort(arr1);
        maxHeapSort.sort(arr2);

        System.out.println("Sorted arr1 is");
        printArray(arr1);
        System.out.println("Sorted arr2 is");
        printArray(arr2);

    }

}
