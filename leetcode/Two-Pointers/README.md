# Two Pointers

> Problems where two pointers traverse an array/string to find pairs, partition, or compact elements.

---

## 🧠 Two Pointer Patterns

| Pattern | How | Example |
|---------|-----|---------|
| **Opposite ends** | `left=0, right=n-1`, move inward | Valid Palindrome, Two Sum II, Container With Most Water |
| **Reader-Writer** | Reader scans, writer tracks write position | Remove Duplicates (in-place compaction) |
| **Fast-Slow** | Fast moves 2x speed of slow | Linked list cycle detection |

### Reader-Writer pattern (in-place):
```java
int writer = 1;
for (int reader = 1; reader < n; reader++) {
    if (shouldKeep(nums[reader])) {
        nums[writer++] = nums[reader];
    }
}
return writer;  // count of kept elements
```

---

## 🧩 Problems Solved

### 26. Remove Duplicates from Sorted Array (Easy) ✅
- **Pattern:** Reader-Writer two pointers (in-place compaction)
- **Approach:** Reader `i` scans ahead, writer `j` tracks write position. Skip duplicates, write uniques
- **Time:** O(n) | **Space:** O(1)
- 📄 [RemoveDuplicatesSortedArray.java](./RemoveDuplicatesSortedArray.java)
