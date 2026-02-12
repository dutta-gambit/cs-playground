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

## 🧩 Problems (will be added as we solve them)

*(problems and solutions will be documented below as we work through them)*

