package heap.leftist_heap;

public class MaxLeftistHeap<E extends Comparable<? super E>> extends LeftistHeap<E> {

    public MaxLeftistHeap() {
        super();
    }

    public MaxLeftistHeap(E[] data) {
        super(data);
    }

    @Override
    protected Node<E> merge(Node<E> heap1, Node<E> heap2) {
        if (heap1 == null) {
            return heap2;
        }else if (heap2 == null) {
            return heap1;
        } else if (heap1.data.compareTo(heap2.data) > 0) {
            return swap(heap1, heap2);
        } else {
            return swap(heap2, heap1);
        }
    }


}
