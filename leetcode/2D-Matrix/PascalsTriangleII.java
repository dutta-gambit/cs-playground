import java.util.*;

/**
 * LeetCode 119 - Pascal's Triangle II
 * https://leetcode.com/problems/pascals-triangle-ii/
 *
 * Return the rowIndex-th row of Pascal's triangle.
 *
 * Approach: Build full triangle up to rowIndex, return last row
 * - Same as Pascal's Triangle I but return specific row
 * - Row i has i+1 elements, edges = 1, middle = sum of two above
 *
 * Time: O(n²) | Space: O(n²)
 */
class Solution {
    public List<Integer> getRow(int rowIndex) {
        List<List<Integer>> storePascalTriangle = new ArrayList<>();

        for (int i = 0; i <= rowIndex; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    row.add(storePascalTriangle.get(i - 1).get(j - 1)
                          + storePascalTriangle.get(i - 1).get(j));
                }
            }
            storePascalTriangle.add(row);
        }
        return storePascalTriangle.get(rowIndex);
    }
}
