import java.util.*;

/**
 * LeetCode 347 - Top K Frequent Elements
 * https://leetcode.com/problems/top-k-frequent-elements/
 *
 * Approach 1: HashMap + Max-Heap (PriorityQueue) → O(n + m log m)
 * - Build frequency map, put entries in max-heap sorted by value, poll k times
 *
 * Approach 2 (Optimal): Bucket Sort → O(n)
 * - Index = frequency, value = list of elements with that frequency
 * - Walk backwards from highest index to collect top k
 *
 * Key learnings:
 * - PriorityQueue is min-heap by default; use (a,b) -> b.compareTo(a) for max-heap
 * - entrySet() gives key-value pairs; getKey() for element, getValue() for count
 * - Use Integer.compare() or .compareTo() instead of a-b to avoid overflow
 * - Collection hierarchy: Collection → List/Set (values() returns Collection)
 */
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequencyCount = new HashMap<>();
        int[] result = new int[k];

        for (int i : nums) {
            frequencyCount.put(i, frequencyCount.getOrDefault(i, 0) + 1);
        }

        int j = 0;
        PriorityQueue<Map.Entry<Integer, Integer>> pq =
            new PriorityQueue<>((a, b) -> b.getValue().compareTo(a.getValue()));
        pq.addAll(frequencyCount.entrySet());

        while (!pq.isEmpty() && j < k) {
            Map.Entry<Integer, Integer> entry = pq.poll();
            result[j] = entry.getKey();
            j++;
        }
        return result;
    }
}

// --- Approach 2: Bucket Sort (Optimal O(n)) ---
// class Solution {
//     public int[] topKFrequent(int[] nums, int k) {
//         Map<Integer, Integer> freq = new HashMap<>();
//         for (int num : nums) {
//             freq.put(num, freq.getOrDefault(num, 0) + 1);
//         }
//
//         // Bucket: index = frequency, value = elements with that frequency
//         List<Integer>[] buckets = new List[nums.length + 1];
//         for (var entry : freq.entrySet()) {
//             int f = entry.getValue();
//             if (buckets[f] == null) buckets[f] = new ArrayList<>();
//             buckets[f].add(entry.getKey());
//         }
//
//         int[] result = new int[k];
//         int idx = 0;
//         for (int i = buckets.length - 1; i >= 0 && idx < k; i--) {
//             if (buckets[i] != null) {
//                 for (int num : buckets[i]) {
//                     result[idx++] = num;
//                     if (idx == k) break;
//                 }
//             }
//         }
//         return result;
//     }
// }
