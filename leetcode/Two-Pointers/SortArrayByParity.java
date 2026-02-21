/**
 * LeetCode 905 - Sort Array By Parity
 * https://leetcode.com/problems/sort-array-by-parity/
 *
 * Return array with all evens before odds. Any order within even/odd groups is fine.
 *
 * Approach: Opposite-ends two pointers with extra array
 * - evenIndex fills from left, oddIndex fills from right
 *
 * Time: O(n) | Space: O(n)
 *
 * In-place alternative: swap nums[left] and nums[right] when left is odd and right is even
 */
class Solution {
    public int[] sortArrayByParity(int[] nums) {
        int[] res = new int[nums.length];
        int evenIndex = 0;
        int oddIndex = nums.length - 1;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 0) {
                res[evenIndex] = nums[i];
                evenIndex++;
            } else {
                res[oddIndex] = nums[i];
                oddIndex--;
            }
        }
        return res;
    }
}
