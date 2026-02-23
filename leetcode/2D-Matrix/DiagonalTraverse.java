import java.util.*;

/**
 * LeetCode 498 - Diagonal Traverse
 * https://leetcode.com/problems/diagonal-traverse/
 *
 * Traverse a matrix diagonally in zigzag order.
 *
 * Approach:
 * - Total diagonals = m + n - 1 (k goes from 0 to m+n-2)
 * - On diagonal k, elements satisfy: row + col = k → col = k - row
 * - Collect each diagonal top-to-bottom, reverse on even k for zigzag
 *
 * Key insights:
 * - col = k - row eliminates inner j loop (O(1) per element)
 * - Bounds check: col >= 0 && col < n
 * - Zigzag = just Collections.reverse() on alternating diagonals
 *
 * Time: O(m*n) | Space: O(min(m,n)) for diagonal list
 */
class Solution {
    public int[] findDiagonalOrder(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        int[] result = new int[m * n];
        int z = 0;

        for (int k = 0; k <= (m + n - 2); k++) {
            List<Integer> diagonals = new ArrayList<>();

            for (int row = 0; row < m; row++) {
                int col = k - row;
                if (col >= 0 && col < n) {
                    diagonals.add(mat[row][col]);
                }
            }

            // Even k → reverse (up-right ↗), Odd k → keep (down-left ↙)
            if (k % 2 == 0) Collections.reverse(diagonals);

            for (int val : diagonals) {
                result[z++] = val;
            }
        }
        return result;
    }
}
