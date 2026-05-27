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

Because recursion is *just* an implicit stack, any recursive DFS can be rewritten with an **explicit `Deque` used as a stack**. Meta/Google sometimes ask for this directly ("now do it without recursion"). Going iterative means **you** store the nodes and **you** rebuild the bookkeeping the runtime did for free.

### The unifying idea: the "wall"

A call-stack frame holds two things — the **node** and a **resume point** (the line to continue at after a child call returns). The position of `result.add(node.val)` — call it **the wall** — decides how many recursive calls happen *before* the node is processed, and that count is exactly how much state you must rebuild by hand:

```java
traverse(node.left);     // is this before the wall?
result.add(node.val);    // ← THE WALL (node is processed right here)
traverse(node.right);    // ...or after it?
```

| Traversal | Wall position | Calls *before* wall | Resume points | Iterative shape |
|-----------|---------------|---------------------|---------------|-----------------|
| **Preorder**  | top    | 0        | 0 | process-on-pop, push both children |
| **Inorder**   | middle | 1 (left) | 1 | dive-left → process-on-pop → go right |
| **Postorder** | bottom | 2 (both) | 2 | dive-left + `prev` pointer (or reverse-trick) |

A node only **waits on the stack** for the calls that come *before* the wall. Preorder processes on arrival (nothing waits); inorder waits for its left subtree; postorder waits for both → hardest.

> **Why dive *left* and not right?** Left subtree = a **prerequisite** (must finish before the node) → the node is blocked → push it and remember it. Right subtree = a **follow-up** (runs after the node is already done) → clean handoff → no waiting, just move `curr` to it.

### Preorder — 0 resume points

Process the instant you pop. Push children **right-then-left** so left pops first (LIFO).

```java
public List<Integer> preorder(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;            // guard: root is pushed before the loop
    Deque<TreeNode> stack = new ArrayDeque<>();
    stack.push(root);
    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);                              // process on arrival
        if (node.right != null) stack.push(node.right);    // push RIGHT first...
        if (node.left  != null) stack.push(node.left);     // ...so LEFT pops first
    }
    return result;
}
```

No dive, no extra state. *Quirk:* a purely **left-skewed** tree uses only **O(1)** stack here (no right children ever pile up) — so preorder's `O(h)` is a loose upper bound.

### Inorder — 1 resume point

Can't process on arrival (the left subtree goes first). Dive left parking nodes, pop-and-process when the left bottoms out, then hand off right.

```java
public List<Integer> inorder(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Deque<TreeNode> stack = new ArrayDeque<>();
    TreeNode curr = root;
    while (curr != null || !stack.isEmpty()) {
        while (curr != null) {     // dive left, parking nodes that must wait
            stack.push(curr);
            curr = curr.left;
        }
        curr = stack.pop();        // left subtree done → this node is next
        result.add(curr.val);      // process (the wall)
        curr = curr.right;         // follow-up: hand off to the right
    }
    return result;
}
```

No `if (root == null)` guard needed — `curr = root` + the loop condition absorb it. Space is **genuinely O(h)** (the dive parks the whole left spine — no skew loophole).

### Postorder — 2 resume points (the hard one)

The node waits for **both** children, so it's visited twice: back-from-left (go right), then back-from-right (process). Two ways:

**A. Reverse trick (easiest to remember).** `L→R→Root` is the reverse of `Root→R→L`, which is just preorder mechanics with children pushed **left-then-right**. Run that, then reverse.

```java
public List<Integer> postorder(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;
    Deque<TreeNode> stack = new ArrayDeque<>();
    stack.push(root);
    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);                              // builds Root → Right → Left
        if (node.left  != null) stack.push(node.left);     // push LEFT first...
        if (node.right != null) stack.push(node.right);    // ...so RIGHT pops next
    }
    Collections.reverse(result);                           // → Left → Right → Root
    return result;
}
```

> ⚠️ **Trap:** this is **preorder** mechanics (process-on-pop, push both), NOT the inorder dive. Mirror the inorder dive (dive *right*, process-on-pop, go *left*) and you get `Right→Root→Left`; reversing *that* lands back on **inorder**, not postorder.

**B. `prev` pointer (single pass, no reverse).** Use when asked "without the reverse." Dive left, but **peek** instead of pop. `prev` = last processed node; since a subtree's root is processed last, `prev == node.right` means "right subtree just finished → process me now."

```java
public List<Integer> postorder(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Deque<TreeNode> stack = new ArrayDeque<>();
    TreeNode curr = root, prev = null;
    while (curr != null || !stack.isEmpty()) {
        while (curr != null) {                 // dive left (same as inorder)
            stack.push(curr);
            curr = curr.left;
        }
        TreeNode peek = stack.peek();          // PEEK, don't pop
        if (peek.right != null && prev != peek.right) {
            curr = peek.right;                 // right subtree not done → go right
        } else {
            result.add(peek.val);              // no right child / right done → process
            prev = stack.pop();
        }
    }
    return result;
}
```

Each node is peeked at most twice → still **O(n)** time, **O(h)** space.

### Recursive vs. iterative — same cost, different visibility

| Style | Stack | Time | Space |
|-------|-------|------|-------|
| Recursive DFS | **implicit** — JVM call stack manages it | O(n) | O(h) |
| Iterative DFS | **explicit** — you manage a `Deque` yourself | O(n) | O(h) |

> **Mental model:** `DFS = stack` (implicit via recursion, or explicit `Deque`). Going iterative doesn't *save* memory — it moves the `O(h)` from the call stack onto the heap, where it's yours to control. The same idea returns in graph DFS and iterative tree problems.

---

## ⏱️ Complexity (all three traversals)

| | Recursive | Iterative (explicit stack) |
|-|-----------|----------------------------|
| Time  | O(n) | O(n) |
| Space | O(h) call stack | O(h) explicit stack |

`h` = tree height. Balanced tree → `O(log n)`. Skewed tree → `O(n)`.

**Auxiliary vs. output:** the `result` list is always `O(n)` — but that's the *required output*, usually not counted. The **auxiliary** space (scratch the algorithm spends to compute the answer) is the stack: **O(h)**. ("In-place" = O(1) auxiliary.) Per-traversal nuance: preorder's `O(h)` is loose — a purely left-skewed tree is `O(1)` — whereas inorder/postorder genuinely hit `O(h)` because the dive parks the whole left spine.

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

> All three traversals below are also implemented **iteratively** (explicit stack) — see the *Iterative DFS* section above for the code and the wall / resume-point reasoning.

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
