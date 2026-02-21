/**
 * LeetCode 27 - Remove Element
 * https://leetcode.com/problems/remove-element/
 *
 * Remove all occurrences of val in-place. Return count of remaining elements.
 *
 * Approach: Reader-Writer two pointers
 * - Reader i scans, writer j tracks write position
 * - Skip elements equal to val, write everything else
 *
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public int removeElement(int[] nums, int val) {
        int j = 0;  // writer
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[j] = nums[i];
                j++;
            }
        }
        return j;
    }
}
