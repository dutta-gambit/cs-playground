# Arrays & Hashing — Study Notes (Java)

> 📅 Started: 2026-02-12 (Day 1)
> 🎯 Goal: NeetCode 150 — 9 problems this week
> 🧪 Language: Java

---

## 📦 Foundation: Array vs ArrayList vs List in Java

### The History — Why Do We Need 3 Things?

#### 1. `int[]` / `String[]` — The Raw Array (inherited from C)
Java was born in 1995, designed by James Gosling at Sun Microsystems. It borrowed the **array** concept directly from C/C++. An array is the most primitive data structure:

```java
int[] nums = new int[5];          // fixed-size, allocated on heap
int[] nums = {1, 2, 3, 4, 5};    // shorthand initialization
```

**Why it exists:** It maps directly to how memory works — a contiguous block of memory with O(1) random access via index arithmetic (`baseAddr + index * sizeof(type)`).

**Limitations:**
- **Fixed size** — once created, you cannot grow or shrink it
- **No built-in methods** — no `.add()`, no `.remove()`, no `.contains()`
- **Works with primitives** — `int[]`, `char[]`, `double[]` (this is actually a *superpower* for performance)

```java
int[] arr = new int[3];
arr[0] = 10;
arr[1] = 20;
// arr[3] = 30;  // 💥 ArrayIndexOutOfBoundsException at runtime
// arr.add(40);  // ❌ Doesn't compile — arrays have no methods
System.out.println(arr.length);  // .length is a FIELD, not a method (no parentheses!)
```

> 🧠 **Interview gotcha:** `arr.length` (no `()`) for arrays vs `str.length()` (with `()`) for Strings vs `list.size()` for Collections. Three different ways to get "size" in Java!

---

#### 2. `ArrayList<E>` — The Dynamic Array (Java 1.2, 1998)

Developers needed arrays that could grow. Java 1.2 introduced the **Collections Framework**, and `ArrayList` was its star: a **resizable array** backed by a plain `Object[]` internally.

```java
import java.util.ArrayList;

ArrayList<Integer> list = new ArrayList<>();  // note: Integer, not int
list.add(10);
list.add(20);
list.add(30);
list.remove(1);           // removes element at index 1 → [10, 30]
System.out.println(list.size());  // 2
System.out.println(list.get(0));  // 10
```

**Why it exists:** Solves the fixed-size limitation of arrays.

**How it works internally:**
1. Starts with a default capacity of **10**
2. When full, creates a **new array of 1.5× the size** and copies everything over
3. This means `.add()` is **amortized O(1)** but occasionally O(n) during resize

**Limitation:** Cannot hold primitives! Only objects.
```java
ArrayList<int> nums = new ArrayList<>();     // ❌ Doesn't compile
ArrayList<Integer> nums = new ArrayList<>(); // ✅ Must use wrapper class
```

> 🧠 **Why no primitives?** Generics in Java use **type erasure** — at runtime, `ArrayList<Integer>` becomes `ArrayList<Object>`. Primitives aren't objects, so they can't be stored. Java **autoboxes** `int → Integer` for you, but this has a performance cost (heap allocation per element).

---

#### 3. `List<E>` — The Interface (abstraction over implementations)

`List` is NOT a data structure — it's an **interface** (a contract). It says *"anything that implements me must support `.add()`, `.get()`, `.remove()`, `.size()`, etc."*

```java
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

List<Integer> list1 = new ArrayList<>();   // backed by dynamic array
List<Integer> list2 = new LinkedList<>();  // backed by doubly-linked list
```

**Why it exists:** **Polymorphism and clean APIs.** When you write a method, you say:

```java
// ✅ GOOD — accepts ANY List implementation
public int sum(List<Integer> numbers) { ... }

// ❌ BAD — forces callers to use ArrayList specifically
public int sum(ArrayList<Integer> numbers) { ... }
```

