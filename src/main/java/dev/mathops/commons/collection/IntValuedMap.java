package dev.mathops.commons.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * A map whose values are primitive integers, with convenience methods to increment, decrement, and adjust values
 * associated with keys.
 *
 * <p>
 * This class stores an array of nodes, each a linked list.  Key hash modulo the number of buckets selects a bucket,
 * then the contents of the bucket are linearly searched for an equal key.  The number of buckets is dynamically chosen
 * to ensure the load factor (number of entries / number of buckets) remains between a minimum and maximum value.
 *
 * @param <T> the key type
 */
public class IntValuedMap<T> {

    /** The default maximum load factor. */
    private static final float DEFAULT_MAX_LOAD_FACTOR = 2.0f;

    /** The default maximum load factor. */
    private static final float DEFAULT_MIN_LOAD_FACTOR = 0.5f;

    /** The minimum load factor. */
    private final float minLoadFactor;

    /** The maximum load factor. */
    private final float maxLoadFactor;

    /**
     * The buckets.  The number N of buckets is a power of 2, and the bucketMask is set to ensure that any hash ANDed
     * with the bucket mask gives a valid index into this list.
     */
    private List<Node<T>> buckets;

    /** The mask to apply to a hash to get a bucket index. */
    private int bucketMask;

    /** The number of entries currently in the map. */
    private int numEntries;

    /** The number of entries above which we should rehash. */
    private int growThreshold;

    /** The number of entries below which we should rehash. */
    private int shrinkThreshold;

    /**
     * Constructs an empty {@code IntValuedMap} with a specified initial capacity.
     */
    public IntValuedMap() {

        this.maxLoadFactor = DEFAULT_MAX_LOAD_FACTOR;
        this.minLoadFactor = DEFAULT_MIN_LOAD_FACTOR;

        final float avgLoadFactor = (this.maxLoadFactor + this.minLoadFactor) * 0.5f;

        // Start off with two buckets (with addresses 0 and 1)
        this.bucketMask = 0x01;
        this.buckets = new ArrayList<>(2);
        this.buckets.add(null);
        this.buckets.add(null);

        this.numEntries = 0;
        this.growThreshold = Math.round(this.maxLoadFactor + this.maxLoadFactor);
        this.shrinkThreshold = Math.round(this.minLoadFactor + this.minLoadFactor);
    }

    /**
     * Gets the number of entries in the map.
     *
     * @return the number of entries
     */
    public final int size() {

        return this.numEntries;
    }

    /**
     * Retrieves the list of keys in the map.
     *
     * @return the list of keys
     */
    public final List<T> getKeys() {

        final List<T> list = new ArrayList<>(this.numEntries);

        for (final Node<T> listHead : this.buckets) {
            Node<T> current = listHead;
            while (current != null) {
                list.add(current.key);
                current = current.next;
            }
        }

        return list;
    }

    /**
     * Gets the value associated with a key.
     *
     * @param key          the key
     * @param defaultValue a value to return if there is no value associated with the given key
     * @return the result
     */
    public final int get(final T key, final int defaultValue) {

        final int keyHash = key.hashCode();
        final int bucketIndex = keyHash & this.bucketMask;

        int result = defaultValue;

        Node<T> node = this.buckets.get(bucketIndex);
        while (node != null) {
            if (node.keyHash == keyHash && node.key.equals(key)) {
                result = node.value;
                break;
            }
            node = node.next;
        }

        return result;
    }

    /**
     * Sets the value associated with a key.
     *
     * @param key   the key
     * @param value the new value to associate with the key
     */
    public final void put(final T key, final int value) {

        final int newSize = this.numEntries + 1;
        if (newSize > this.growThreshold) {
            // The following will update bucketMask, buckets, growThreshold, shrinkThreshold
            rehash();
        }

        final int keyHash = key.hashCode();
        final int bucketIndex = keyHash & this.bucketMask;

        boolean searching = true;
        Node<T> node = this.buckets.get(bucketIndex);
        while (node != null) {
            if (node.keyHash == keyHash && node.key.equals(key)) {
                node.value = value;
                searching = false;
                break;
            }
            node = node.next;
        }

        if (searching) {
            final Node<T> newHead = new Node<>(key, keyHash, value);
            newHead.next = this.buckets.get(bucketIndex);
            this.buckets.set(bucketIndex, newHead);
            ++this.numEntries;
        }
    }

    /**
     * Tests whether this map has a value for a specified key.
     *
     * @param key the key
     * @return true if a value is defined for the specified key
     */
    public final boolean hasValue(final T key) {

        final int keyHash = key.hashCode();
        final int bucketIndex = keyHash & this.bucketMask;

        boolean hasValue = false;

        Node<T> node = this.buckets.get(bucketIndex);
        while (node != null) {
            if (node.keyHash == keyHash && node.key.equals(key)) {
                hasValue = true;
                break;
            }
            node = node.next;
        }

        return hasValue;
    }

