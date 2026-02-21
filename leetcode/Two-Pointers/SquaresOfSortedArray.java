import java.util.*;

/**
 * LeetCode 977 - Squares of a Sorted Array
 * https://leetcode.com/problems/squares-of-a-sorted-array/
 *
 * Given sorted array, return squares in sorted order.
 *
 * Approach 1: TreeMap frequency count → O(n log n)
 * Approach 2: Opposite-ends two pointers → O(n) optimal
 *   - Sorted array means largest squares are at edges (most negative or most positive)
 *   - Compare abs values from both ends, fill result array from the back
 *
 * Time: O(n) | Space: O(n)
 */

// --- Approach 1: TreeMap (O(n log n)) ---
// class Solution {
//     public int[] sortedSquares(int[] nums) {
//         Map<Integer, Integer> frequencyCount = new TreeMap<>();
//         for (int i = 0; i < nums.length; i++) {
//             frequencyCount.put(Math.abs(nums[i]),
//                 frequencyCount.getOrDefault(Math.abs(nums[i]), 0) + 1);
//         }
//         int j = 0;
//         for (Map.Entry<Integer, Integer> result : frequencyCount.entrySet()) {
//             int k = result.getValue();
//             while (k-- > 0) {
//                 nums[j++] = result.getKey() * result.getKey();
//             }
//         }
//         return nums;
//     }
// }

// --- Approach 2: Opposite-ends two pointers (O(n)) ---
class Solution {
    public int[] sortedSquares(int[] nums) {
        int[] res = new int[nums.length];
        int left = 0, right = nums.length - 1;

        for (int i = nums.length - 1; i >= 0; i--) {
            if (Math.abs(nums[left]) > Math.abs(nums[right])) {
                res[i] = nums[left] * nums[left];
                left++;
            } else {
                res[i] = nums[right] * nums[right];
                right--;
            }
        }
        return res;
    }
}
