/**
 * LeetCode 1275 - Find Winner on a Tic Tac Toe Game
 * https://leetcode.com/problems/find-winner-on-a-tic-tac-toe-game/
 *
 * Simulate tic-tac-toe moves and determine winner, draw, or pending.
 *
 * Approach: Build the board, then check all win conditions (rows, cols, diagonals)
 * - Even-indexed moves = A (X), odd-indexed = B (O)
 * - Check 3 rows, 3 cols, 2 diagonals for matching non-empty cells
 * - If no winner and moves < 9 → Pending, else → Draw
 *
 * Time: O(1) — board is always 3×3. Space: O(1).
 */
class Solution {
    public String tictactoe(int[][] moves) {
        char[][] board = new char[3][3];
        int totalMoveCount = 0;

        for (int i = 0; i < moves.length; i++) {
            int[] arr = moves[i];
            board[arr[0]][arr[1]] = (i % 2 == 0) ? 'X' : 'O';
            totalMoveCount++;
        }

        Character winner = checkIfWinPossible(board);

        if (winner == null && totalMoveCount < 9) return "Pending";
        if (winner == null) return "Draw";
        if (winner == 'X') return "A";
        return "B";
    }

    private Character checkIfWinPossible(char[][] board) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == 'X' || board[i][0] == 'O') &&
                board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }
        // Check columns
        for (int i = 0; i < 3; i++) {
            if ((board[0][i] == 'X' || board[0][i] == 'O') &&
                board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }
        // Check diagonals
        if ((board[0][0] == 'X' || board[0][0] == 'O') &&
            board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if ((board[0][2] == 'X' || board[0][2] == 'O') &&
            board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }
        return null;
    }
}
