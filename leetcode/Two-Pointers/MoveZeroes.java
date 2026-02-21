/**
 * LeetCode 283 - Move Zeroes
 * https://leetcode.com/problems/move-zeroes/
 *
 * Move all 0s to end while maintaining relative order of non-zero elements. In-place.
 *
 * Approach: Reader-Writer two pointers (same pattern as Remove Duplicates)
 * - Writer j tracks next write position for non-zero elements
 * - Reader scans, writes non-zeros at j
 * - Fill remaining positions with 0
 *
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public void moveZeroes(int[] nums) {
        int j = 0;  // writer

        // Pass 1: compact non-zeros to the front
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[j] = nums[i];
                j++;
            }
        }

        // Pass 2: fill remaining with zeros
        for (int k = j; k < nums.length; k++) {
            nums[k] = 0;
        }
    }
}
