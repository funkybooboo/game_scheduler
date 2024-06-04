package heap.leftist_heap;

public class MaxLeftistHeap<E extends Comparable<? super E>> extends LeftistHeap<E> {

    public MaxLeftistHeap() {
        super();
    }

    public MaxLeftistHeap(E[] data) {
        super(data);
    }

    @Override
    public LeftistHeap<E> clone() {
        LeftistHeap<E> clone = new MaxLeftistHeap<>();
        clone(root, clone.root);
        return clone;
    }
    private void clone(Node<E> node, Node<E> cloneNode) {
        if (node == null) {
            return;
        }
        cloneNode.data = node.data;
        cloneNode.nullPathLength = node.nullPathLength;
        cloneNode.left = new Node<>(null);
        cloneNode.right = new Node<>(null);
        clone(node.left, cloneNode.left);
        clone(node.right, cloneNode.right);
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
