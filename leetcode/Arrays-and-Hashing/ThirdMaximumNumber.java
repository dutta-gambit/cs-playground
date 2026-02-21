/**
 * LeetCode 414 - Third Maximum Number
 * https://leetcode.com/problems/third-maximum-number/
 *
 * Return third distinct maximum. If doesn't exist, return the max.
 *
 * Approach: Three passes — find 1st max, then 2nd (excluding 1st), then 3rd (excluding 1st & 2nd)
 * - Boolean flag tracks if third max actually exists (handles Integer.MIN_VALUE edge case)
 *
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public int thirdMax(int[] nums) {
        int firstMax = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;
        int thirdMax = Integer.MIN_VALUE;
        boolean thirdMaxExists = false;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= firstMax) {
                firstMax = Math.max(firstMax, nums[i]);
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= secondMax && nums[i] != firstMax) {
                secondMax = Math.max(secondMax, nums[i]);
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= thirdMax && nums[i] != firstMax && nums[i] != secondMax) {
                thirdMaxExists = true;
                thirdMax = Math.max(thirdMax, nums[i]);
            }
        }

        return thirdMaxExists ? thirdMax : firstMax;
    }
}
