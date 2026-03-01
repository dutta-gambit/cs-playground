import java.util.*;

/**
 * LeetCode 557 - Reverse Words in a String III
 * https://leetcode.com/problems/reverse-words-in-a-string-iii/
 *
 * Reverse characters within each word, keeping word order same.
 *
 * Approach: Split → two-pointer swap on char[] per word → rejoin
 * - toCharArray() to get mutable char[]
 * - Swap from both ends inward (same as Reverse String #344)
 * - sb.append(char[]) accepts char[] directly
 *
 * Syntax bugs hit:
 * - "//s+" instead of "\\s+" (wrong regex escape)
 * - Used .charAt()/.setCharAt() on char[] (those are String methods, not array)
 * - sb.append(s) instead of sb.append(str) — appended original input, not
 * reversed word
 *
 * Time: O(n) | Space: O(n)
 */
class Solution {
    public String reverseWords(String s) {
        String[] words = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            char[] str = words[i].toCharArray();
            int j = 0;
            int k = str.length - 1;
            while (j < k) {
                char temp = str[j];
                str[j] = str[k];
                str[k] = temp;
                j++;
                k--;
            }
            sb.append(str);
            if (i != words.length - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
