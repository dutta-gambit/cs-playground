/**
 * LeetCode 70 - Climbing Stairs
 * https://leetcode.com/problems/climbing-stairs/
 *
 * You can climb 1 or 2 steps. How many distinct ways to reach the top?
 * This is essentially the Fibonacci sequence: f(n) = f(n-1) + f(n-2)
 *
 * Approach 1: Recursion + Memoization (top-down DP) → O(n) time, O(n) space
 * Approach 2: Bottom-up DP → O(n) time, O(1) space
 *
 * Key learnings:
 * - Memoization bug: must call the SAME function with the SAME memo, and return memo[n]
 * - This is Fibonacci in disguise: f(1)=1, f(2)=2, f(n)=f(n-1)+f(n-2)
 */

// --- Approach 1: Recursion + Memoization (top-down) ---
// class Solution {
//     public int climbStairs(int n) {
//         int[] memo = new int[n + 1];
//         return scaleStairs(n, memo);
//     }
//
//     private int scaleStairs(int n, int[] memo) {
//         if (n == 0 || n == 1 || n == 2) return n;
//         if (memo[n] != 0) return memo[n];
//         memo[n] = scaleStairs(n - 1, memo) + scaleStairs(n - 2, memo);
//         return memo[n];  // return the STORED value, don't recompute!
//     }
// }

// --- Approach 2: Bottom-up DP (O(1) space) ---
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        int prev2 = 1;  // f(1)
        int prev1 = 2;  // f(2)
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }
}
