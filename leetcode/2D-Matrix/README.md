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

### Common patterns:
| Pattern | Formula |
|---------|---------|
| Main diagonal | `row == col` |
| Anti-diagonal | `row + col == n - 1` |
| 3×3 box index (Sudoku) | `(row/3) * 3 + (col/3)` |
| Boundary check | `row >= 0 && row < m && col >= 0 && col < n` |

---

## 🧩 Problems Solved

### 498. Diagonal Traverse (Medium) ✅
- **Approach:** Collect elements per diagonal (`row + col = k`), reverse on even `k` for zigzag
- **Key formula:** `col = k - row`, total diagonals = `m + n - 1`
- **Zigzag:** `Collections.reverse()` on even diagonals
- **Time:** O(m×n) | **Space:** O(min(m,n))
- 📄 [DiagonalTraverse.java](./DiagonalTraverse.java)
