import java.util.*;

/**
 * LeetCode 128 - Longest Consecutive Sequence
 * https://leetcode.com/problems/longest-consecutive-sequence/
 *
 * Approach 1: TreeSet (sorted + dedup) → O(n log n) time
 * - Insert all into TreeSet, iterate in order, track consecutive runs
 *
 * Approach 2 (Optimal): HashSet + "start of sequence" trick → O(n) time
 * - Only start counting from elements where (num-1) is NOT in set
 * - Each element visited at most twice → O(n)
 *
 * Space: O(n) for both
 *
 * Key learnings:
 * - TreeSet vs HashSet: TreeSet sorts (O(log n) per op), HashSet doesn't (O(1) per op)
 * - "Start of sequence" check: if (!set.contains(num - 1)) avoids redundant counting
 * - Objects.equals() for Integer comparison (avoids == cache trap)
 */

// --- Approach 1: TreeSet (sorted iteration) ---
class Solution {
    public int longestConsecutive(int[] nums) {
        if (nums.length <= 1) {
            return nums.length;
        }

        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            set.add(nums[i]);
        }

        if (set.size() <= 1) {
            return set.size();
        }

        Iterator<Integer> it = set.iterator();
        int previous = it.next();
        int count = 1;
        int result = 1;

        while (it.hasNext()) {
            int current = it.next();
            if (Objects.equals(previous, current - 1)) {
                count++;
                result = Math.max(result, count);
            } else {
                count = 1;
            }
            previous = current;
        }

        return result;
    }
}

// --- Approach 2: HashSet + start-of-sequence (Optimal O(n)) ---
// class Solution {
//     public int longestConsecutive(int[] nums) {
//         Set<Integer> set = new HashSet<>();
//         for (int num : nums) set.add(num);
//
//         int longest = 0;
//         for (int num : set) {
//             if (!set.contains(num - 1)) {   // only start from sequence beginning
//                 int count = 1;
//                 while (set.contains(num + count)) {
//                     count++;
//                 }
//                 longest = Math.max(longest, count);
//             }
//         }
//         return longest;
//     }
// }
