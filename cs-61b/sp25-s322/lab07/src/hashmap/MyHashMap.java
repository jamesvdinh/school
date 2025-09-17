package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadFactor;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this.buckets = new Collection[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.size = 0;
    }

    public MyHashMap(int initialCapacity) {
        this.buckets = new Collection[initialCapacity];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.buckets = new Collection[initialCapacity];
        this.loadFactor = loadFactor;
        this.size = 0;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private void resize() {
        Collection<Node>[] newBuckets = new Collection[buckets.length * 2];
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    int index = Math.floorMod(node.key.hashCode(), newBuckets.length);
                    if (newBuckets[index] == null) {
                        newBuckets[index] = createBucket();
                    }
                    newBuckets[index].add(node);
                }
            }
        }
        buckets = newBuckets;
    }

    // Your code won't compile until you do so!
    @Override
    public void put(K key, V value) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }

        // case 1: key already exists in bucket -> update value
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                node.value = value;
                return; // end method
            }
        }

        // case 2: add new node to bucket collection
        Node newNode = new Node(key, value);
        buckets[index].add(newNode);
        size++;
        if (size > buckets.length * loadFactor) { // check if # elements is greater than N
            resize();
        }
    }

    @Override
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[index] == null) {
            return null;
        }
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[index] == null) {
            return false;
        }

        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        buckets = new Collection[DEFAULT_CAPACITY];
        size = 0;
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