> 🧠 **FAANG principle:** "Program to interfaces, not implementations." This is a core SOLID principle (Dependency Inversion). In interviews, declare `List<>` on the left, `ArrayList<>` on the right.

---

### Summary Table

| Feature | `int[]` | `ArrayList<Integer>` | `List<Integer>` |
|---------|---------|---------------------|-----------------|
| Type | Concrete (primitive array) | Concrete class | Interface |
| Size | Fixed | Dynamic (grows 1.5×) | Depends on impl |
| Primitives? | ✅ Yes | ❌ No (autoboxing) | ❌ No |
| Performance | Best (no boxing) | Good (amortized O(1) add) | Depends on impl |
| Methods | None (just `.length`) | Full API | Full API |
| Use when | Performance-critical, fixed-size known | General purpose | Method signatures, polymorphism |

---

### 🔧 Convenience: `List.of()` and `Arrays.asList()`

```java
// Immutable list (Java 9+)
List<Integer> immutable = List.of(1, 2, 3);
// immutable.add(4);  // 💥 UnsupportedOperationException

// Fixed-size but mutable elements (backed by the array)
List<String> fixed = Arrays.asList("a", "b", "c");
// fixed.add("d");    // 💥 UnsupportedOperationException
fixed.set(0, "z");    // ✅ This works — element mutation is allowed

// Fully mutable
List<Integer> mutable = new ArrayList<>(List.of(1, 2, 3));
mutable.add(4);       // ✅ Works fine
```

---

## ☕ Java Nuance: `orElse()` vs `orElseGet()`

This is an `Optional<T>` concept. Both provide a fallback value when the Optional is empty, but they differ in **when the fallback is evaluated**:

```java
// orElse() — fallback is ALWAYS evaluated (eager)
String name = optional.orElse(expensiveComputation());
// expensiveComputation() runs even if optional has a value!

// orElseGet() — fallback is evaluated ONLY if needed (lazy)
String name = optional.orElseGet(() -> expensiveComputation());
// expensiveComputation() runs only if optional is empty
```

**Why does this matter?**
```java
// Dangerous with side effects:
User user = findUser(id).orElse(createDefaultUser());
// createDefaultUser() ALWAYS runs — might insert a row in DB!

// Safe:
User user = findUser(id).orElseGet(() -> createDefaultUser());
// createDefaultUser() runs ONLY if findUser returns empty
```

> 🧠 **Rule of thumb:** Use `orElse()` only for **simple constants** like `orElse("default")` or `orElse(0)`. Use `orElseGet()` for anything that involves **computation, I/O, or side effects**.

---

## 🧩 Problems Solved

### Pattern Summary

| Problem | Data Structure | Map Usage Pattern |
|---------|---------------|-------------------|
| Contains Duplicate | `Set` | Existence: "seen before?" |
| Valid Anagram | `Map<char, count>` | Frequency: "how many times?" |
| Two Sum | `Map<value, index>` | Location: "where did I see it?" |
| Group Anagrams | `Map<key, List>` | Grouping: "which bucket does this belong to?" |
| Longest Consecutive Sequence | `HashSet` | Existence + sequence start: "is num-1 absent?" |
| Top K Frequent Elements | `Map` + `PriorityQueue` | Frequency + ranking: "which k are most frequent?" |
| Merge Adjacent Equal Elements | `ArrayList` as stack | Adjacent collapse: "does this equal what's on top?" |
| Max Points on a Line | `Map<slope, count>` | Grouping: "how many share this slope from anchor?" |

---

### 1. Contains Duplicate (Easy) ✅
- **Approach:** `HashSet` — check existence with `Set.add()` returning `boolean`
- **Time:** O(n) | **Space:** O(n)
- **Alt:** Sort first → O(n log n) time, O(1) space
- 📄 [ContainsDuplicate.java](./ContainsDuplicate.java)

