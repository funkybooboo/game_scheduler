package heap.leftist_heap;

public abstract class LeftistHeap<E extends Comparable<? super E>> {

    protected static class Node<E> {
        E data;
        Node<E> left;
        Node<E> right;
        int nullPathLength;

        public Node(E data) {
            this.data = data;
            this.nullPathLength = 0;
        }
    }

    protected Node<E> root;

    public LeftistHeap() {
        root = null;
    }

    public LeftistHeap(E[] data) {
        this();
        for (E e : data) {
            insert(e);
        }

    }

    protected abstract Node<E> merge(Node<E> heap1, Node<E> heap2);

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(E data) {
        root = merge(new Node<>(data), root);
    }

    public E delete() {
        if (isEmpty()) {
            return null;
        }
        E data = root.data;
        root = merge(root.left, root.right);
        return data;
    }

    protected void swapChildren(Node<E> node) {
        if (node == null) return;
        if (node.left == null && node.right != null) {
            node.left = node.right;
            node.right = null;
        } else if (node.left != null && node.right == null){
            node.right = node.left;
            node.left = null;
        } else {
            Node<E> temp = node.left;
            node.left = node.right;
            node.right = temp;
        }
    }

    protected Node<E> swap(Node<E> heap1, Node<E> heap2) {
        if (heap1.left == null) {
            heap1.left = heap2;
        } else {
            heap1.right = merge(heap1.right, heap2);
            if (heap1.left.nullPathLength < heap1.right.nullPathLength) {
                swapChildren(heap1);
                heap1.nullPathLength = heap1.right.nullPathLength + 1;
            }
        }
        return heap1;
    }

    public void print() {
        if (root == null) {
            System.out.println("Empty tree");
        } else {
            System.out.println("\n" + print(root, new StringBuilder(), 1));
        }
    }

    private StringBuilder print(Node<E> currentNode, StringBuilder stringBuilder, int depth) {
        if (currentNode == null) return stringBuilder;
        int spacing = 5;
        print(currentNode.right, stringBuilder, depth + spacing);
        stringBuilder.append(String.format("%" + depth + "d\n", currentNode.data));
        print(currentNode.left, stringBuilder, depth + spacing);
        return stringBuilder;
    }

    public static void main(String[] args) {
        Integer[] data = {12, 11, 13, 5, 6, 7, 44, 4, 8, 77, 12 ,1, 215, 423, 2, 3, 4, 7, 8};

        LeftistHeap<Integer> minLeftistHeap = new MinLeftistHeap<>(data);
        minLeftistHeap.print();
        System.out.println("--------------------");
        LeftistHeap<Integer> maxLeftistHeap = new MaxLeftistHeap<>(data);
        maxLeftistHeap.print();

    }



}
