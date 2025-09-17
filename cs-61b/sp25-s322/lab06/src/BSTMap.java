import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class Node {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private int size;

    public BSTMap() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (!containsKey(key)) {
            this.size++;
        }
        root = generateTree(root, key, value);
    }

    private Node generateTree(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value);
        }
        int cmp = node.key.compareTo(key);
        if (cmp < 0) { // go left
            node.left = generateTree(node.left, key, value);
        } else if (cmp > 0) { // go right
            node.right = generateTree(node.right, key, value);
        } else {
            node.value = value;
        }
        return node;
    }

    @Override
    public V get(K key) {
        Node currNode = root;
        while (currNode != null) {
            int cmp = currNode.key.compareTo((K) key);
            if (cmp != 0) {
                currNode = compareResult(currNode, cmp);
            } else {
                return currNode.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        Node currNode = root;
        while (currNode != null) {
            int cmp = currNode.key.compareTo((K) key);
            if (cmp == 0) {
                return true;
            }
            currNode = compareResult(currNode, cmp);
        }
        return false;
    }

    private Node compareResult(Node node, int cmp) {
        if (cmp < 0) {
            return node.left;
        } else if (cmp > 0) {
            return node.right;
        }
        return node;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