### 2. Valid Anagram (Easy) ✅
- **Approach:** `HashMap<Character, Integer>` frequency count — increment for `s`, decrement for `t`
- **Time:** O(n) | **Space:** O(1) — at most 26 keys
- **Optimal:** `int[26]` array — no autoboxing, O(1) space, single loop
- **Key trick:** `charAt(i) - 'a'` → numeric promotion (`char` widens to `int`)
- 📄 [ValidAnagram.java](./ValidAnagram.java)

### 3. Two Sum (Easy) ✅
- **Approach:** Single-pass `HashMap<value, index>` — check complement before insert
- **Time:** O(n) | **Space:** O(n)
- **Key insight:** `complement = target - nums[i]`. Check BEFORE insert → prevents self-matching
- 📄 [TwoSum.java](./TwoSum.java)

### 4. Group Anagrams (Medium) ✅
- **Approach 1:** Sort chars as key → O(n · k log k)
- **Approach 2:** Count array `int[26]` as key → O(n · k) — use `#` separator to avoid collision
- **Key API:** `computeIfAbsent(key, k -> new ArrayList<>())` — returns existing or newly created list
- **Gotcha:** `List.add()` returns `boolean`, not the list. Map stores references — no re-put needed.
- 📄 [GroupAnagrams.java](./GroupAnagrams.java)

### 9. Longest Consecutive Sequence (Medium) ✅
- **Approach 1:** `TreeSet` + `prev` variable pattern → O(n log n)
  - "prev variable pattern" — track previous value when iterating without index access
- **Approach 2 (Optimal):** `HashSet` + "end of sequence" trick → O(n)
  - Only count from elements where `num + 1` is NOT in set, count downward
  - Each element visited at most twice
- **Key insight:** O(log n) per-op × n ops = O(n log n) total. O(1) per-op × n = O(n) total
- 📄 [LongestConsecutiveSequence.java](./LongestConsecutiveSequence.java)

### 5. Top K Frequent Elements (Medium) ✅
- **Approach 1:** Frequency map + Max-Heap (`PriorityQueue`) → O(n + m log m)
- **Approach 2 (Optimal):** Bucket Sort (index = frequency) → O(n)
- **Key API:** `PriorityQueue` with custom comparator `(a,b) -> b.getValue().compareTo(a.getValue())`
- **Gotcha:** `entry.getKey()` returns element, `entry.getValue()` returns count — don't mix them up!
- 📄 [TopKFrequentElements.java](./TopKFrequentElements.java)

### Merge Adjacent Equal Elements (Medium) ✅
- **Approach 1 (Naive):** Simulation — scan, merge leftmost, rebuild → O(n²) TLE
- **Approach 2 (Optimal):** Stack — build result left-to-right, chain-react on push → O(n)
- **Key pattern:** "Adjacent collapse" = Stack. Push element, compare with top, merge if equal, repeat.
- **Bugs hit:** `==` on `Long` objects (cache trap again!), out-of-bounds from missing bounds checks
- **Meta-lesson:** When code needs many edge-case patches, the approach is fighting the problem — simpler approach = simpler code
- 📄 [MergeAdjacentEqualElements.java](../Stacks-and-Queues/MergeAdjacentEqualElements.java)

### 149. Max Points on a Line (Hard) ✅
- **Approach:** For each anchor, compute GCD-normalized slopes, count via HashMap
- **Key trick:** Normalize direction (flip if dx < 0), handle vertical/horizontal, use `"dx/dy"` as key
- **Time:** O(n²) — optimal (must compare all pairs)
- 📄 [MaxPointsOnALine.java](./MaxPointsOnALine.java)

### 238. Product of Array Except Self (Medium) ✅
- **Approach 1:** Two arrays (prefix + suffix) → O(n) time, O(n) space
- **Approach 2 (Optimal):** Build prefix into result, single `suffix` variable right-to-left → O(1) extra space
- **Key insight:** `result[i]` = product LEFT of `i` × product RIGHT of `i`
- **Gotcha:** `prefix[i]` = product **before** `i` (excludes `i`), not cumulative product up to `i`
- 📄 [ProductOfArrayExceptSelf.java](./ProductOfArrayExceptSelf.java)

