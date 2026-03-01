import java.util.*;

/**
 * LeetCode 151 - Reverse Words in a String
 * https://leetcode.com/problems/reverse-words-in-a-string/
 *
 * Approach 1 (First attempt): Manual split with StringBuilder
 * - Built word list by scanning char-by-char, stored words + spaces
 * - Bug: empty strings added for consecutive/leading/trailing spaces
 * - Fix needed: only add to list when sb.length() > 0
 *
 * Approach 2 (Optimal): trim() + split("\\s+") + reverse loop
 * - trim() handles leading/trailing spaces
 * - split("\\s+") splits on 1+ whitespace (handles multiple spaces)
 * - Reverse iterate and join with single space
 *
 * Key takeaway: trim() + split("\\s+") handles all whitespace edge cases in one
 * line.
 *
 * Time: O(n) | Space: O(n)
 */
class Solution {
    public String reverseWords(String s) {
        String[] words = s.trim().split("\\s+");

        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i > 0) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
