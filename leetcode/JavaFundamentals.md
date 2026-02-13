# Java Fundamentals — Quick Reference

> Learnings collected during DSA practice. Not tied to a specific topic — general Java nuances for interviews.

---

## 📦 Collections Hierarchy

```
            Iterable
               |
           Collection          ← "a group of things"
           /        \
         List       Set        ← more specific contracts
        /    \        \
  ArrayList  LinkedList  HashSet / TreeSet
```

| Interface | Contract | Duplicates? | Ordered? | Indexed? |
|-----------|----------|-------------|----------|----------|
| `Collection` | Iterate, size, add, contains | Yes | No guarantee | No |
| `List` | Ordered, indexed access `get(i)` | Yes | ✅ Insertion order | ✅ |
| `Set` | Unique elements | ❌ No | Depends on impl | No |

---

## 🔢 PriorityQueue (Heap)

- Default: **min-heap** (smallest first)
- Backed by a dynamic array arranged as a binary heap

```java
// Min-heap (default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
// or: new PriorityQueue<>((a, b) -> Integer.compare(b, a));
```

### Key Operations

| Operation | Method | Time |
|-----------|--------|------|
| Insert | `add(e)` / `offer(e)` | O(log n) |
| Peek top | `peek()` | O(1) |
| Remove top | `poll()` | O(log n) |
| Build from collection | constructor | O(n) |

### Custom Comparators

```java
// Ascending (a first = a-scending):  a.compareTo(b)
// Descending (b first = b-iggest):   b.compareTo(a)
```

> ⚠️ **Never use `a - b`** for comparators — integer overflow risk! Use `Integer.compare(a, b)` or `.compareTo()`.

---

## 🗺️ Map Views

```java
map.keySet()    → Set<K>                   // keys are unique → Set
map.values()    → Collection<V>            // values can have dupes, no ordering → Collection
map.entrySet()  → Set<Map.Entry<K,V>>      // key-value pairs
```

### When to use which

| Need | Use |
|------|-----|
| Only keys | `keySet()` |
| Only values | `values()` |
| Both key and value | `entrySet()` (avoids extra `get()` lookups) |

### Map.Entry methods
- `entry.getKey()` — the key
- `entry.getValue()` — the value

### Anti-pattern
```java
// ❌ Two hash lookups per iteration:
for (String key : map.keySet()) { int val = map.get(key); }

// ✅ Single iteration, no extra lookups:
for (Map.Entry<String, Integer> e : map.entrySet()) { ... }
```

---

## 🔄 Autoboxing & Integer Cache

- `int ↔ Integer` conversion happens automatically (autoboxing/unboxing)
- Integer values **-128 to 127** are cached — `==` works by coincidence
- **Always use `.equals()`** for object comparison
- `Objects.equals(a, b)` — null-safe version

---

## 📏 Size/Length Inconsistency

| Type | Syntax | Why |
|------|--------|-----|
| Array `int[]` | `.length` (field) | JVM-special, no method needed |
| String | `.length()` (method) | Encapsulation (OOP) |
| Collection | `.size()` (method) | Interface contract |

---

## ⚡ orElse() vs orElseGet()

```java
optional.orElse(value)            // value ALWAYS evaluated (eager)
optional.orElseGet(() -> value)   // lambda runs ONLY if empty (lazy)
```

**Rule:** `orElse("constant")` is fine. Computation/side effects → `orElseGet()`.

---

## 🔧 Useful Patterns

### computeIfAbsent — "get or create"
```java
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
// Returns existing list for key, or creates new one if absent
```

### getOrDefault — safe get with fallback
```java
map.getOrDefault(key, 0)  // returns 0 if key not found (instead of null)
```

### Character to index
```java
char c = 'e';
int index = c - 'a';  // numeric promotion: 101 - 97 = 4
```