    /**
     * Removes the value associated with a specified key.
     *
     * @param key the key
     * @return true if a value was found and removed
     */
    public final boolean remove(final T key) {

        final int keyHash = key.hashCode();
        final int bucketIndex = keyHash & this.bucketMask;

        boolean found = false;

        final Node<T> head = this.buckets.get(bucketIndex);
        if (head != null) {
            if (head.keyHash == keyHash && head.key.equals(key)) {
                this.buckets.set(bucketIndex, head.next);
                found = true;
            } else {
                Node<T> prior = head;
                Node<T> node = head.next;
                while (node != null) {
                    if (node.keyHash == keyHash && node.key.equals(key)) {
                        prior.next = node.next;
                        found = true;
                        break;
                    }
                    prior = node;
                    node = node.next;
                }
            }
        }

        if (found) {
            --this.numEntries;
            if (this.numEntries < this.shrinkThreshold) {
                // The following will update bucketMask, buckets, growThreshold, shrinkThreshold
                rehash();
            }
        }

        return found;
    }

    /**
     * Increments the value associated with a key.  If there is no value associated with the key, the value is
     * initialized to a provided default initial value, then incremented.  Equivalent to
     * {@code adjust(key, 1, defaultInitialValue)}.
     *
     * @param key                 the key
     * @param defaultInitialValue the default initial value to use when no value is defined for the key
     * @return the new value associated with the key
     */
    public final int increment(final T key, final int defaultInitialValue) {

        return adjust(key, 1, defaultInitialValue);
    }

    /**
     * Decrements the value associated with a key.  If there is no value associated with the key, the value is
     * initialized to a provided default initial value, then decremented.  Equivalent to *
     * {@code adjust(key, -1, defaultInitialValue)}.
     *
     * @param key                 the key
     * @param defaultInitialValue the default initial value to use when no value is defined for the key
     * @return the new value associated with the key
     */
    public final int decrement(final T key, final int defaultInitialValue) {

        return adjust(key, -1, defaultInitialValue);
    }

    /**
     * Adjusts the value associated with a key by a set amount (positive or negative).  If there is no value associated
     * with the key, the value is initialized to a provided default initial value, then adjusted.
     *
     * @param key                 the key
     * @param delta               the amount to add to the value associated with the key
     * @param defaultInitialValue the default initial value to use when no value is defined for the key
     * @return the new value associated with the key
     */
    public final int adjust(final T key, final int delta, final int defaultInitialValue) {

        // Ensure there is space in case we need to create a new node
        final int newSize = this.numEntries + 1;
        if (newSize > this.growThreshold) {
            // The following will update bucketMask, buckets, growThreshold, shrinkThreshold
            rehash();
        }

        final int keyHash = key.hashCode();
        final int bucketIndex = keyHash & this.bucketMask;

        boolean searching = true;
        int newValue = 0;
        Node<T> node = this.buckets.get(bucketIndex);
        while (node != null) {
            if (node.keyHash == keyHash && node.key.equals(key)) {
                node.value += delta;
                newValue = node.value;
                searching = false;
                break;
            }
            node = node.next;
        }

        if (searching) {
            newValue = defaultInitialValue + delta;
            final Node<T> newHead = new Node<>(key, keyHash, newValue);
            newHead.next = this.buckets.get(bucketIndex);
            this.buckets.set(bucketIndex, newHead);
            ++this.numEntries;
        }

        return newValue;
    }

    /**
     * Rehashes the table.  After this call, the number of buckets will provide a load factor between the object's
     * minimum and maximum.
     */
    private void rehash() {

        final int minBuckets = Math.round((float) this.numEntries / this.maxLoadFactor);
        final int maxBuckets = Math.round((float) this.numEntries / this.minLoadFactor);
        final int wantBuckets = (minBuckets + maxBuckets) >> 1;

        // Determine the actual number of buckets to be a power of 2, at least as large as "wantBuckets"
        int mask = 0x01;
        int newNumBuckets = 2;
        while (newNumBuckets < wantBuckets) {
            mask = (mask << 1) + 1;
            newNumBuckets <<= 1;
        }

        if (newNumBuckets != this.buckets.size()) {
//            Log.info("Rehashing for " + newNumBuckets + " buckets");

            this.bucketMask = mask;
            final List<Node<T>> newBuckets = new ArrayList<>(newNumBuckets);
            for (int i = 0; i < newNumBuckets; ++i) {
                newBuckets.add(null);
            }

            // Assign all nodes to new buckets
            for (final Node<T> head : this.buckets) {
                Node<T> node = head;
                while (node != null) {
                    final Node<T> flink = node.next;
                    final int newBucketIndex = node.keyHash & mask;
                    node.next = newBuckets.get(newBucketIndex);
                    newBuckets.set(newBucketIndex, node);
                    node = flink;
                }
            }

            this.buckets.clear();  // Eliminate orphaned references
            this.buckets = newBuckets;

            this.growThreshold = Math.round(this.maxLoadFactor * (float) newNumBuckets);
            this.shrinkThreshold = Math.round(this.minLoadFactor * (float) newNumBuckets);
        }
    }

    /** A node. */
    static class Node<T> {
        /** The next node in the singly-linked lists of nodes in a bucket. */
        Node<T> next = null;
        /** The node key. */
        final T key;
        /** The node key's hash. */
        final int keyHash;
        /** The value associated with the node. */
        int value;

        /**
         * Constructs a new {@code Node}.
         *
         * @param theKey     the key
         * @param theKeyHash the key hash
         * @param theValue   the value associated with the key
         */
        Node(final T theKey, final int theKeyHash, final int theValue) {

            this.key = theKey;
            this.keyHash = theKeyHash;
            this.value = theValue;
        }
    }
}
