# DSA Prep Progress — NeetCode 150 Track

## Target Companies
Meta, Google, Apple, Intuit, Adobe, InMobi, OpenAI, Anthropic

---

## Topic Progress

### 1. Arrays & Hashing — SOLID
- ~27 problems solved (easy through hard)
- Hash maps, frequency counting, grouping, set operations — all instinctive
- Design problems done (HashSet, HashMap)
- Negation trick for [1,n] range problems — comfortable
- **Status:** Core complete. Can revisit for hard variants if needed.

### 2. Two Pointers — IN PROGRESS
- 7 problems solved, mostly easy
- Opposite-ends approach done (TwoSumII, Squares of Sorted Array)
- Same-direction pointer done (Remove Element, Move Zeroes)
- **Gaps:** 3Sum, Container With Most Water, Trapping Rain Water not yet attempted
- **Status:** Fundamentals there, needs medium-hard depth.

### 3. Sliding Window — IN PROGRESS
- MinSubarraySum (209) done, MaxConsecutiveOnesII (487) done
- **Gaps:** Variable-length window problems (Longest Substring Without Repeating, Minimum Window Substring, etc.)
- **Status:** Basic fixed/variable window understood, needs more practice.

### 4. Stack — JUST STARTED
- 1 problem (MergeAdjacentEqualElements)
- **Gaps:** Valid Parentheses, Min Stack, Daily Temperatures, monotonic stack patterns
- **Status:** Needs full coverage.

### 5. Binary Search — NOT STARTED
- **Status:** Topic not yet attempted.

### 6. Linked List — IN PROGRESS
- Reverse Linked List (206) — done, iterative three-pointer approach solid
- Design Linked List (707) — done, both without and with dummy/sentinel node
- Linked List Cycle (141) — done, Floyd's Phase 1 (slow/fast detection) solid
- Linked List Cycle II (142) — done, HashMap approach + Floyd's Phase 2 (O(1) space)
- Intersection of Two Lists (160) — HashMap approach done, O(1) space approach in progress
- Understands: node structure, traversal, insertion, deletion, head edge cases, sentinel nodes
- Learned prev/curr/next march pattern for reversal
- Learned slow/fast pointer pattern for cycle detection
- **Needs revisit:** Floyd's Phase 2 proof (why F = C-a works) — not yet fully confident
- **Gaps:** Intersection O(1) space, merge sorted lists, DLL, Circular LL, LRU Cache
- **Status:** SLL fundamentals and two-pointer technique solid. Working through LeetCode Explore card.
- **Current track:** SLL → Two Pointers → Classic Problems → DLL → Circular LL → LRU Cache

### 7. Trees — IN PROGRESS
- Inorder Traversal (94) — done, recursive helper carrying the result list
- Preorder Traversal (144) — done, recursive helper
- Postorder Traversal (145) — done, recursive helper
- Level-Order / BFS (102) — pattern worked through (size-snapshot queue); not yet committed as a solution file
- Understands: inorder/preorder/postorder are DFS; recursion rides the implicit call stack (frames push/pop, space O(h))
- Learned iterative DFS — converting recursion to an explicit `Deque` stack (iterative inorder)
- Understands BFS = FIFO queue, level-by-level; three impl variants (size snapshot / null sentinel / recursive DFS-by-depth)
- Java depth: `ArrayDeque` vs `LinkedList` (array vs nodes, cache/GC, null handling), `Deque` vs `Queue` (narrowest interface), `ArrayDeque` as default container for both stack and queue
- **Gaps:** BST operations, tree construction, height/diameter, path sum, LCA, validate BST, serialize/deserialize, and the broader tree problem patterns
- **Status:** Traversal foundation solid (DFS recursive + iterative, BFS). Notes captured in leetcode/Binary-Tree/README.md. Ready to move into tree problem-solving patterns.

### 8. Tries — NOT STARTED
- **Status:** Topic not yet attempted.

### 9. Heap / Priority Queue — NOT STARTED
- TopKFrequentElements uses heap but filed under Arrays.
- **Status:** Topic not yet attempted as dedicated study.

### 10. Backtracking — NOT STARTED
- **Status:** Topic not yet attempted.

### 11. Graphs — NOT STARTED
- **Status:** Topic not yet attempted.

### 12. Dynamic Programming — JUST STARTED
- ClimbingStairs (70), MinCostSplitIntoOnes done
- **Gaps:** House Robber, Coin Change, LIS, and all core DP patterns
- **Status:** Very early. Needs structured buildout.

### 13. Greedy — NOT STARTED
### 14. Intervals — NOT STARTED
### 15. Math & Geometry — NOT STARTED
### 16. Bit Manipulation — NOT STARTED

---

## Strengths
- Strong hash map instincts
- Clean Java code, good variable naming
- Organized study approach with notes and pattern recognition
- Does weekly LC contests — good competitive practice

## Areas to Build
- Two Pointers depth (medium-hard)
- Sliding Window (variable-length variants)
- Stack (monotonic stack especially — Meta/Google love this)
- Binary Search (critical for all target companies)
- Trees and Graphs (massive chunk of interview questions)
- DP (the big boss — needs structured, patient buildout)

---

## Session Log
- **2026-03-10:** Initial assessment. Arrays & Hashing strong. Starting to deepen Two Pointers and Sliding Window.
- **2026-03-14:** Started Linked List topic. Covered SLL fundamentals — reversal (206), Design LinkedList (707) with dummy node, cycle detection (141), cycle start (142) with both HashMap and Floyd's, started intersection (160). Floyd's Phase 2 proof needs revisit. Following LeetCode Explore card.
- **2026-05-27:** Started Trees. Inorder/preorder/postorder (94/144/145) done recursively. Connected DFS → implicit call stack → explicit `Deque` (iterative inorder). Covered BFS/level-order with the size-snapshot queue pattern + variants. Deep-dived container choice: `ArrayDeque` vs `LinkedList`, `Deque` vs `Queue`. Notes written up in Binary-Tree/README.md.
