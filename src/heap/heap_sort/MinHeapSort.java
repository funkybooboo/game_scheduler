package heap.heap_sort;

public class MinHeapSort<E extends Comparable<? super E>> extends HeapSort<E> {

	// To heapify a subtree rooted with node rootIndex which is an index in array[].
	@Override
	public void heapify(E[] array, int sizeOfHeap, int rootIndex) {
		int largest = rootIndex;
		int left = 2 * rootIndex + 1;
		int right = 2 * rootIndex + 2;
		// If left child is larger than root
		if (left < sizeOfHeap && (array[left].compareTo(array[largest]) > 0)) {
			largest = left;
		}
		// If right child is larger than largest so far
		if (right < sizeOfHeap && (array[right].compareTo(array[largest]) > 0)) {
			largest = right;
		}
		// If largest is not root
		if (largest != rootIndex) {
			E swap = array[rootIndex];
			array[rootIndex] = array[largest];
			array[largest] = swap;
			// Recursively heapify the affected sub-tree
			heapify(array, sizeOfHeap, largest);
		}
	}

}
