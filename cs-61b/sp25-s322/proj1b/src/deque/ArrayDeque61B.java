package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque61B<T> implements Deque61B<T> {

    private T[] elements;
    private int size;
    private int front;
    public ArrayDeque61B() {
        elements = (T[]) new Object[8];
        size = 0;
        front = 0; // index of first el, offset for first element add
    }

    private void resizeUp() {
        T[] newArr = (T[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            int currIndex = Math.floorMod(front + i, elements.length);
            newArr[i] = elements[currIndex]; // start from beginning of newArr
        }
        elements = newArr;
        front = 0; // reset index
    }

    private void resizeDown() {
        int capacity = Math.max(elements.length / 2, 1); // resize down by a factor of 1/2, or 1 if length is 0
        T[] newArr = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            int currIndex = Math.floorMod(front + i, elements.length);
            newArr[i] = elements[currIndex]; // start from beginning of newArr
        }
        elements = newArr;
        front = 0; // reset index
    }

    @Override
    public void addFirst(T x) {
        if (size == elements.length) {
            resizeUp();
        }
        front = Math.floorMod(front - 1, elements.length);
        elements[front] = x;
        size++;
    }

    @Override
    public void addLast(T x) {
        if (size == elements.length) {
            resizeUp();
        }
        int editIndex = Math.floorMod(front + size, elements.length);
        elements[editIndex] = x;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int currIndex = Math.floorMod(front + i, elements.length);
            returnList.add(elements[currIndex]);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private final int SIZE_THRESHOLD = 16;
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (elements.length >= SIZE_THRESHOLD && size < elements.length / 4) {
            resizeDown();
        }
        T temp = elements[front];
        elements[front] = null;
        front = Math.floorMod(front + 1, elements.length);
        size--;
        return temp;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (elements.length >= SIZE_THRESHOLD && size < elements.length / 4) {
            resizeDown();
        }
        int lastIndex = Math.floorMod(front + size - 1, elements.length);
        T temp = elements[lastIndex];
        elements[lastIndex] = null;
        size--;
        return temp;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= elements.length) {
            return null;
        }
        int getIndex = Math.floorMod(front + index, elements.length);
        return elements[getIndex];
    }

    private T getRecursiveHelper(int index, int track) {
        if (index == 0) { // base case
            return elements[track];
        } else {
            int newTrack = Math.floorMod(track + 1, elements.length); // keep moving up the list
            return getRecursiveHelper(index - 1, newTrack);
        }
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index >= elements.length) {
            return null;
        }
        return getRecursiveHelper(index, front);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currIndex = front;
            private int numReturned = 0;

            @Override
            public boolean hasNext() {
                return numReturned < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    return null;
                }
                T el = elements[currIndex];
                currIndex = Math.floorMod(currIndex + 1, elements.length);
                numReturned++;
                return el;
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        switch (obj) {
            case null -> {
                return false;
            }
            case ArrayDeque61B<?> otherAList -> {
                if (this.size != otherAList.size) {
                    return false;
                }
                Iterator<?> otherIter = otherAList.iterator();
                for (T x : this) {
                    if (!otherIter.next().equals(x)) {
                        return false;
                    }
                }
                return true;
            }
            case LinkedListDeque61B<?> otherLList -> {
                if (this.size != otherLList.size()) {
                    return false;
                }
                Iterator<?> otherIter = otherLList.iterator();
                for (T x : this) {
                    if (!otherIter.next().equals(x)) {
                        return false;
                    }
                }
                return true;
            }
            default -> {
            }
        }
        if (obj instanceof LinkedListDeque61B<?> otherLList) {
            if (this.size != otherLList.size()) {
                return false;
            }
            Iterator<?> otherIter = otherLList.iterator();
            for (T x : this) {
                if (!otherIter.next().equals(x)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.toList().toString();
    }
}
