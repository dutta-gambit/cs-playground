import java.util.Arrays;

/**
 * LeetCode 1051 - Height Checker
 * https://leetcode.com/problems/height-checker/
 *
 * Count how many students are not standing in non-decreasing height order.
 *
 * Approach: Sort a copy, compare with original, count mismatches.
 * Time: O(n log n) | Space: O(n)
 */
class Solution {
    public int heightChecker(int[] heights) {
        int misMatch = 0;
        int[] expectedHeights = Arrays.copyOf(heights, heights.length);
        Arrays.sort(expectedHeights);

        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != expectedHeights[i]) {
                misMatch++;
            }
        }
        return misMatch;
    }
}