### 36. Valid Sudoku (Medium) ✅
- **Approach 1:** Three passes (rows, cols, 3×3 boxes) — clearer
- **Approach 2:** Single pass with arrays of sets for rows, cols, boxes
- **Key formula:** Box index = `(i/3)*3 + (j/3)` — maps any cell to its box (0-8)
- **Trick:** `Set.add()` returns `false` if duplicate → check + insert in one call
- **Time:** O(1) — board is always 9×9
- 📄 [ValidSudoku.java](./ValidSudoku.java)

### 448. Find All Numbers Disappeared in an Array (Easy) ✅
- **Approach 1:** HashSet → O(n) time, O(n) space
- **Approach 2 (Optimal):** Negation trick → O(n) time, O(1) extra space
  - Values in [1,n] → each maps to index (value-1). Negate to mark "exists"
  - `Math.abs()` to handle already-negated values, `if > 0` to prevent double-negate
  - Positive indices after marking = missing numbers
- 📄 [FindDisappearedNumbers.java](./FindDisappearedNumbers.java)

### 1051. Height Checker (Easy) ✅
- **Approach:** Sort a copy with `Arrays.copyOf()` + `Arrays.sort()`, compare with original
- **Time:** O(n log n) | **Space:** O(n)
- 📄 [HeightChecker.java](./HeightChecker.java)

### 487. Max Consecutive Ones II (Medium) ✅
- **Approach:** Track `leftCount` (ones after last zero) and `rightCount` (ones before last zero + flipped zero). Result = max(left + right)
- **Time:** O(n) | **Space:** O(1)
- 📄 [MaxConsecutiveOnesII.java](./MaxConsecutiveOnesII.java)

### 414. Third Maximum Number (Easy) ✅
- **Approach:** Three passes — find 1st, 2nd (excl 1st), 3rd (excl 1st & 2nd)
- **Gotcha:** `Integer.MIN_VALUE` edge case — use boolean flag to track if 3rd max exists
- **Time:** O(n) | **Space:** O(1)
- 📄 [ThirdMaximumNumber.java](./ThirdMaximumNumber.java)

### 66. Plus One (Easy) ✅
- **Approach:** Simulate addition right-to-left with carry
- **Edge case:** All 9s → new array of `size+1` with `result[0]=1`
- **Trap:** Don't convert to `int`/`Long` — arrays can be 100+ digits, overflows `Long.MAX_VALUE`
- **Time:** O(n) | **Space:** O(1)
- 📄 [PlusOne.java](./PlusOne.java)

### 1275. Find Winner on a Tic Tac Toe Game (Easy) ✅
- **Approach:** Build board from moves, check rows/cols/diagonals for 3 matching non-empty cells
- **Time:** O(1) | **Space:** O(1) — board is always 3×3
- 📄 [TicTacToeWinner.java](./TicTacToeWinner.java)

### 1282. Group the People (Medium) ✅
- **Approach:** HashMap grouping by size → chunk lists into sublists via `subList`
- **Time:** O(n) | **Space:** O(n)
- 📄 [GroupThePeople.java](./GroupThePeople.java)

### 3847. Find the Score Difference in a Game (Easy) ✅
- **Approach:** Track active player, XOR toggle logic — both swap conditions cancel out
- **Key formula:** 6th game = `(i+1) % 6 == 0`, not `i % 5 == 0`
- **Time:** O(n) | **Space:** O(1)
- 📄 [ScoreDifference.java](./ScoreDifference.java)

### 3848. Check Digitorial Permutation (Easy) ✅
- **Approach:** Sum digit factorials (order-independent), sort-and-compare for permutation check
- **Key insight:** Sum is same for all permutations → compute once, check if sum is a permutation of n
- **Time:** O(d log d) | **Space:** O(d)
- 📄 [DigitorialPermutation.java](./DigitorialPermutation.java)

