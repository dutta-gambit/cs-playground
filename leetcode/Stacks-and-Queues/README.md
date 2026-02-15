# Stacks and Queues

> Problems where the core pattern involves stack or queue-based processing.

---

## 📦 Java Interface Hierarchy

```
                 Collection
                /          \
             List          Queue       ← separate interface branches
            /    \            \
      ArrayList   \          Deque     ← extends Queue
                   \        /     \
                  LinkedList    ArrayDeque
                  (implements    (implements
                   BOTH List      only Deque)
                   AND Deque)

   ❌ There is NO Stack interface in Java.
   Deque has push/pop/peek built in — it doubles as the stack contract.
   Stack class exists but is legacy (extends Vector) — never use it.
```

### LinkedList implements BOTH interfaces:
```java
public class LinkedList<E> implements List<E>, Deque<E> { ... }
```

### How the reference type controls what you can call

Same object, different "view" — compiler restricts based on declared type:

```java
Queue<Integer> q  = new ArrayDeque<>();  // sees ONLY Queue methods
Deque<Integer> d  = new ArrayDeque<>();  // sees ALL deque/stack/queue methods
List<Integer> l   = new LinkedList<>();  // sees ONLY List methods (get, set, add at index)
Deque<Integer> dq = new LinkedList<>();  // sees ONLY Deque methods (not get(i)!)
```

> `Deque<Integer> stack` — variable name `stack` is a **human convention**, not compiler-enforced. You CAN still call `addLast()` on it. Your discipline enforces stack-only usage.

---

## 🥞 Stack (LIFO — Last In, First Out)

> Mental model: stack of plates. All operations on TOP only.

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);       // add to top
stack.pop();         // remove from top (💥 throws if empty!)
stack.peek();        // look at top (returns null if empty)
stack.isEmpty();     // check if empty
```

### Operations (all O(1)):

| Operation | Method | If empty |
|-----------|--------|----------|
| Add to top | `push(e)` | Always works |
| Remove top | `pop()` | 💥 `NoSuchElementException` |
| Look at top | `peek()` | Returns `null` |

> ❌ **Never use `java.util.Stack`** — extends `Vector` (synchronized = slow), allows `get(i)` (breaks stack contract), legacy from Java 1.0.

### All ways to implement a stack in Java:

| Approach | Declaration | Best for |
|----------|-------------|----------|
| `ArrayDeque` (recommended) | `Deque<Integer> s = new ArrayDeque<>();` | General purpose |
| `ArrayList` | `List<Integer> s = new ArrayList<>();` | When result IS the stack |
| `LinkedList` | `Deque<Integer> s = new LinkedList<>();` | When nulls needed |
| Raw array | `int[] s = new int[n]; int top = -1;` | Max performance |
| Recursion | Call stack itself | DFS, tree traversal |

> ⚠️ `List` does NOT have `push/pop/peek/offer/poll` — those belong to `Queue`/`Deque`. With `ArrayList`, you manually simulate using `add`, `get(size-1)`, `remove(size-1)`.

---

## 📥 Queue (FIFO — First In, First Out)

> Mental model: movie theater line. First in line gets served first.
> Uses **two ends**: insert at REAR, remove/peek from FRONT.

```
     offer(A)  offer(B)  offer(C)
                                       poll() → A
     ┌────┬────┬────┐
     │  A │  B │  C │
     └────┴────┴────┘
     front         rear
     ↑              ↑
     remove here    insert here
```

```java
Queue<Integer> queue = new ArrayDeque<>();
queue.offer(1);      // add to REAR
queue.poll();        // remove from FRONT (returns null if empty)
queue.peek();        // look at FRONT (returns null if empty)
```

### ⚠️ Two sets of methods — safe vs unsafe:

| Operation | Safe (returns null/false) | Unsafe (throws exception) |
|-----------|--------------------------|---------------------------|
| Insert | `offer(e)` → `false` | `add(e)` → 💥 `IllegalStateException` |
| Remove | `poll()` → `null` | `remove()` → 💥 `NoSuchElementException` |
| Examine | `peek()` → `null` | `element()` → 💥 `NoSuchElementException` |

> 🧠 **Interview rule:** Always use `offer/poll/peek` (the safe versions).

---

## ↔️ Deque (Double-Ended Queue)

> Mental model: tunnel open on both sides. Enter/exit from either end.

```java
Deque<Integer> dq = new ArrayDeque<>();
dq.addFirst(1);      // add to front
dq.addLast(2);       // add to back
dq.removeFirst();    // remove from front (💥 if empty)
dq.removeLast();     // remove from back (💥 if empty)
dq.peekFirst();      // look at front (null if empty)
dq.peekLast();       // look at back (null if empty)
```

### How Deque acts as Stack vs Queue:

| As Stack (one end) | As Queue (two ends) |
|--------------------|---------------------|
| `push(e)` → addFirst | `offer(e)` → addLast |
| `pop()` → removeFirst | `poll()` → removeFirst |
| `peek()` → peekFirst | `peek()` → peekFirst |

### Safe vs unsafe for Deque:

| Safe (null) | Unsafe (throws) |
|-------------|-----------------|
| `peekFirst()` / `peekLast()` | `getFirst()` / `getLast()` |
| `pollFirst()` / `pollLast()` | `removeFirst()` / `removeLast()` |
| — | `pop()` (no safe equivalent! use `pollFirst()`) |

---

## ⚙️ ArrayDeque Internals — Circular Array

### The problem with normal arrays:
Removing from front requires shifting all elements — O(n).

### Solution: circular buffer with `head` and `tail` pointers

```
offer(A), offer(B), offer(C):
┌────┬────┬────┬────┬────┬────┬────┬────┐
│  A │  B │  C │    │    │    │    │    │
└────┴────┴────┴────┴────┴────┴────┴────┘
  ↑              ↑
 head           tail

