# Java Generics Deep Dive — Type Erasure & Covariance

> These are foundational concepts in Java's type system. Understanding them explains many "weird" Java limitations you'll encounter in interviews and production code.

---

## 1. Type Erasure

### What is it?

Generics in Java are a **compile-time only** feature. The JVM knows nothing about them. At compile time, the Java compiler:
1. Checks your generic types for correctness
2. **Erases all generic type info** from the bytecode

```java
// What YOU write:
List<String> names = new ArrayList<String>();
names.add("alice");
String name = names.get(0);

// What the JVM sees (after erasure):
List names = new ArrayList();
names.add("alice");
String name = (String) names.get(0);   // compiler inserts cast
```

### Why does Java do this?

**Backward compatibility.** Generics were added in Java 5 (2004), but Java already had millions of lines of code using raw `List`, `Map`, etc. Sun Microsystems chose type erasure so that:
- Old code (`List`) and new code (`List<String>`) produce **identical bytecode**
- No changes needed to the JVM itself
- Old libraries kept working without recompilation

> This is called **migration compatibility** — the most controversial design decision in Java's history. Languages like C# chose **reified generics** (generics survive at runtime) but couldn't maintain backward compatibility.

### What gets erased to what?

| Compile-time type | Erased to (runtime type) |
|-------------------|--------------------------|
| `List<String>` | `List` |
| `Map<Integer, String>` | `Map` |
| `T` (unbounded) | `Object` |
| `T extends Comparable` | `Comparable` |
| `T extends Number` | `Number` |

```java
// This method:
public <T extends Comparable<T>> T findMax(List<T> list) { ... }

// Becomes at runtime:
public Comparable findMax(List list) { ... }
```

### Consequences of erasure

#### 1. Can't use `instanceof` with generics
```java
if (list instanceof List<String>) { }   // ❌ won't compile
if (list instanceof List<?>) { }        // ✅ wildcard OK (erased anyway)
if (list instanceof ArrayList) { }      // ✅ raw type OK
```

#### 2. Can't create generic instances
```java
T obj = new T();           // ❌ JVM doesn't know what T is
T[] arr = new T[10];       // ❌ same reason
```

#### 3. Can't overload by generic type
```java
// ❌ Both erase to process(List) — compile error: duplicate method
void process(List<String> strings) { }
void process(List<Integer> numbers) { }
```

#### 4. Static fields can't use class type parameter
```java
class Box<T> {
    static T value;     // ❌ which T? Box<String>.value and Box<Integer>.value
                        //    would share the SAME static field
}
```

---

## 2. Covariance, Invariance, and Contravariance

These describe how **subtyping relationships** transfer to generic types.

### Prerequisite: `Dog extends Animal`

```java
class Animal { }
class Dog extends Animal { }
class Cat extends Animal { }

Dog dog = new Dog();
Animal animal = dog;        // ✅ Dog IS-A Animal (subtyping)
```

### Arrays are COVARIANT (subtyping transfers)

`Dog[]` is treated as a subtype of `Animal[]`:

```java
Dog[] dogs = new Dog[3];
Animal[] animals = dogs;     // ✅ compiles — "Dog[] IS-A Animal[]"
animals[0] = new Dog();      // ✅ works fine
animals[1] = new Cat();      // 💥 ArrayStoreException at RUNTIME!
// JVM knows the actual array is Dog[], and Cat is not a Dog
```

