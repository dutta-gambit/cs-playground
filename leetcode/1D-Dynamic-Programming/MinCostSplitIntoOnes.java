/**
 * Minimum Cost to Split into Ones (Medium) — Weekly Contest
 *
 * Split integer n into n ones. Each split of x into (a, b) costs a*b.
 * Find minimum total cost.
 *
 * Pattern spotted by computing by hand:
 * n=1 → 0, n=2 → 1, n=3 → 3, n=4 → 6, n=5 → 10, n=6 → 15, n=7 → 21
 * = n*(n-1)/2 (triangular numbers)
 *
 * Approach 1 (Submitted): Recursive — minCost(n) = (n-1) + minCost(n-1)
 * - Optimal split is always (1, n-1) costing (n-1), then recurse on (n-1)
 * - Penalty taken: returned 1 for n=1 instead of 0
 *
 * Approach 2 (O(1)): Direct formula — n*(n-1)/2
 *
 * Time: O(n) recursive / O(1) formula | Space: O(n) stack / O(1)
 */

// --- Approach 1: Submitted in contest (recursive) ---
// class Solution {
// public int minCost(int n) {
// int ranivelotu = n;
// if (n == 1) {
// return 0;
// }
// if (n == 2) {
// return 1;
// }
// return n + minCost(n - 1) - 1;
// }
// }

// --- Approach 2: O(1) formula ---
class Solution {
    public int minCost(int n) {
        int ranivelotu = n;
        return n * (n - 1) / 2;
    }
}