poll() → A.  Just move head forward — O(1)!
┌────┬────┬────┬────┬────┬────┬────┬────┐
│    │  B │  C │    │    │    │    │    │
└────┴────┴────┴────┴────┴────┴────┴────┘
       ↑         ↑
      head      tail

After many operations — tail WRAPS AROUND:
┌────┬────┬────┬────┬────┬────┬────┬────┐
│  G │    │    │    │  D │  E │  F │    │
└────┴────┴────┴────┴────┴────┴────┴────┘
       ↑              ↑
      tail           head
Logical order: D → E → F → G  (physical ≠ logical!)
```

### The math:
```
Add to back:   array[tail] = e;   tail = (tail + 1) % capacity
Remove front:  e = array[head];   head = (head + 1) % capacity
Add to front:  head = (head - 1 + capacity) % capacity;  array[head] = e
```

### Resize: when full, double the array and unwrap elements in logical order.

---

## 🏢 ArrayDeque vs LinkedList

| | `ArrayDeque` | `LinkedList` |
|--|-------------|-------------|
| Backed by | Circular array | Doubly-linked list |
| Speed | ✅ Faster (cache-friendly) | ❌ Slower (pointer chasing) |
| Memory | ✅ Less (just an array) | ❌ More (prev + next per node) |
| Nulls | ❌ Throws NPE | ✅ Allows null |
| Implements | `Deque` only | `List` AND `Deque` |
| `get(i)` | ❌ Not available | ✅ Available (but O(n)!) |

> **Default:** Always `ArrayDeque`. Use `LinkedList` only when you need null values or need both `List` and `Deque` methods on the same object.

> 🧠 **Interview answer:** "ArrayDeque is array-backed with better cache locality and less memory overhead per element. LinkedList has per-node allocation with prev/next pointers. I'd only use LinkedList if I needed null values or the List interface simultaneously."

---

## ⚠️ Common Traps

| Trap | Fix |
|------|-----|
| `stack.pop()` on empty → 💥 | Check `isEmpty()` first, or use `pollFirst()` |
| Using `java.util.Stack` | Use `ArrayDeque` instead |
| `null` in `ArrayDeque` → 💥 NPE | Use `LinkedList` if nulls needed |
| Forgetting `pop()` has no safe version | Use `pollFirst()` for null-safe pop |

---

## 🧠 FAANG Patterns

### Stack Patterns:

| Pattern | Example Problems | Signal in problem |
|---------|-----------------|-------------------|
| **Adjacent collapse** | Merge Adjacent Equal, Remove Duplicates | "adjacent elements interact" |
| **Matching pairs** | Valid Parentheses, Remove Outer Parens | "match opening with closing" |
| **Monotonic stack** | Next Greater Element, Daily Temperatures | "find next larger/smaller" |
| **Expression eval** | Calculator, Postfix eval | "operators and operands" |
| **Undo/backtrack** | Browser history, Simplify Path | "go back to previous" |

### Queue Patterns:

| Pattern | Example Problems | Signal in problem |
|---------|-----------------|-------------------|
| **BFS** | Level-order traversal, Shortest path | "level by level" or "minimum steps" |
| **Sliding window** | Sliding Window Maximum (uses Deque) | "window of size k" |
| **Scheduling** | Task Scheduler, Process queue | "process in order" |

> Stack = DFS (depth-first). Queue = BFS (breadth-first).
> Recursion IS an implicit stack — any recursive solution can be made iterative with an explicit stack.

---

## 🧩 Problems Solved

### Merge Adjacent Equal Elements (Medium) ✅
- **Pattern:** Adjacent collapse = Stack
- **Approach:** Build result as stack; push element, merge with top if equal, chain-react
- **Time:** O(n) | **Space:** O(n)
- 📄 [MergeAdjacentEqualElements.java](./MergeAdjacentEqualElements.java)
