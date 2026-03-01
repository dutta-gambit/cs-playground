import java.util.*;
import javafx.util.Pair;

/**
 * LeetCode 706 - Design HashMap (Bucket-based approach)
 * https://leetcode.com/problems/design-hashmap/
 *
 * ⚠️ NOT my solution — LeetCode reference for later revision.
 *
 * How it works:
 * - Array of 100,000 buckets (lists of key-value Pairs)
 * - Hash function: key % MAX_LEN → bucket index
 * - Each bucket is a List<Pair<Integer, Integer>> (chaining for collisions)
 * - put: hash → if key exists update value, else append new pair
 * - get: hash → find key in bucket → return value or -1
 * - remove: hash → find key → remove pair from bucket
 *
 * Difference from HashSet:
 * - Stores Pair<key, value> instead of just key
 * - put() must handle both insert AND update
 *
 * Time: O(n/k) average per operation (n = keys, k = buckets)
 * Space: O(k + n)
 */
class MyHashMap {
    private final int MAX_LEN = 100000;
    private List<Pair<Integer, Integer>>[] map;

    private int getIndex(int key) {
        return key % MAX_LEN;
    }

    private int getPos(int key, int index) {
        List<Pair<Integer, Integer>> temp = map[index];
        if (temp == null) {
            return -1;
        }
        for (int i = 0; i < temp.size(); ++i) {
            if (temp.get(i).getKey() == key) {
                return i;
            }
        }
        return -1;
    }

    public MyHashMap() {
        map = (List<Pair<Integer, Integer>>[]) new ArrayList[MAX_LEN];
    }

    public void put(int key, int value) {
        int index = getIndex(key);
        int pos = getPos(key, index);
        if (pos < 0) {
            if (map[index] == null) {
                map[index] = new ArrayList<Pair<Integer, Integer>>();
            }
            map[index].add(new Pair(key, value));
        } else {
            map[index].set(pos, new Pair(key, value));
        }
    }

    public int get(int key) {
        int index = getIndex(key);
        int pos = getPos(key, index);
        if (pos < 0) {
            return -1;
        } else {
            return map[index].get(pos).getValue();
        }
    }

    public void remove(int key) {
        int index = getIndex(key);
        int pos = getPos(key, index);
        if (pos >= 0) {
            map[index].remove(pos);
        }
    }
}
