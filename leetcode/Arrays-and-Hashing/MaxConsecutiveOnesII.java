/**
 * LeetCode 487 - Max Consecutive Ones II
 * https://leetcode.com/problems/max-consecutive-ones-ii/
 *
 * Given binary array, find max consecutive 1s if you can flip at most one 0.
 *
 * Approach: Track ones on both sides of the "flipped" zero
 * - leftCount: consecutive 1s ending at current position
 * - rightCount: consecutive 1s before the last zero (+ the flipped zero itself)
 * - When we hit a 0: rightCount = leftCount + 1 (ones before zero + the flipped zero)
 * - Result = max(rightCount + leftCount) at each step
 *
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int leftCount = 0;
        int rightCount = 0;
        int result = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1) {
                leftCount++;
            }
            if (nums[i] == 0) {
                rightCount = leftCount + 1;  // ones before zero + flipped zero
                leftCount = 0;               // reset for ones after zero
            }
            result = Math.max(result, rightCount + leftCount);
        }
        return result;
    }
}
