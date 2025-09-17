import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {

    private class Node {
        T data;
        Node next;
        Node last;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.last = this;
        }
    }

    private Node sentinel;
    private int size;
    public LinkedListDeque61B() {
        sentinel = new Node(null);
        sentinel.next = sentinel;
        sentinel.last = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node newNode = new Node(x);
        newNode.next = sentinel.next;
        newNode.last = sentinel;
        newNode.next.last = newNode;
        sentinel.next = newNode;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node newNode = new Node(x);
        newNode.next = sentinel;
        newNode.last = sentinel.last;
        newNode.last.next = newNode;
        sentinel.last = newNode;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node currNode = sentinel.next;
        while (currNode != sentinel) {
            returnList.add(currNode.data);
            currNode = currNode.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0) && (sentinel.next == sentinel) && (sentinel.last == sentinel);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T removedData = sentinel.next.data;
        sentinel.next = sentinel.next.next;
        sentinel.next.last = sentinel;
        size--;
        return removedData;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removedData = sentinel.last.data;
        sentinel.last = sentinel.last.last;
        sentinel.last.next = sentinel;
        size--;
        return removedData;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node currNode = sentinel.next;
        for (int i = 0; i < index; i++) {
            currNode = currNode.next;
        }
        return currNode.data;
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node node, int index) {
        if (index == 0) {
            return node.data;
        }
        return getRecursiveHelper(node.next, index - 1);
    }
}
