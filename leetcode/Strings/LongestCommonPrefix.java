/**
 * LeetCode 14 - Longest Common Prefix
 * https://leetcode.com/problems/longest-common-prefix/
 *
 * Approach: Start with shortest string as candidate prefix,
 * check startsWith() against all strings, trim from end until all match.
 *
 * Key methods: startsWith(), substring(), isEmpty()
 * Gotcha: Use .isEmpty() not != "" for string comparison
 *
 * Time: O(n × m²) where n = num strings, m = shortest string length
 * Space: O(1)
 */
class Solution {
    public String longestCommonPrefix(String[] strs) {
        String shortestString = "";
        int currSize = Integer.MAX_VALUE;
        for (String s : strs) {
            if (currSize > s.length()) {
                shortestString = s;
                currSize = s.length();
            }
        }

        while (!shortestString.isEmpty()) {
            int count = 0;
            for (int j = 0; j < strs.length; j++) {
                if (strs[j].startsWith(shortestString)) {
                    count++;
                }
            }
            if (count == strs.length) {
                return shortestString;
            }
            shortestString = shortestString.substring(0, shortestString.length() - 1);
        }
        return shortestString;
    }
}
