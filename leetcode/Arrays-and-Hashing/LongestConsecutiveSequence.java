import java.util.*;

/**
 * LeetCode 128 - Longest Consecutive Sequence
 * https://leetcode.com/problems/longest-consecutive-sequence/
 *
 * Approach 1: TreeSet + prev variable → O(n log n) time, O(n) space
 *   - TreeSet sorts + deduplicates, iterate with prev tracking
 *   - "prev variable pattern" — track previous value when no index access
 *
 * Approach 2: HashSet "end of sequence" → O(n) time, O(n) space (optimal)
 *   - Only count from END of sequence (!contains(num+1))
 *   - Count downward with while loop
 *   - Each number visited at most twice → O(n) total
 *
 * Key learnings:
 * - TreeSet.add() is O(log n) per op × n = O(n log n) total
 * - HashSet.add() is O(1) per op × n = O(n) total — per-op vs total cost!
 * - "prev variable pattern" eliminates need to copy into ArrayList for index access
 */

// --- Approach 1: TreeSet + prev variable (O(n log n)) ---
// class Solution {
//     public int longestConsecutive(int[] nums) {
//         if (nums.length <= 1) return nums.length;
//
//         Set<Integer> treeSet = new TreeSet<>();
//         for (int num : nums) treeSet.add(num);
//
//         Integer previous = null;
//         int result = 1;
//         int count = 1;
//
//         for (Integer key : treeSet) {
//             if (previous != null && key == previous + 1) {
//                 count++;
//             } else {
//                 count = 1;
//             }
//             result = Math.max(result, count);
//             previous = key;
//         }
//         return result;
//     }
// }

// --- Approach 2: HashSet "end of sequence" (O(n) optimal) ---
class Solution {
    public int longestConsecutive(int[] nums) {
        if (nums.length <= 1) return nums.length;

        Set<Integer> hashSet = new HashSet<>();
        for (int num : nums) hashSet.add(num);

        int result = 1;
        for (int num : hashSet) {
            // Only count from END of sequence (no successor)
            if (!hashSet.contains(num + 1)) {
                int current = num;
                int count = 1;
                while (hashSet.contains(current - 1)) {
                    count++;
                    current--;
                }
                result = Math.max(result, count);
            }
        }
        return result;
    }
}
