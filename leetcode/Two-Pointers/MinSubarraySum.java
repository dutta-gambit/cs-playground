/**
 * LeetCode 209 - Minimum Size Subarray Sum
 * https://leetcode.com/problems/minimum-size-subarray-sum/
 *
 * Find minimum length subarray with sum >= target.
 *
 * Approach: Sliding window — expand j, shrink i while sum >= target
 * - Key: use while (not if) for shrinking — finds minimum by shrinking as much as possible
 * - Window size = j - i (not j - i + 1 when j is post-incremented)
 *
 * Gotchas:
 * - Start total = 0 (not nums[0]) to avoid double-counting
 * - Return 0 if no valid subarray exists
 *
 * Time: O(n) — each element added/removed at most once | Space: O(1)
 */
class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int i = 0;
        int total = 0;
        int result = Integer.MAX_VALUE;

        for (int j = 0; j < nums.length; j++) {
            total += nums[j];
            while (total >= target) {
                result = Math.min(result, j - i + 1);
                total -= nums[i];
                i++;
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }
}