### 3849. Maximum Bitwise XOR After Rearrangement (Medium) ✅
- **Approach:** Greedy — count 0s/1s in t, assign opposite bits from MSB to maximize XOR
- **Key insight:** XOR=1 when bits differ. Leftmost bits have highest value → greedy from left
- **Time:** O(n) | **Space:** O(n)
- 📄 [MaximumXOR.java](./MaximumXOR.java)

### 189. Rotate Array (Medium) ✅
- **Approach 1:** Extra array — copy last k first, then rest → O(n) time, O(n) space
- **Approach 2 (Optimal):** Triple reverse — reverse all, first k, rest → O(n) time, O(1) space
- **Approach 3:** Cyclic replacement — follow chain `(i+k)%n` → O(n) time, O(1) space
- **Key insight:** Array = `[A|B]`, want `[B|A]`. Reverse all → fix each half
- 📄 [RotateArray.java](./RotateArray.java)

### Smallest Pair With Different Frequencies (Easy) ✅
- **Approach:** Frequency map + sorted distinct pairs scan — first valid pair = answer
- **Key insight:** Sort → skip duplicates → first `(x, y)` with `freq(x) != freq(y)` is guaranteed smallest
- **Bugs hit:** `Map.add()` doesn't exist (use `put()`), `nums.lenth` typo, `i` never incremented → infinite loop, missing `[-1,-1]` return
- **Gotcha:** Use `.equals()` not `!=` when comparing `Integer` objects from `Map.get()`
- **Time:** O(n log n) | **Space:** O(n)
- 📄 [SmallestPairDifferentFrequencies.java](./SmallestPairDifferentFrequencies.java)

### 705. Design HashSet (Easy) 📖 — Reference
- **Approach 1 (My naive):** `ArrayList` with linear scan — O(n) per operation
- **Approach 2 (Bucket-based):** Array of lists, `key % MAX_LEN` for bucket index, chaining for collisions — O(n/k) average
- **Key concept:** Hash function maps key → bucket index. Collisions handled by list within bucket.
- 📄 [DesignHashSet.java](./DesignHashSet.java)

### 706. Design HashMap (Easy) 📖 — Reference
- **Approach 1 (My naive):** Direct address table `int[1_000_001]` — O(1) but only works for bounded integer keys
- **Approach 2 (Bucket-based):** Array of `List<Pair<K,V>>`, same hashing + chaining as HashSet but stores key-value pairs
- **Key difference from HashSet:** `put()` must handle both insert AND update
- 📄 [DesignHashMap.java](./DesignHashMap.java)

### 136. Single Number (Easy) ✅
- **Approach:** XOR all elements — pairs cancel (`a ^ a = 0`), single remains (`a ^ 0 = a`)
- **Key insight:** XOR is the **only** approach for O(n) time + O(1) space. HashSet/HashMap use O(n) space, sorting uses O(n log n) time.
- **Time:** O(n) | **Space:** O(1)
- 📄 [SingleNumber.java](./SingleNumber.java)

### 349. Intersection of Two Arrays (Easy) ✅
- **Approach:** HashMap from `nums1`, scan `nums2` — mark visited with `-1` to avoid duplicates
- **Bug hit:** Used `nums1[i]` instead of `nums2[i]` in second loop — wrong array with shared loop variable
- **Simpler alt:** HashSet (only need existence, not frequency)
- **Time:** O(n + m) | **Space:** O(n)
- 📄 [IntersectionOfTwoArrays.java](./IntersectionOfTwoArrays.java)

### 202. Happy Number (Easy) ✅
- **Approach:** HashSet cycle detection + digit extraction (`% 10`, `/ 10`)
- **Key structure:** Two nested loops — inner extracts digits & sums squares, outer repeats until `sum == 1` or cycle
- **Bugs hit:** Set check inside inner loop (partial sums), missing `n = sum`, missing `sum == 1` check
- **Time:** O(log n) per step | **Space:** O(k)
- 📄 [HappyNumber.java](./HappyNumber.java)