**Arrays know their element type at runtime** (they're **reified**), so the JVM catches the violation — but only at runtime, not compile time. This is a **design flaw** that Java is stuck with.

### Generics are INVARIANT (subtyping does NOT transfer)

`List<Dog>` is **NOT** a subtype of `List<Animal>`:

```java
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;   // ❌ WON'T COMPILE
```

**Why?** Because if it compiled, this would be possible:

```java
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;    // hypothetically allowed
animals.add(new Cat());         // legal — Cat IS-A Animal
Dog dog = dogs.get(0);          // 💥 it's actually a Cat!
```

Since generics are **erased** at runtime, the JVM can't catch this like arrays can (no `ArrayStoreException` equivalent). So Java **prevents it at compile time** by making generics invariant.

### Wildcards — controlled flexibility

If invariance is too strict, Java offers **bounded wildcards**:

#### `? extends T` — Upper bound (covariance for reading)

```java
// "A list of SOME type that extends Animal" — I can READ Animals from it
List<? extends Animal> animals = new ArrayList<Dog>();    // ✅ allowed!
Animal a = animals.get(0);       // ✅ safe to read as Animal
animals.add(new Dog());          // ❌ can't add — compiler doesn't know exact type
```

**Think:** `extends` = "I want to **get** things out" (producer)

#### `? super T` — Lower bound (contravariance for writing)

```java
// "A list of SOME type that is a superclass of Dog" — I can WRITE Dogs to it
List<? super Dog> list = new ArrayList<Animal>();    // ✅ allowed!
list.add(new Dog());             // ✅ safe to add Dogs
Object obj = list.get(0);       // ⚠️ can only read as Object (don't know exact type)
```

**Think:** `super` = "I want to **put** things in" (consumer)

#### PECS — Producer Extends, Consumer Super

This is Joshua Bloch's (author of Effective Java) mnemonic:

| If the generic type... | Use | Example |
|------------------------|-----|---------|
| **Produces** values (you read from it) | `? extends T` | `List<? extends Number>` |
| **Consumes** values (you write to it) | `? super T` | `List<? super Integer>` |
| Both reads and writes | Exact type, no wildcard | `List<Integer>` |

```java
// Real-world example: Collections.copy()
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
//                                ^^^^^ consumer             ^^^^^^ producer
    for (int i = 0; i < src.size(); i++) {
        dest.set(i, src.get(i));
    }
}
```

---

## 3. The Collision — Why Generic Arrays Are Banned

Now you can see why `new List<Integer>[10]` is illegal:

| Feature | Behavior | When checked |
|---------|----------|-------------|
| **Arrays** | Covariant, reified (knows type at runtime) | Runtime |
| **Generics** | Invariant, erased (type info lost at runtime) | Compile time |

```java
// If this were allowed:
List<Integer>[] intLists = new List<Integer>[10];   // hypothetical

// Array covariance allows this:
Object[] objects = intLists;

// Insert wrong type through Object[] reference:
objects[0] = new ArrayList<String>();
// Array should throw ArrayStoreException...
// but at runtime, both are just "List" (erasure!) — JVM CAN'T tell the difference!

// Silent corruption:
List<Integer> list = intLists[0];    // actually List<String>!
Integer num = list.get(0);           // 💥 ClassCastException with no warning
```

**Java bans generic array creation** to prevent this undetectable type corruption.

### Workarounds

```java
// 1. Raw type + suppress warning
@SuppressWarnings("unchecked")
List<Integer>[] buckets = new List[n];

// 2. ArrayList of ArrayLists (preferred in most cases)
List<List<Integer>> buckets = new ArrayList<>();
for (int i = 0; i < n; i++) buckets.add(new ArrayList<>());

// 3. HashMap instead of array (when index isn't needed)
Map<Integer, List<Integer>> buckets = new HashMap<>();
```

---

## Summary Table

| Concept | Arrays | Generics |
|---------|--------|----------|
| Type info at runtime | ✅ Reified | ❌ Erased |
| Variance | Covariant (`Dog[]` → `Animal[]`) | Invariant (`List<Dog>` ≠ `List<Animal>`) |
| Type safety check | Runtime (`ArrayStoreException`) | Compile time |
| Unsafe combination | — | Generic array creation banned |

| Wildcard | Meaning | Can read? | Can write? | Mnemonic |
|----------|---------|-----------|------------|----------|
| `<T>` | Exact type | ✅ | ✅ | — |
| `<? extends T>` | T or subtype | ✅ as T | ❌ | **P**roducer **E**xtends |
| `<? super T>` | T or supertype | ⚠️ as Object | ✅ | **C**onsumer **S**uper |
