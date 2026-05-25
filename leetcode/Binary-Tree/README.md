# Binary Tree

> Problems where the core pattern involves traversing or transforming a binary tree.

---

## 🌳 The `TreeNode` Contract

LeetCode tree problems use this canonical definition — assume it exists; do not redeclare it in the solution file:

```java
public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
```

---

## 🔁 The Three DFS Traversals

All three visit each node exactly once — they differ only in **when** the current node's value is appended relative to its children's recursion.

```
        1
       / \
      2   3
     / \
    4   5
```

| Traversal | Order        | Output for tree above |
|-----------|--------------|-----------------------|
| Preorder  | Root → L → R | `1, 2, 4, 5, 3`        |
| Inorder   | L → Root → R | `4, 2, 5, 1, 3`        |
| Postorder | L → R → Root | `4, 5, 2, 3, 1`        |

### When to reach for which

| Need | Use |
|------|-----|
| Serialize / clone a tree (parent before children) | **Preorder** |
| Visit nodes of a BST in sorted order | **Inorder** |
| Bottom-up aggregation (sum, height, diameter, delete) | **Postorder** |
| Level-by-level work (shortest path, level sums) | BFS / level-order (Queue) |

---

## 🧰 The Helper Pattern

All three solutions use the same shape:

```java
public List<Integer> traversal(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    impl(root, result);
    return result;
}

private void impl(TreeNode root, List<Integer> result) {
    if (root == null) return;
    // ...recurse left / add / recurse right — order depends on traversal
}
```

### ⚠️ Common Bug — Recursing into the Public Method

```java
// ❌ WRONG — calls the public method, which creates a NEW list and throws it away
private void impl(TreeNode root, List<Integer> result) {
    if (root == null) return;
    inorderTraversal(root.left);     // returns a list, ignored
    result.add(root.val);
    inorderTraversal(root.right);    // returns a list, ignored
}
```

The helper MUST recurse into **itself** and pass `result` along — otherwise only the root's value ends up in the output.

```java
// ✅ CORRECT
impl(root.left, result);
result.add(root.val);
impl(root.right, result);
```

---

## ⏱️ Complexity (all three traversals)

| | Recursive | Iterative (explicit stack) |
|-|-----------|----------------------------|
| Time  | O(n) | O(n) |
| Space | O(h) call stack | O(h) explicit stack |

`h` = tree height. Balanced tree → `O(log n)`. Skewed tree → `O(n)`.

---

## 🧩 Problems Solved

### 94. Binary Tree Inorder Traversal (Easy) ✅
- **Order:** Left → Root → Right
- **Approach:** Recursive helper carrying the result list
- **Note:** On a BST, inorder yields values in ascending sorted order
- 📄 [InorderTraversal.java](./InorderTraversal.java)

### 144. Binary Tree Preorder Traversal (Easy) ✅
- **Order:** Root → Left → Right
- **Approach:** Recursive helper carrying the result list
- **Note:** Natural for serialization — parent emitted before children
- 📄 [PreorderTraversal.java](./PreorderTraversal.java)

### 145. Binary Tree Postorder Traversal (Easy) ✅
- **Order:** Left → Right → Root
- **Approach:** Recursive helper carrying the result list
- **Note:** Natural for bottom-up aggregation (heights, sums, deletes)
- 📄 [PostorderTraversal.java](./PostorderTraversal.java)
