/**
 * LeetCode 26 - Remove Duplicates from Sorted Array
 * https://leetcode.com/problems/remove-duplicates-from-sorted-array/
 *
 * Given sorted array, remove duplicates in-place. Return count of unique elements.
 *
 * Approach: Two pointers — reader (i) scans ahead, writer (j) tracks write position
 * - If nums[i] == nums[i-1]: duplicate → skip (advance i only)
 * - Else: unique → write nums[i] at position j, advance both
 *
 * Time: O(n) | Space: O(1)
 *
 * Key pattern: "Reader-Writer" two pointers for in-place array compaction
 */
class Solution {
    public int removeDuplicates(int[] nums) {
        int i = 1;  // reader — scans through array
        int j = 1;  // writer — next position to write unique element

        while (i < nums.length) {
            if (nums[i] == nums[i - 1]) {
                i++;        // duplicate, skip
                continue;
            } else {
                nums[j] = nums[i];  // unique, write it
                j++;
                i++;
            }
        }
        return j;  // j = count of unique elements
    }
}
