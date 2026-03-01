# 1-D Dynamic Programming

> Problems where the solution builds on smaller subproblems (optimal substructure + overlapping subproblems).

---

## 🧠 DP Fundamentals

### Two approaches:

| Approach | Direction | How |
|----------|-----------|-----|
| **Top-down (Memoization)** | Start from `f(n)`, recurse down | Recursion + cache (memo array) |
| **Bottom-up (Tabulation)** | Start from `f(0)`, build up | Iterative loop |

### Common memoization bugs:
1. **Calling wrong function** — must recursively call the function that reads the SAME memo
2. **Returning recomputed value** — store in `memo[n]`, then `return memo[n]` (not recompute)
3. **Creating new memo per call** — memo must be shared across all recursive calls

---

## 🧩 Problems Solved

### 70. Climbing Stairs (Easy) ✅
- **Pattern:** Fibonacci — `f(n) = f(n-1) + f(n-2)`
- **Approach 1:** Recursion + memoization (top-down) → O(n) time, O(n) space
- **Approach 2:** Bottom-up with two variables → O(n) time, O(1) space
- **Key insight:** "How many ways" with 1-or-2 choices = Fibonacci in disguise
- 📄 [ClimbingStairs.java](./ClimbingStairs.java)

### Minimum Cost to Split into Ones (Medium) ✅ — Weekly Contest
- **Pattern:** Triangular numbers — `f(n) = n*(n-1)/2`
- **Approach 1 (Submitted):** Recursion — `minCost(n) = (n-1) + minCost(n-1)`, always split as `(1, n-1)`
- **Approach 2 (O(1)):** Direct formula `n*(n-1)/2`
- **Penalty:** Returned 1 for `n=1` instead of 0 (no split needed = 0 cost)
- **Key insight:** Spotted pattern by computing `n=1..7` by hand, recognized triangular numbers
- 📄 [MinCostSplitIntoOnes.java](./MinCostSplitIntoOnes.java)
