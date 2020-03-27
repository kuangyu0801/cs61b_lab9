package lab9;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int bucketIndex = this.hash(key);
        return buckets[bucketIndex].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int bucketIndex = this.hash(key);
        if ( buckets[bucketIndex].get(key) == null) {
            size += 1;
        }
        buckets[bucketIndex].put(key, value);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<K> ();
        Iterator<K> myHashMapIterator = new MyHashMapIterator();
        while(myHashMapIterator.hasNext()) {
            keySet.add(myHashMapIterator.next());
        }
        return keySet;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {

        int bucketIndex = this.hash(key);
        V rVal = buckets[bucketIndex].remove(key);
        if(rVal != null) {
            size -= 1;
        }
        return rVal;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int bucketIndex = this.hash(key);
        V rVal = buckets[bucketIndex].get(key);
        if (rVal.equals(value) && (rVal != null)) {
            buckets[bucketIndex].remove(key);
            size -= 1;
        }
        return rVal;
    }

    public class MyHashMapIterator implements Iterator<K>{
        private int wizPos;

        public MyHashMapIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public K next() {
            int nextIndex = wizPos;
            K rKey = null;
            for(int i = 0; i < DEFAULT_SIZE; i += 1) {
                Iterator<K> bucketIterator = buckets[i].iterator();
                if(bucketIterator.hasNext()) {
                    if(nextIndex == 0) {
                        rKey = bucketIterator.next();
                        break;
                    }
                    nextIndex -= 1;
                }
            }
            wizPos += 1;
            return rKey;
        }
    }

    @Override
    public Iterator<K> iterator() {
        Iterator<K> myHashMapIterator = new MyHashMapIterator();
        return myHashMapIterator ;
    }
}
