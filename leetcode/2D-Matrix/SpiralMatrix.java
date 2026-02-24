import java.util.*;

/**
 * LeetCode 54 - Spiral Matrix
 * https://leetcode.com/problems/spiral-matrix/
 *
 * Traverse matrix in spiral order: → ↓ ← ↑ → ↓ ← ↑ ...
 *
 * Approach: 4 boundaries (top, bottom, left, right) shrinking inward
 * - Each step: fix one dimension, traverse the other, then shrink boundary
 * - if checks before ← and ↑ prevent double-counting on single row/col
 *
 * Key rules:
 * - Never use boundary variables as loop counters (use separate j/i)
 * - Boundaries only change AFTER a full edge traversal, by exactly 1
 *
 * Time: O(m×n) | Space: O(1) excluding output
 */
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        List<Integer> result = new ArrayList<>();

        int top = 0, bottom = m - 1;
        int left = 0, right = n - 1;

        while (top <= bottom && left <= right) {
            // → Go RIGHT along top row
            for (int j = left; j <= right; j++)
                result.add(matrix[top][j]);
            top++;

            // ↓ Go DOWN along right column
            for (int i = top; i <= bottom; i++)
                result.add(matrix[i][right]);
            right--;

            // ← Go LEFT along bottom row (if rows remain)
            if (top <= bottom) {
                for (int j = right; j >= left; j--)
                    result.add(matrix[bottom][j]);
                bottom--;
            }

            // ↑ Go UP along left column (if cols remain)
            if (left <= right) {
                for (int i = bottom; i >= top; i--)
                    result.add(matrix[i][left]);
                left++;
            }
        }
        return result;
    }
}
