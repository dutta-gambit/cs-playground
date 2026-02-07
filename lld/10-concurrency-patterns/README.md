# 10 - Concurrency Patterns

This module covers multi-threading and concurrency patterns essential for senior Java developers.

## Topics Covered

| Pattern | Description | Java Support |
|---------|-------------|--------------|
| **Producer-Consumer** | Decouple production from consumption | `BlockingQueue` |
| **Thread Pool** | Manage thread lifecycle | `ExecutorService` |
| **Read-Write Lock** | Allow concurrent reads, exclusive writes | `ReadWriteLock` |
| **Semaphore** | Control access to limited resources | `Semaphore` |
| **CountDownLatch** | Wait for multiple operations | `CountDownLatch` |
| **CyclicBarrier** | Synchronize threads at barrier | `CyclicBarrier` |
| **Future/Promise** | Async computation results | `CompletableFuture` |

## Key Learnings

- [ ] Thread-safe Singleton implementations
- [ ] Producer-Consumer with BlockingQueue
- [ ] Custom Thread Pool implementation
- [ ] Read-Write Lock usage
- [ ] CompletableFuture patterns
- [ ] Deadlock detection and prevention

## Java Concurrency Essentials

- `synchronized` keyword
- `volatile` keyword
- `java.util.concurrent` package
- `Atomic*` classes
- `Lock` and `Condition`

## Practice Problems

- [ ] Implement thread-safe Singleton (multiple ways)
- [ ] Producer-Consumer problem
- [ ] Dining Philosophers problem
- [ ] Print numbers using multiple threads (odd/even)
- [ ] Custom blocking queue implementation
- [ ] Rate Limiter with Token Bucket

## Interview Questions

1. What is the difference between `synchronized` and `Lock`?
2. How does `volatile` work?
3. Explain deadlock and how to prevent it.
4. What is spurious wakeup?
