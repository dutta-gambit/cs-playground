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

### 5. Binary Search — IN PROGRESS
- Kicked off 2026-05-30. Strong conceptual + implementation progress in one sitting.
- Understands: linear O(n) → binary O(log n) (each step discards half); **monotonicity** is the real requirement (predicate flips F→T once) — sortedness is just the common special case (firstGreenBall = "first green" boundary)
- **Clean `lowerBound` AND `upperBound` both written** (first index ≥ / > target, return length if none). lowerBound took 3 guided iterations (infinite-loop + nums[-1] crash → correct); upperBound landed first try as the predicted one-token change (`>=` → `>`).
- **LC 35 (Search Insert Position) solved clean.** Used an `== target` early-return (valid for distinct arrays); taught the canonical two-branch `>=` form as the default (leaner + returns *leftmost* under duplicates, which the early-return version doesn't). In upperBound he correctly did NOT carry over the early-return (equality isn't "strictly greater") — shows the lesson landed.
- **LC 34 (First & Last Position) solved by composition — re-typed from memory, fully correct.** Used `bisectLeft`/`bisectRight` (his naming after we mapped lowerBound = bisect_left, upperBound = bisect_right) with the presence check (`start == length || nums[start] != target`, short-circuit order correct → no OOB on empty array) and `bisectRight - 1` for the last index. Internalized: both bisects share `low=0, high=length` and the same `[0, length]` answer range — *only* the `>=` vs `>` comparison differs ("does `==` go left or right"). Both ends of `[0,length]` are reachable by each (lowerBound can return length when target > all; upperBound can return 0 when target < all). **(2026-06-07 revisit)** Re-attempted (InterviewBit "Search for a Range" form) and hit the *flip side* of this very lesson: he pushed the presence check *into* the bisect helpers as `A[low] == B ? low : -1`, which dereferences `A[low]` exactly when `low == length` — and bisect_right of a present last element lands at `length`, so `[1]`/target 1 throws AIOOBE (not a wrong answer — a crash). Fixes: (1) boundary helpers return the **raw** index (`return low`), never dereferencing; (2) presence check lives in ONE place (the caller); (3) the caller's guard must compare index to length BEFORE the array read — `first == length || A[first] != B` — he'd weakened it to `A.length == 0`, which still crashes when target > all (`[1,2,3]`/5: first==3==length≠0 → `A[3]` throws). The robust guard is the exact `start == length || nums[start] != target` pattern he'd written before, then regressed from. Clean solution saved to `leetcode/Binary-Search/SearchRange.java`.
- **Internalized the two-template model:**
  - closed `[low, high]` → `high = length-1` + `while (low <= high)` + `low=mid+1`/`high=mid-1` → exact match
  - half-open `[low, high)` → `high = length` + `while (low < high)` + `high=mid` (keep candidate) / `low=mid+1` → boundary finding (converge to one survivor, `return low`)
