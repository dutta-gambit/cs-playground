# 04 - Creational Patterns

This module covers creational design patterns that deal with object creation mechanisms.

## Patterns Covered

| Pattern | Intent | When to Use |
|---------|--------|-------------|
| **Singleton** | Ensure single instance | Database connections, loggers, config |
| **Factory Method** | Defer instantiation to subclasses | Multiple product types, unknown types at compile-time |
| **Abstract Factory** | Create families of related objects | UI toolkits, cross-platform code |
| **Builder** | Construct complex objects step by step | Objects with many optional parameters |
| **Prototype** | Clone existing objects | Expensive object creation, object pools |

## Key Learnings

- [ ] Singleton (Thread-safe implementations)
- [ ] Factory Method Pattern
- [ ] Abstract Factory Pattern
- [ ] Builder Pattern
- [ ] Prototype Pattern

## Java-Specific Examples

- **Singleton**: `Runtime.getRuntime()`, Spring Beans
- **Factory**: `Calendar.getInstance()`, `NumberFormat.getInstance()`
- **Builder**: `StringBuilder`, `Stream.Builder`

## Practice Implementation

- [ ] Thread-safe Singleton (Bill Pugh, Enum, Double-checked locking)
- [ ] Document Factory (PDF, Word, Excel)
- [ ] Pizza Builder with step-by-step construction
- [ ] Database connection pool with Prototype

## Interview Questions

1. How to make Singleton thread-safe?
2. Factory vs Abstract Factory - when to use which?
3. Why prefer Builder over telescoping constructors?
