/**
 * LeetCode 344 - Reverse String
 * https://leetcode.com/problems/reverse-string/
 *
 * Approach: Two pointers — swap from both ends inward
 * - i starts at 0, j starts at end, swap and move toward center
 *
 * Key: Problem gives char[] (not String) so we CAN modify in-place
 * - char[]: access with s[i], mutable
 * - String: access with s.charAt(i), immutable
 *
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public void reverseString(char[] s) {
        int i = 0;
        int j = s.length - 1;

        while (i < j) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            i++;
            j--;
        }
    }
}
