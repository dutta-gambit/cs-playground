/**
 * LeetCode 66 - Plus One
 * https://leetcode.com/problems/plus-one/
 *
 * Increment a large integer represented as an array of digits.
 *
 * Approach: Simulate addition right-to-left with carry
 * - If digit + carry > 9: set to 0, carry = 1
 * - If all digits are 9: create new array of size n+1 with result[0] = 1
 *
 * Key lesson: Never convert to int/Long for big-number problems — can exceed Long.MAX_VALUE (19 digits)
 * 
 * Time: O(n) | Space: O(1) or O(n) if all 9s
 */
class Solution {
    public int[] plusOne(int[] digits) {
        int carry = 1;
        int j = digits.length - 1;

        while (carry != 0 && j >= 0) {
            int res = digits[j] + 1;
            if (res > 9) {
                carry = 1;
                digits[j] = res % 10;
            } else {
                carry = 0;
                digits[j] = res;
            }
            j--;
        }

        if (carry == 1) {
            // All digits were 9 → need bigger array (999 → 1000)
            int[] result = new int[digits.length + 1];
            result[0] = 1;
            return result;
        }
        return digits;
    }
}
