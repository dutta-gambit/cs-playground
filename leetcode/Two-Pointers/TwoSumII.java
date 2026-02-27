/**
 * LeetCode 167 - Two Sum II (Input Array Is Sorted)
 * https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/
 *
 * Approach: Two pointers — left and right, move based on sum vs target
 * - sum > target → shrink right (smaller number needed)
 * - sum < target → grow left (larger number needed)
 * - Sorted property guarantees this works in one pass
 *
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int[] result = new int[2];
        int i = 0;
        int j = numbers.length - 1;

        while (i < j) {
            if (numbers[i] + numbers[j] == target) {
                result[0] = i + 1;
                result[1] = j + 1;
                break;
            } else if (numbers[i] + numbers[j] > target) {
                j--;
            } else {
                i++;
            }
        }
        return result;
    }
}
