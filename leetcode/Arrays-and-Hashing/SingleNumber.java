/**
 * LeetCode 136 - Single Number
 * https://leetcode.com/problems/single-number/
 *
 * Every element appears twice except one. Find the single one.
 * Constraint: O(n) time, O(1) space.
 *
 * Approach: XOR all elements
 * - a ^ a = 0 (pairs cancel out)
 * - a ^ 0 = a (single element remains)
 * - XOR is commutative + associative (order doesn't matter)
 *
 * Why XOR is the ONLY approach for O(n) time + O(1) space:
 * - HashSet/HashMap: O(n) time but O(n) space
 * - Sort: O(1) space but O(n log n) time
 * - Math (2*sum(unique) - sum(all)): O(n) time but needs Set = O(n) space
 *
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res = res ^ nums[i];
        }
        return res;
    }
}
