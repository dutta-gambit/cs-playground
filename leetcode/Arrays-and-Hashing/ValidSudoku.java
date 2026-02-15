import java.util.*;

/**
 * LeetCode 36 - Valid Sudoku
 * https://leetcode.com/problems/valid-sudoku/
 *
 * Approach 1: Three separate passes (rows, cols, boxes) — clearer
 * Approach 2: Single pass with 3 arrays of sets — more elegant
 *
 * Key formula for box index: (i / 3) * 3 + (j / 3)
 *   - i/3 = box row (0,1,2), j/3 = box col (0,1,2)
 *   - Maps 9x9 grid to 9 box indices (0-8)
 *
 * Time: O(1) — board is always 9x9. Space: O(1).
 */

// --- Approach 1: Three passes (row, col, box) ---
// class Solution {
//     public boolean isValidSudoku(char[][] board) {
//         Set<Integer> values;
//
//         // Check rows
//         for (int i = 0; i < 9; i++) {
//             values = new HashSet<>();
//             for (int j = 0; j < 9; j++) {
//                 if (Character.isDigit(board[i][j])) {
//                     if (!values.add(board[i][j] - '0')) return false;
//                 }
//             }
//         }
//
//         // Check columns
//         for (int i = 0; i < 9; i++) {
//             values = new HashSet<>();
//             for (int j = 0; j < 9; j++) {
//                 if (Character.isDigit(board[j][i])) {
//                     if (!values.add(board[j][i] - '0')) return false;
//                 }
//             }
//         }
//
//         // Check 3x3 boxes
//         for (int boxRow = 0; boxRow < 3; boxRow++) {
//             for (int boxCol = 0; boxCol < 3; boxCol++) {
//                 values = new HashSet<>();
//                 for (int i = boxRow * 3; i < boxRow * 3 + 3; i++) {
//                     for (int j = boxCol * 3; j < boxCol * 3 + 3; j++) {
//                         if (Character.isDigit(board[i][j])) {
//                             if (!values.add(board[i][j] - '0')) return false;
//                         }
//                     }
//                 }
//             }
//         }
//         return true;
//     }
// }

// --- Approach 2: Single pass ---
class Solution {
    @SuppressWarnings("unchecked")
    public boolean isValidSudoku(char[][] board) {
        Set<Integer>[] rows = new HashSet[9];
        Set<Integer>[] cols = new HashSet[9];
        Set<Integer>[] boxes = new HashSet[9];

        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Character.isDigit(board[i][j])) {
                    int val = board[i][j] - '0';
                    int boxIndex = (i / 3) * 3 + (j / 3);

                    if (!rows[i].add(val) || !cols[j].add(val) || !boxes[boxIndex].add(val)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