### 205. Isomorphic Strings (Easy) ✅
- **Wrong approach:** Frequency counting — fails for `"abab"` vs `"cddc"` (same frequencies, not isomorphic)
- **Correct approach:** Two `int[256]` mapping arrays (s→t and t→s), walk position by position
- **Key insight:** Mapping must be **bijective** — need both directions to catch `"ab"` → `"aa"` (two chars map to same target)
- **Time:** O(n) | **Space:** O(1)
- 📄 [IsomorphicStrings.java](./IsomorphicStrings.java)

---

## 🧠 Pattern Recognition

### "Negation Trick" — O(1) space for [1,n] range problems

When values are in `[1, n]` and you need to mark presence without extra space:
1. For each value, compute `index = Math.abs(value) - 1`
2. Negate `nums[index]` to mark "this value exists"
3. Scan: positive index = that number is missing

### "Adjacent Collapse" = Stack

When a problem says "repeatedly merge/remove/collapse adjacent elements":

```
1. "Am I comparing adjacent elements?"        → Think stack
2. "Can a merge create new pairs?"             → Definitely stack (chain reaction)
3. "Am I processing left to right?"            → Stack works naturally
```

**Examples:** Merge Adjacent Equal, Remove Duplicate Chars, Asteroid Collision, Valid Parentheses

**ArrayList as stack:**
| Operation | Code | Time |
|-----------|------|------|
| peek | `list.get(list.size() - 1)` | O(1) |
| pop | `list.remove(list.size() - 1)` | O(1) |
| push | `list.add(value)` | O(1) |

---

## 📚 HashMap Reference

### When to use HashMap
- **"Find if element exists"** with O(1): avoids O(n) scans
- **"Two Sum" style:** store complements → find pairs in O(n) instead of O(n²)
- **"Count frequency":** naturally aggregate counts while iterating
- **"Group by" property:** use transformed keys (sorted strings, count arrays) to bucket elements
- **"First/last occurrence":** store indices as values to remember positions

### When NOT to use HashMap
- **Need ordered iteration:** use `TreeMap` or sorted structures
- **Memory constraints:** HashMap has overhead (keys + values + collision handling)
- **In-place required:** HashMap uses O(n) extra space

### HashMap vs Array — when to use which
- **Array:** keys are small integers in a known range (e.g., `int[26]` for characters)
- **HashMap:** keys are sparse, large, or non-numeric (strings, coordinates, IDs)
- **Trade-off:** arrays = O(1) with no overhead but waste space on sparse data; HashMaps = any key type but hashing overhead

---

## ☕ Java Nuances Learned

### `==` vs `.equals()` and the Integer Cache
- `==` compares **references** for objects, **values** for primitives
- `Integer`/`Long` values -128 to 127 are **cached** — `==` works by coincidence, breaks outside this range
- **Always use `.equals()`** for object comparison (or `Objects.equals()` for null-safety)
- **Exception:** when one side is primitive (`long current`), auto-unboxing makes `==` safe
  - `result.get(i) == current` where `current` is `long` → unboxes the `Long`, compares values ✅

### `Map.get()` returns `null`, not `0`
- Comparing `map.get(key) != 0` causes **NullPointerException** (auto-unboxing `null`)
- Use `containsKey()` or `getOrDefault(key, 0)` instead

### `computeIfAbsent(key, k -> new ArrayList<>())`
- Returns the value for key: **existing** if present, **newly created** if absent
- The lambda only runs when key is absent
- Perfect for "get or create" grouping pattern

### Autoboxing costs
- `int → Integer` (autoboxing) allocates objects on heap
- `int[]` is always faster than `ArrayList<Integer>` — no boxing, cache-friendly, less GC pressure
- Use `int[]` when size is known; `ArrayList` when dynamic sizing needed

### Size/Length inconsistency
- `arr.length` — field (no parentheses)
- `str.length()` — method
- `list.size()` — method
