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

## 🧠 DFS & the Call Stack (the "Implicit Stack")

All three traversals above are **DFS** — they dive as deep as possible down one path before backtracking. (BFS, further down, goes level-by-level instead.) You never manage a stack yourself because the **JVM call stack does it for you** — that is the **implicit stack**.

Each recursive call pushes a *frame* (the current node + the line to resume at) onto the call stack. When the call returns, its frame is popped and execution resumes exactly where it left off — that is what makes backtracking automatic.

Tracing **inorder** (`L → Root → R`) on the tree above visits `4 → 2 → 5 → 1 → 3`. At the deepest point — processing node `4` — the live call stack is:

```
┌─────────────────┐  ← top (currently executing)
│  inorder(4)     │
├─────────────────┤
│  inorder(2)     │
├─────────────────┤
│  inorder(1)     │  ← bottom (first call)
└─────────────────┘
```

The stack only ever holds **one root-to-current path**, whose length is the height `h`. That is exactly why traversal space is **O(h)**.

---

## 🪜 Iterative DFS — Making the Stack Explicit

Because recursion is *just* an implicit stack, any recursive DFS can be rewritten with an **explicit `Deque` used as a stack**. Meta/Google sometimes ask for this directly ("now do it without recursion").

```java
public List<Integer> inorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Deque<TreeNode> stack = new ArrayDeque<>();
    TreeNode curr = root;

    while (curr != null || !stack.isEmpty()) {
        while (curr != null) {     // dive left, pushing every node on the way
            stack.push(curr);
            curr = curr.left;
        }
        curr = stack.pop();        // backtrack to nearest unprocessed node
        result.add(curr.val);      // process
        curr = curr.right;         // then explore its right subtree
    }
    return result;
}
```

You are manually doing what the call stack did silently: push while going left, pop-and-process when you can't go further.

| Style | Stack | Time | Space |
|-------|-------|------|-------|
| Recursive DFS | **implicit** — JVM call stack manages it | O(n) | O(h) |
| Iterative DFS | **explicit** — you manage a `Deque` yourself | O(n) | O(h) |

> **Mental model:** `DFS = stack` (implicit via recursion, or explicit `Deque`). This same idea returns in graph DFS and iterative tree problems.

---

## ⏱️ Complexity (all three traversals)

| | Recursive | Iterative (explicit stack) |
|-|-----------|----------------------------|
| Time  | O(n) | O(n) |
| Space | O(h) call stack | O(h) explicit stack |

`h` = tree height. Balanced tree → `O(log n)`. Skewed tree → `O(n)`.

---

## 🌊 BFS / Level-Order Traversal

DFS uses a **stack** (LIFO); BFS uses a **queue** (FIFO) and visits the tree **level by level**. There is no natural recursive BFS — the queue is always explicit.

```java
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.add(root);

    while (!queue.isEmpty()) {
        int size = queue.size();              // snapshot: # nodes on THIS level
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            level.add(node.val);
            if (node.left != null)  queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
        result.add(level);
    }
    return result;
}
```

**The key trick:** snapshot `queue.size()` *before* the inner loop, so you process exactly the nodes on the current level even as you enqueue the next level's children behind them.

### Three ways to implement it

| Way | Idea | Verdict |
|-----|------|---------|
| **Size snapshot** (above) | Process `queue.size()` nodes per level | ✅ Default — cleanest |
| **Null sentinel** | Push `null` between levels as a delimiter | ⚠️ Needs `null` *in* the queue → **breaks `ArrayDeque`** (NPE), forcing `LinkedList` |
| **Recursive DFS by depth** | DFS carrying a `depth`, appending into `result.get(depth)` | 🔥 Produces level-order *output* using the implicit stack — no queue at all |

```java
// Recursive DFS-by-depth — same output as BFS, different mechanics
void dfs(TreeNode node, int depth, List<List<Integer>> result) {
    if (node == null) return;
    if (depth == result.size()) result.add(new ArrayList<>()); // first node at this depth
    result.get(depth).add(node.val);
    dfs(node.left,  depth + 1, result);
    dfs(node.right, depth + 1, result);
}
```

**Time O(n), Space O(n)** — the queue holds up to ~n/2 nodes (the widest level) in a balanced tree.

---

## 🧱 Choosing the Container — `ArrayDeque` vs `LinkedList`, `Deque` vs `Queue`

### `ArrayDeque` vs `LinkedList` (the *implementation*)

`ArrayDeque` is the right default for **both** stacks and queues:

| | `ArrayDeque` | `LinkedList` |
|-|--------------|--------------|
| Backing | Resizable circular **array** | Doubly-linked **nodes** |
| Per element | One reference slot | A `Node` object (+2 pointers, ~24B overhead) |
| Cache locality | Excellent (contiguous) | Poor (pointer chasing) |
| GC pressure | Low | High (node alloc per op) |
| enqueue/dequeue | Amortized O(1) | O(1), but with alloc overhead |
| Allows `null`? | **No** — throws NPE | Yes |

`LinkedList` only wins when you genuinely need `null` elements (e.g. the null-sentinel BFS above). Otherwise `ArrayDeque` is faster *and* lighter — the JavaDoc itself says it beats `LinkedList` as a queue.

### `Deque` vs `Queue` (the *declared type*)

Same object (`new ArrayDeque<>()`), **zero runtime difference** — only the compile-time API surface changes:

```java
Queue<TreeNode> q  = new ArrayDeque<>();  // FIFO only: offer/poll/peek — signals intent, prevents misuse
Deque<TreeNode> dq = new ArrayDeque<>();  // both ends: + push/pop, offerFirst/pollLast (stack ops, zigzag)
```

**Rule: declare the narrowest interface that does the job.** Plain BFS → `Queue`. Need front-end ops (iterative DFS stack, zigzag level-order) → `Deque`.

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
