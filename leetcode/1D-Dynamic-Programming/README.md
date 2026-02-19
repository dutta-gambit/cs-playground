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