- **Core principle owned:** interval must *strictly shrink every iteration* or infinite loop; floor-mid makes `high=mid` safe (mid<high always) but `low=mid` unsafe (needs ceil-mid `(high-low+1)/2`). In half-open, `high` is an *exclusive bound*, never dereferenced — only `nums[mid]` is read, and mid<high always (so `high=length` can't OOB).
- **Reasoning at the invariant level now:** asked "if the interval shrinks either way, why does the assign *direction* matter?" → taught **termination vs correctness** — shrinking only guarantees you stop; correctness needs the loop invariant "the answer always stays inside `[low, high]`." `high=mid` vs `low=mid+1` is a *claim* about which half provably lacks the answer; the comparison is the proof. Wrong direction → still terminates, silently wrong answer (worst kind of bug).
- Understands overflow-safe mid `low+(high-low)/2` and *why* (`low+high` can exceed Integer.MAX_VALUE → wraps negative).
- **Recurring tell (improving):** early on kept defensive special-cases / dead branches (top guards, `low==high ? low : length` ternary); after nudging, dropped them and trusts the invariant. Also re-derives known primitives from scratch under a new problem name (rebuilt lowerBound as `findInsertPosition` and re-hit the `low=mid` trap) — drill **recognize-the-primitive-and-reuse**.
- **Floor problem (largest ≤ x, last occurrence) solved — and pattern-matched to `bisect_right` *unprompted*.** Recognize-and-reuse (the flagged gap) is now showing as a *strength*: he reasoned "needs last occurrence → bisect_right" on his own. Crystallized the predecessor/successor map: **ceil** (smallest ≥ x) = `bisect_left(x)`; **floor** (largest ≤ x) = `bisect_right(x) − 1`; last `< x` = `bisect_left(x) − 1`. (Still slipped in a redundant `arr.length==0` guard — `bisect_right==0 → -1` already covers empty; the defensive-branch tell persists.)
- **LC 278 (First Bad Version) solved — the monotonicity payoff with NO array.** Pure predicate `isBadVersion` (F…F T…T); bisect_left for the first T. Underlying search was flawless; the only bug was a `return low` placed *inside* the loop (ran one iteration then bailed) — fix = move it past the loop. Polish noted: `boolean` not boxed `Boolean`, `if (isMidBad)` not `== true`. This is the bridge from "search a sorted array" → "find the flip in any monotonic predicate."
- **LC 540 (Single Element in Sorted Array) solved first try — asked for intuition only.** Key insight built: binary search over **pairs, not elements**. Snap mid to the even index (pair start); `nums[mid] == nums[mid+1]` ⇒ everything left is cleanly paired ⇒ single is to the right ⇒ `low = mid + 2`; else `high = mid`. The `+2` is because the search *unit* is a 2-index pair. O(log n) vs the O(n) XOR/scan.
- **Almost-sorted array search (findTarget) — SOLVED; the capstone template lesson.** Sorted array with each element displaced by ≤1. His 3-way probe (`mid`, `mid-1`, `mid+1`) + `±2` jump was correct, but he imported the **wrong template ending**: used the boundary-bisect `while (low < high)` + `return low`. Three coupled bugs, all from one root: (1) `return low` → **`return -1`** (this is exact-match — falling out of the loop means *not found*; real answers come only from the in-loop `==` checks); (2) `<` → **`<=`** (else the final lone element never gets its `==` check — pairs inseparably with the `return -1` fix); (3) `high = mid` → **`high = mid - 2`** (mirror of `low = mid + 2`; `mid`/`mid-1` already probed). Crystallized **template-selection-by-question**: *exact match* → closed `[low,high]` + `<=` + `return -1`; *boundary / first-true* → half-open `[low,high)` + `<` + `return low`. His now-grooved bisect reflex (`return low`) bled into a problem that needed exact-match — a good demonstration that the template is chosen by the question, not by habit. **Then applied all three fixes cleanly (`<=`, `high = mid-2`, `return -1`) — solution now correct.**
- **LC 162 (Find Peak Element) solved independently.** Binary search with **no target and no sorted array** — pure slope-monotonicity: compare `nums[mid]` vs `nums[mid+1]` and walk uphill (`nums[mid] > nums[mid+1]` → peak at mid-or-left, `high = mid`; else ascending → `low = mid+1`). `−∞` sentinels at both ends guarantee a peak always exists in `[low, high]`, so `low` converges onto one. O(log n). The day-one firstGreenBall/monotonicity insight in its purest form. Carried the defensive-guard tell again — redundant `mid+1 <= high &&` on *both* branches (always true under floor-mid in a `low<high` loop; and guarding both branches means a false guard would update neither → infinite loop). Clean form is an unguarded `if/else`.
- **Find K Rotation (find-min-in-rotated, LC 153 sibling) solved.** Index of the minimum = rotation count k. Built the **two-lines** model: a rotated sorted array is two increasing runs, the left/high run floating *entirely* above the right/low run, with the min at the foot of the cliff = start of the low run. Anchor on `arr[high]` (always on the low run): `arr[mid] > arr[high]` → on high run, cliff is right → `low = mid+1` (mid provably not the min); `arr[mid] < arr[high]` → on low run, min at-or-left → `high = mid` (**keep** mid). Reframed as **`bisect_left` on the predicate `arr[i] < arr[last]`** (false on high run, true on low run, flips once at the min) — a primitive he already owns. Went deep on two *why*s: (1) the keep-mid/discard-mid asymmetry = bisect_left's signature (a *true* is a candidate for the leftmost-true → keep; a *false* is never the answer → discard); (2) why the min can never sit on the first slope (rotation moves the large *tail* to the front, so slope 1 is the big values by construction, and a pure climb has no valley). No special case for k=0 — invariant handles it (resisted the guard reflex this time). Saved to `leetcode/Binary-Search/FindKRotation.java`.
- **LC 33 (Search in Rotated Sorted Array) & LC 81 (with duplicates) — SOLVED.** The capstone application of **"one half is always sorted"**: a rotated array has one cliff, so at any `mid` it splits into one CLEAN (cliff-free, fully sorted) half and one half still hiding the cliff; you can only range-check the clean half, because only there do you hold BOTH endpoints to bound the target. Cliff locator anchored on `nums[high]` (same anchor as find-min): `nums[mid] <= nums[high]` → right half `[mid..high]` clean; `nums[mid] > nums[high]` → left half `[low..mid]` clean. Proved rigorously: a *strict* inequality can't straddle the cliff because every high-run element ≥ every low-run element, so `nums[mid] > nums[high]` forces the cliff to mid's right ⇒ `[low..mid]` sorted (inclusive). Two recurring tells resurfaced and got fixed: (1) **template confusion AGAIN** — imported the convergence ending (`while(low<high)` + `return low`, `==` check commented out) into an exact-match problem; fix = the closed-interval package (`<=`, live `if(nums[mid]==target) return mid`, `mid±1`, `return -1`). (2) **single-endpoint range check** — kept comparing target to just `nums[high]` (e.g. `target < nums[high]`), which can't localize a target in a rotated array; the four-branch logic collapsed to inert (the mid-vs-high half did nothing) → infinite loop on `[4,5,6,7,0,1,2]`/target 5. Fix = bound target inside the PROVABLY sorted half with both ends. **LC 81 duplicates** change exactly one thing: the tie `nums[mid] == nums[high]` becomes ambiguous (`[1,1,1,0,1]` — equal endpoints with a cliff between), so peel it with `high--` (safe: that endpoint `== nums[mid] != target`) → degrades O(log n) → **O(n)** worst case (all-equal adversary). The strict `<`/`>` branches are untouched, so one method serves both 33 and 81. Saved to `leetcode/Binary-Search/SearchInRotatedSortedArray.java` and `SearchInRotatedSortedArrayII.java`.
- **Gaps:** search-on-answer (Koko 875 / ship-packages 1011), 2D matrix (74).
- **Status:** Boundary-search toolkit complete and applied — `bisectLeft`/`bisectRight` + LC 35 + LC 34 + floor/ceil + countFreq, all clean. Then pushed past pure arrays: **LC 278** (bisect on a bare predicate — the monotonicity payoff), **LC 540** (binary search over pairs), and **LC 162** (peak — binary search with no target, pure slope-monotonicity). Capstone lesson landed via the almost-sorted variant: **the template is chosen by the question** (exact-match closed-interval `+ return -1` vs boundary half-open `+ return low`) — his bisect `return low` reflex bled into an exact-match problem. Then the first rotated-array rep — **Find K Rotation / find-min-in-rotated (LC 153)** — solved via the two-lines model (reframed as `bisect_left` on `arr[i] < arr[last]`). Then both rotated-array target searches — **LC 33** (one half is always sorted; range-check the clean half against BOTH its endpoints) and **LC 81** (duplicates make the `nums[mid]==nums[high]` tie ambiguous → `high--`, O(n) worst case). Next: **search-on-answer** (Koko 875 / ship 1011) — closes the loop back to day-one monotonicity.

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
- Inorder Traversal (94) — done recursive + iterative (dive-left explicit stack)
- Preorder Traversal (144) — done recursive + iterative (process-on-pop, push both children, LIFO order)
- Postorder Traversal (145) — done recursive + iterative BOTH ways (reverse-trick + principled `prev`-pointer single pass)
- Level-Order / BFS (102) — pattern worked through (size-snapshot queue); not yet committed as a solution file
- Understands: inorder/preorder/postorder are DFS; recursion rides the implicit call stack (frames push/pop, space O(h))
- **Iterative DFS mental model SOLID** — the "wall" (position of `result.add`) = # recursive calls before processing = # resume points = conversion difficulty: preorder 0 (process-on-pop), inorder 1 (dive-left), postorder 2 (peek + `prev`)
- Internalized: left subtree = prerequisite (node waits → push), right = follow-up (clean handoff → move `curr`); LIFO push-order reasoning
- Understands auxiliary vs output space; preorder skew loophole (O(1) on pure left-skew) vs inorder/postorder genuinely O(h)
- Understands BFS = FIFO queue, level-by-level; three impl variants (size snapshot / null sentinel / recursive DFS-by-depth)
- Java depth: `ArrayDeque` vs `LinkedList` (array vs nodes, cache/GC, null handling), `Deque` vs `Queue` (narrowest interface), `ArrayDeque` as default container for both stack and queue
- **Trap hit & understood:** applied inorder dive-mechanics to the postorder reverse-trick → got inorder out; learned the trick rides on PREORDER mechanics
- **Gaps:** BST operations, tree construction, height/diameter, path sum, LCA, validate BST, serialize/deserialize, and the broader tree problem patterns
- **Status:** Traversal foundation rock-solid — all 3 DFS traversals recursive + iterative with full intuition, BFS understood. Notes in leetcode/Binary-Tree/README.md. Next: tree problem patterns (height/diameter via postorder aggregation, validate-BST via inorder).

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
- **2026-05-27:** Started Trees. All three traversals (94/144/145) done **recursive and iterative**. Built the full iterative-DFS mental model — the "wall"/resume-points framework (preorder 0, inorder 1, postorder 2), dive-left as prerequisite-waiting, LIFO push ordering. Postorder both ways: reverse-trick (after hitting the classic inorder-mechanics trap) and the principled `prev`-pointer single pass. Also covered BFS/level-order (size-snapshot + variants), container choice (`ArrayDeque` vs `LinkedList`, `Deque` vs `Queue`), and auxiliary-vs-output space. Notes fully written up in Binary-Tree/README.md.
- **2026-05-30:** Big Binary Search session — NOT STARTED → complete boundary-search toolkit. Sharpened day-one intuition into **monotonicity** (predicate flips F→T once; sortedness is just the common case). Built `lowerBound`/`bisectLeft` from scratch (3 iterations: infinite loop + `nums[-1]` crash → clean) and `upperBound`/`bisectRight` (one-token mirror, first try). Internalized: closed `[l,r]`+`<=`+`mid±1` vs half-open `[l,r)`+`<`+`high=mid`; **termination vs correctness** and the loop invariant ("the answer must stay inside the window"); overflow-safe mid and *why*; `high=length` is an exclusive bound, never dereferenced; both bisects share the `[0,length]` answer range, only `>=`/`>` differs. Solved **LC 35** (clean) and **LC 34** (composition, re-typed from memory). Mapped to Python `bisect_left`/`bisect_right`. Recurring tells: keeps defensive dead branches until nudged; re-derives known primitives under new problem names (rebuilt lowerBound as `findInsertPosition`, re-hit the `low=mid` trap) → drill recognize-and-reuse. Was reasoning at the invariant level by the end. Next: rotated array (33), then search-on-answer.
- **2026-06-01:** Binary Search, part 2 — past pure arrays into predicates and variants. **LC 278 (First Bad Version)**: bisect_left on a bare monotonic predicate — the "no array, just a F→T flip" payoff; only bug was a `return low` stranded *inside* the loop. **LC 540 (Single Element)**: solved first try, built the intuition — binary search over *pairs* (even-index snap, `+2` because the unit is a 2-index pair). **Almost-sorted search (findTarget)**: 3-way probe + `±2` jump were right, but he imported the boundary-bisect ending (`while(low<high)` + `return low`) into an exact-match problem → 3 coupled bugs (`return low`→`-1`, `<`→`<=`, `high=mid`→`high=mid-2`). Applied all three fixes and **solved it**. Crystallized **template-selection-by-question** (exact-match closed-interval+`return -1` vs boundary half-open+`return low`) — the capstone insight of the topic. **LC 162 (Find Peak Element)**: solved *independently* — binary search with no target / no sorted array, pure slope-monotonicity (walk toward the higher neighbor; `−∞` boundaries guarantee a peak in range). Recurring defensive-guard tell resurfaced (redundant `mid+1<=high` on both branches). **Find K Rotation (find-min-in-rotated, LC 153 sibling)**: solved — the two-lines model (left run floats above right run; min = foot of the cliff = start of the low run), anchored on `arr[high]`, reframed as `bisect_left` on `arr[i] < arr[last]`. Deep-dived the keep-mid-vs-discard-mid asymmetry (a *true* is a leftmost-true candidate → keep; a *false* is never the answer → discard) and why the min can never lie on the first slope (rotation moves the large tail to the front; a slope that only climbs has no valley). Solution saved to `leetcode/Binary-Search/FindKRotation.java`. Next: **LC 33** (search a target in a rotated array).
- **2026-06-03:** Binary Search, part 3 — closed out the rotated-array target searches. **LC 33 (Search in Rotated Sorted Array)**: the "one half is always sorted" application. Two recurring tells resurfaced and were fixed — (1) template confusion *again* (imported the convergence ending `while(low<high)`/`return low` with the `==` check commented out into an exact-match problem → fix = closed-interval package + `return -1`); (2) single-endpoint range-checking (comparing target only to `nums[high]`), which made the four branches collapse to inert and infinite-loop on `[4,5,6,7,0,1,2]`/target 5 → fix = bound target inside the provably-sorted half with BOTH endpoints. Proved the cliff-locator rigorously (a strict `nums[mid] > nums[high]` guarantees `[low..mid]` clean, since every high-run element ≥ every low-run element). Also unified the two templates from first principles: the `+1`/`-1` vs bare-`mid` and `<=` vs `<` choices all reduce to one question — *after testing mid, is it still a candidate?* (eliminated → step over it; candidate → keep it). **LC 81 (duplicates)**: isolated that duplicates change exactly one thing — the `nums[mid] == nums[high]` tie is ambiguous (`[1,1,1,0,1]`), peel it with `high--` (safe: tie endpoint `!= target`), degrading to O(n) worst case; strict branches unchanged, so one method serves both. Saved `SearchInRotatedSortedArray.java` + `SearchInRotatedSortedArrayII.java`. Next: **search-on-answer** (Koko 875 / ship-packages 1011), then 2D matrix (74).
- **2026-06-07:** Quick LC 34 revisit (InterviewBit "Search for a Range"). Binary search itself was flawless — the bug was a **boundary out-of-bounds crash, not a logic error**: he moved the presence check into the bisect helpers as `A[low] == B ? low : -1`, which dereferences `A[low]` when `low == length`. bisect_right of a present last element legitimately lands at `length`, so `[1]`/1 throws AIOOBE; `return low` "fixed" it only because it stops touching the array. Real lesson reinforced: a boundary helper's one job is to return the index in `[0, length]` and NEVER dereference it; the presence check belongs in exactly one place (the caller), guarded as `first == length || A[first] != B` (compare index to length before the read). Also flagged the same latent crash in his caller guard (`A.length == 0` misses target>all, e.g. `[1,2,3]`/5). Saved `leetcode/Binary-Search/SearchRange.java`. Still next: **search-on-answer** (Koko 875 / ship-packages 1011), then 2D matrix (74).
