# Two Pointers / Sliding Window

> Problems using two pointers or sliding window technique on arrays and strings.

---

## 🧠 Fundamentals

### Two Pointers:
- **Opposite ends:** `i=0, j=n-1` → move inward (e.g., Two Sum II, Reverse String)
- **Same direction:** `i=0, j=0` → sliding window (e.g., Min Subarray Sum)

### Sliding Window template:
```java
int i = 0;
for (int j = 0; j < n; j++) {
    // expand: add nums[j] to window
    while (condition met) {
        // record result
        // shrink: remove nums[i], i++
    }
}
```

### Key rules:
- `while` (not `if`) for shrinking — finds minimum by shrinking as much as possible
- Start `total = 0`, not `nums[0]` — avoid double-counting
- Window size depends on when `j++` happens:
  - `add then j++` → size = `j - i`
  - `j++ then add` → size = `j - i + 1`

### Subarray vs Subsequence vs Subset:
```
Subarray ⊂ Subsequence ⊂ Subset
contiguous + ordered → ordered + gaps OK → any elements, any order
```

---

## 🧩 Problems Solved

### 167. Two Sum II (Medium) ✅
- **Approach:** Two pointers from both ends, move based on sum vs target
- **Key:** Sorted property guarantees moving right gives smaller sum, left gives larger
- **Time:** O(n) | **Space:** O(1)
- 📄 [TwoSumII.java](./TwoSumII.java)

### 209. Minimum Size Subarray Sum (Medium) ✅
- **Approach:** Sliding window — expand `j`, shrink `i` with `while` (not `if`)
- **Gotcha:** `while` shrinks all the way, `if` only shrinks once (misses smaller windows)
- **Time:** O(n) | **Space:** O(1)
- 📄 [MinSubarraySum.java](./MinSubarraySum.java)
