# 2D Matrix

> Problems involving traversal, transformation, or pattern recognition on 2D arrays/matrices.

---

## 🧠 2D Matrix Fundamentals

### Dimensions:
```java
int m = mat.length;      // number of ROWS
int n = mat[0].length;   // number of COLUMNS
```

### Diagonal properties:
- Elements on diagonal `k` satisfy: `row + col = k`
- Total diagonals = `m + n - 1`
- Given `row`, compute `col = k - row` (eliminates inner loop)
- Bounds check: `col >= 0 && col < n`

### Spiral traversal (4 boundaries):
```java
int top = 0, bottom = m-1, left = 0, right = n-1;
// → right along top, top++
// ↓ down along right, right--
// ← left along bottom (if top<=bottom), bottom--
// ↑ up along left (if left<=right), left++
```
- Never use boundaries as loop counters — use separate `i`/`j`
- `if` guards before ← and ↑ prevent double-counting on single row/col

---

## 🧩 Problems Solved

### 498. Diagonal Traverse (Medium) ✅
- **Approach:** Collect elements per diagonal (`row + col = k`), reverse on even `k` for zigzag
- **Key formula:** `col = k - row`, total diagonals = `m + n - 1`
- **Zigzag:** `Collections.reverse()` on even diagonals
- **Time:** O(m×n) | **Space:** O(min(m,n))
- 📄 [DiagonalTraverse.java](./DiagonalTraverse.java)

### 54. Spiral Matrix (Medium) ✅
- **Approach:** 4 boundaries (`top/bottom/left/right`) shrinking inward → ↓ ← ↑
- **Key insight:** Fix one dimension, traverse the other, then shrink boundary by 1
- **Gotcha:** `if` guards before ← and ↑ to prevent double-counting
- **Time:** O(m×n) | **Space:** O(1)
- 📄 [SpiralMatrix.java](./SpiralMatrix.java)

### 118. Pascal's Triangle (Easy) ✅
- **Approach:** Build row by row. Edges = 1, middle = `result[i-1][j-1] + result[i-1][j]`
- **Gotcha:** Create each row with `new ArrayList<>()` + use `add()`, not `set()`
- **Time:** O(n²) | **Space:** O(n²)
- 📄 [PascalsTriangle.java](./PascalsTriangle.java)
