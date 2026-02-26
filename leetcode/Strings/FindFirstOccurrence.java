/**
 * LeetCode 28 - Find the Index of the First Occurrence in a String
 * https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
 *
 * Approach 1: Built-in indexOf() — one-liner
 * Approach 2: Manual sliding window — substring comparison
 *
 * Time: O(n×m) | Space: O(m) for substring creation
 */

// --- Approach 1: Built-in ---
// class Solution {
//     public int strStr(String haystack, String needle) {
//         return haystack.indexOf(needle);
//     }
// }

// --- Approach 2: Manual sliding window ---
class Solution {
    public int strStr(String haystack, String needle) {
        for (int i = 0; i <= haystack.length() - needle.length(); i++) {
            if (haystack.substring(i, i + needle.length()).equals(needle)) {
                return i;
            }
        }
        return -1;
    }
}
