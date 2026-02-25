import java.util.*;

/**
 * LeetCode 118 - Pascal's Triangle
 * https://leetcode.com/problems/pascals-triangle/
 *
 * Generate first numRows of Pascal's triangle.
 *
 * Approach: Build row by row
 * - First and last element of each row = 1
 * - Middle elements = sum of two elements above: result[i-1][j-1] + result[i-1][j]
 *
 * Gotcha: Must create each row (new ArrayList) before filling it. Use add(), not set().
 *
 * Time: O(n²) | Space: O(n²)
 */
class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || i == j) {
                    row.add(1);
                } else {
                    row.add(result.get(i - 1).get(j - 1) + result.get(i - 1).get(j));
                }
            }
            result.add(row);
        }
        return result;
    }
}
