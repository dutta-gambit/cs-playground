import java.util.*;

/**
 * LeetCode 705 - Design HashSet (Bucket-based approach)
 * https://leetcode.com/problems/design-hashset/
 *
 * ⚠️ NOT my solution — LeetCode reference for later revision.
 *
 * How it works:
 * - Array of 100,000 buckets (lists)
 * - Hash function: key % MAX_LEN → bucket index
 * - Each bucket is a List<Integer> (chaining for collisions)
 * - add: hash → check if exists → append to bucket
 * - remove: hash → find position → remove from bucket
 * - contains: hash → scan bucket
 *
 * Time: O(n/k) average per operation (n = keys, k = buckets)
 * Space: O(k + n)
 */
class MyHashSet {
    private final int MAX_LEN = 100000; // the amount of buckets
    private List<Integer>[] set; // hash set implemented by array

    /** Returns the corresponding bucket index. */
    private int getIndex(int key) {
        return key % MAX_LEN;
    }

    /**
     * Search the key in a specific bucket. Returns -1 if the key does not existed.
     */
    private int getPos(int key, int index) {
        List<Integer> temp = set[index];
        if (temp == null) {
            return -1;
        }
        for (int i = 0; i < temp.size(); ++i) {
            if (temp.get(i) == key) {
                return i;
            }
        }
        return -1;
    }

    public MyHashSet() {
        set = (List<Integer>[]) new ArrayList[MAX_LEN];
    }

    public void add(int key) {
        int index = getIndex(key);
        int pos = getPos(key, index);
        if (pos < 0) {
            if (set[index] == null) {
                set[index] = new ArrayList<Integer>();
            }
            set[index].add(key);
        }
    }

    public void remove(int key) {
        int index = getIndex(key);
        int pos = getPos(key, index);
        if (pos >= 0) {
            set[index].remove(pos);
        }
    }

    public boolean contains(int key) {
        int index = getIndex(key);
        int pos = getPos(key, index);
        return pos >= 0;
    }
}
