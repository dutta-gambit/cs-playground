# Concurrency, Synchronization & Coordination

Understanding concurrent systems for distributed system design.

---

## Fundamentals

### What is Concurrency?

**Concurrency** = Multiple tasks making progress (not necessarily simultaneously)
**Parallelism** = Multiple tasks executing at the exact same instant

```
CONCURRENCY (interleaved):
  Task A: ████░░░░████░░░░████
  Task B: ░░░░████░░░░████░░░░
          ─────────────────────▶ Time
  One CPU, tasks take turns

PARALLELISM (simultaneous):
  Task A: ████████████████████  (CPU 1)
  Task B: ████████████████████  (CPU 2)
          ─────────────────────▶ Time
  Multiple CPUs, truly simultaneous
```

> 💡 **Key Insight**: Concurrency is about *dealing with* many things at once. Parallelism is about *doing* many things at once.

---

## Why Concurrency Matters

### Without Concurrency (Sequential)

```
Request 1 → [Process 100ms] → Response
Request 2 →                   [Process 100ms] → Response
Request 3 →                                     [Process 100ms] → ...
Total: 300ms for 3 requests
```

### With Concurrency, 1 CPU (Interleaved, No Speedup)

```
CPU switches between tasks (time-slicing):

Request 1: ███░░░███░░░███░░░ 
Request 2: ░░░███░░░███░░░███
Request 3: ░░░░░░███░░░███░░░

Total: Still ~300ms (no speedup!)
Benefit: Server doesn't BLOCK on one request
```

### With Concurrency + Parallelism, 3 CPUs (True Speedup!)

```
Core 1 (Request 1): ████████████ 100ms
Core 2 (Request 2): ████████████ 100ms  
Core 3 (Request 3): ████████████ 100ms

Total: ~100ms (3x speedup!)
```

| Scenario | Time | Speedup? |
|----------|------|----------|
| Sequential | 300ms | ❌ |
| Concurrent, 1 CPU | ~300ms | ❌ (interleaved only) |
| Concurrent + Parallel, 3 CPUs | ~100ms | ✅ |

> 💡 **Key Insight**: Concurrency is about CODE DESIGN (tasks can run independently). Parallelism is about HARDWARE (running them simultaneously). Concurrency enables parallelism, but doesn't guarantee it!

---

## Thread Basics

### What is a Thread?

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         PROCESS vs THREAD                               │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  PROCESS:                                                               │
│    - Isolated memory space                                              │
│    - Heavy to create (~10ms, ~10MB)                                     │
│    - Communication via IPC (Inter-Process Communication)                │
│      (pipes, sockets, shared memory, message queues)                    │
│                                                                         │
│  THREAD:                                                                │
│    - Shares memory with other threads in same process                   │
│    - Light to create (~1ms, ~1MB)                                       │
│    - Communication via shared memory (fast but dangerous!)              │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────── PROCESS ───────────────────┐
│                                               │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐        │
│  │ Thread 1│  │ Thread 2│  │ Thread 3│        │
│  │ Stack   │  │ Stack   │  │ Stack   │        │
│  └────┬────┘  └────┬────┘  └────┬────┘        │
│       │            │            │             │
│       └────────────┼────────────┘             │
│                    ▼                          │
│          ┌─────────────────┐                  │
│          │   SHARED HEAP   │ ← All threads    │
│          │   (danger zone!)│   can access     │
│          └─────────────────┘                  │
└───────────────────────────────────────────────┘
```
 
### Clarification: Process Contains Threads

```
✅ CORRECT: One PROCESS has MULTIPLE THREADS
❌ WRONG:   One thread cannot have multiple processes

Process = Container (like a house)
Thread  = Workers inside (like rooms in a house)
```

### Clarification: Can Tomcat Create Child Processes?

```
Running Tomcat: java -jar myapp.jar → 1 JVM Process (PID: 1234)

Can it create child processes? YES!
  - Runtime.getRuntime().exec("script.sh")
  - ProcessBuilder can spawn children

But typically uses THREADS, not child processes (cheaper).
```

### What are File Descriptors?

```
File Descriptor (FD) =  A NUMBER that refers to an open resource

OS manages FD table PER PROCESS:
  FD 0  → stdin  (keyboard)
  FD 1  → stdout (console)
  FD 2  → stderr (errors)
  FD 3  → /var/log/app.log (opened file)
  FD 4  → socket to DB (192.168.1.10:5432)
  FD 5  → socket from client (HTTP connection)
  ...

Why FD? Everything in Unix is a "file" (files, sockets, pipes).
FD is the uniform handle to access them all.

"Too many open files" error?
  ulimit -n shows max FDs (often 1024)
  Each client socket = 1 FD. Close properly!
```

### Clarification: Is Each API Request a Process?

```
NO! Each request is handled by a THREAD (in Tomcat).

❌ WRONG: 1 request = 1 process (10ms + 10MB per request!)
✅ RIGHT: 1 request = 1 thread (0.01ms, reused from pool)

1 Tomcat PROCESS contains:
├── Thread Pool (200 threads)
│   ├── Thread 1:  Handling Request A
│   ├── Thread 2:  Handling Request B
│   └── Thread 3:  Back in pool (waiting)
```

---

## Blocking vs Non-Blocking I/O

### What is Blocking?

```
BLOCKING:   Thread WAITS and does NOTHING until I/O completes
NON-BLOCKING: Thread starts I/O, continues other work, gets notified later
```

### Example: 3 Requests, Each 50ms CPU + 50ms DB Wait

```
BLOCKING (Thread waits during I/O):
─────────────────────────────────────────────────────────────────────
Time:    0────────50────────100────────150
Thread:  [R1 CPU][R1 WAIT ][R2 CPU][R2 WAIT ][R3 CPU][R3 WAIT ]
                    ↑                  ↑  
              Thread stuck!      Thread stuck!
              
CPU Utilization: 50%


NON-BLOCKING (Thread moves on during I/O):
─────────────────────────────────────────────────────────────────────
Time:    0────────50────────100────────150
Thread:  [R1 CPU][R2 CPU][R3 CPU][R1 finish][R2 finish][R3 finish]
                          │
                    All I/O happening in parallel (OS handles it)
                    
CPU Utilization: ~100%
```

### Who Handles I/O?

```
Thread calls socket.read() → BLOCKING syscall!

┌──────────────────────────────────────────────────────────────────────┐
│  Blocking:                                                           │
│    Thread → syscall → OS: "No data? Put thread to SLEEP"            │
│              ↓                                                       │
│    Thread state: RUNNABLE → BLOCKED (not scheduled)                 │
│              ↓                                                       │
│    When data arrives: OS WAKES thread                               │
│                                                                      │
│  Non-blocking:                                                       │
│    Thread → register callback → returns IMMEDIATELY                 │
│    OS (epoll) tracks: "Socket 123 → callback"                       │
│    Data arrives → OS notifies → callback runs                       │
└──────────────────────────────────────────────────────────────────────┘
```


---

## Types of Work

| Type | Thread Doing? | Example | Can WebFlux Help? |
|------|---------------|---------|-------------------|
| **CPU-bound** | Computing | JSON parse, encryption | ❌ No |
| **I/O-bound** | Waiting | DB query, HTTP call | ✅ Yes! |
| **Memory-bound** | RAM access | Large array traversal | ❌ No |
| **Lock-bound** | Waiting for lock | Mutex, DB lock | ❌ No |

> 💡 **When to use WebFlux**: High I/O ratio (many network calls, DB queries). If work is CPU-bound, WebFlux doesn't help!

---

## CPU Cores vs Threads

### Why 200 Threads on 2 Cores Works

```
Misconception: "2 cores = only 2 threads can run"

Reality: At any instant, only 2 threads USE CPU.
         But most threads are SLEEPING (I/O wait)!
         
Typical request: 10ms CPU + 90ms I/O = 10% CPU utilization

At any moment:
  Core 1: [Thread 42 - CPU work]
  Core 2: [Thread 156 - CPU work]
  Thread 1-41, 43-155, 157-200: SLEEPING (waiting for I/O)
  
200 threads but only 2-10 need CPU at any time!
```

### Thread Count Formula

```
Optimal Threads ≈ Cores × (1 + Wait Time / Compute Time)

Example (2 cores, 90ms I/O, 10ms CPU):
  Threads = 2 × (1 + 90/10) = 20

Tomcat defaults to 200 because:
  - Wait times vary wildly
  - Sleeping threads are cheap (just 1MB memory)
```

### When It Breaks (CPU-Heavy Work)

```
90% CPU work, 2 cores, 100 req/sec:

Each request needs: 1.8 sec CPU time
2 cores can provide: 2 CPU-sec / second
Max throughput: 2 / 1.8 = 1.1 req/sec

Sending 100 req/sec → 99% will timeout!
Solution: More cores or optimize code
```

---

## Complete Queue Chain (Network to CPU)

```
Client → [Socket Buffer] → [NIC TX] → Wire → [Router Queue] → 
       → [NIC RX] → [Socket Buffer] → [Accept Queue] → 
       → [Thread Pool Queue] → [CPU Run Queue] → Processing
```

### Key Queues

| Queue | Managed By | Size | When Full |
|-------|------------|------|-----------|
| Socket Send Buffer | OS Kernel | 16KB-4MB | write() blocks |
| NIC Ring Buffer | Hardware | 256-4096 packets | Drops |
| Router Queue | Router | 1MB-1GB | Packet drop! |
| Socket Recv Buffer | OS Kernel | 16KB-16MB | TCP window=0 |
| Accept Queue | OS Kernel | somaxconn | RST or drop |
| Thread Pool Queue | Tomcat | configurable | 503 error |

### TCP Accept Queue Details

```
SYN received → SYN QUEUE (half-open)
ACK received → ACCEPT QUEUE (established, waiting for app)
accept() called → App gets socket

somaxconn = Max size of accept queue (default 128, set higher!)
```

### TCP Backpressure Flow

```
Server slow → Recv buffer fills → TCP window=0 → Client stops sending
              ↑                        ↓
         Backpressure propagates from server to client!
```

---

## CPU Scheduling

### No "Pre-Allocator" - OS Scheduler Handles Everything!

```
Threads are NOT bound to cores by default.
OS scheduler assigns threads to cores dynamically.

Each core has its OWN run queue:
  Core 0: [Thread A, B, C] ← waiting to run
  Core 1: [Thread X, Y]
  Core 2: (empty, idle)

Load balancer: Core 2 idle → "steal" thread from Core 0!
```

### Thread Migration Between Cores

```
Time 0: Thread 5 on Core 1 → Goes to SLEEP (DB call)
Time 50: DB responds → Thread 5 wakes up
         Core 1: BUSY
         Core 2: FREE
         OS: "Thread 5, run on Core 2!"
         
Threads migrate between cores freely!
```

### CPU Affinity (Optional Override)

```
Default: OS moves threads freely (dynamic)
Hard Affinity: Pin thread to specific core (for low-latency)

# Linux: Pin to core 2
taskset -c 2 ./my_program

Use cases: Trading systems, real-time, databases
Warning: Wrong affinity can HURT performance!
```

---

## Network Hardware: NIC Ring Buffers

### What is a Ring Buffer?

```
Ring Buffer = Circular array, indices wrap around

     ┌─────┐
  ┌──│  0  │◀── Write pointer
  │  ├─────┤
  │  │  1  │◀── Data
  │  ├─────┤
  │  │  2  │◀── Read pointer
  │  ├─────┤
  │  │  3  │
  │  ├─────┤
  └─▶│  4  │── Wraps to 0!
     └─────┘

Why Ring? Fixed size, no malloc during operation, lock-free!
```

### NIC Buffers

```
TX Ring: OS puts packets → NIC reads & sends
RX Ring: NIC puts received packets → OS reads

"Put on wire" = Send to physical medium:
  Ethernet: Electrical signals
  Fiber: Light pulses
  WiFi: Radio waves (2.4/5 GHz)
  
All devices doing networking have some form of NIC!
```

---

## TCP RST (Reset)

### What is RST?

```
RST = "ABORT! This connection is invalid!"

Normal close: FIN → ACK → FIN → ACK (graceful)
RST close: RST (immediate, may lose data!)
```

### When RST is Sent

| Scenario | What Happens |
|----------|--------------|
| Accept queue full | Server sends RST |
| Connection to closed port | OS sends RST |
| App crashed | OS sends RST |
| Firewall blocks | May send RST |

---

## The Problem: Race Conditions

### What is a Race Condition?

When outcome depends on unpredictable thread timing.

```java
// SHARED VARIABLE
int counter = 0;

// Thread 1               // Thread 2
counter++;                counter++;

// Expected: counter = 2
// Actual: could be 1 or 2!
```

### Why It Happens

```
counter++ is actually 3 operations:
  1. READ counter (get 0)
  2. ADD 1 (0 + 1 = 1)
  3. WRITE counter (store 1)

Thread 1:  READ(0)  ADD(1)  WRITE(1)
Thread 2:       READ(0)  ADD(1)  WRITE(1)

Both read 0, both write 1 → counter = 1 (not 2!)
```

---

## Solution 1: Mutual Exclusion (Mutex/Lock)

### What is a Lock?

**Lock** = Only one thread can enter the critical section at a time.

```java
Lock lock = new ReentrantLock();
int counter = 0;

// Thread 1               
lock.lock();              
try {                     
    counter++;  // Only I can access!
} finally {
    lock.unlock();
}

// Thread 2 MUST WAIT until Thread 1 unlocks
```

```
Thread 1:  [LOCK]████ counter++ ████[UNLOCK]
Thread 2:       wait...wait...wait [LOCK]████ counter++ ████[UNLOCK]

No overlap → No race condition!
```

### Types of Locks

| Lock Type | Readers | Writers | Use Case |
|-----------|---------|---------|----------|
| **Mutex** | 1 | 1 | General exclusion |
| **ReadWriteLock** | Many | 1 | Read-heavy data |
| **Spinlock** | 1 (busy wait) | 1 | Very short critical sections |
| **Semaphore** | N | N | Limit concurrency (e.g., pool) |

---

## Solution 2: Atomic Operations

### What is Atomic?

**Atomic** = Indivisible operation, cannot be interrupted.

```java
// NOT atomic (3 instructions)
counter++;

// ATOMIC (single CPU instruction)
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();  // One uninterruptible operation!
```

### Compare-And-Swap (CAS)

Foundation of lock-free programming.

```
CAS(memory, expected, newValue):
  IF memory == expected:
    memory = newValue
    return SUCCESS
  ELSE:
    return FAILURE (someone else changed it!)
```

```java
AtomicInteger counter = new AtomicInteger(0);

// Thread tries to update: 0 → 1
boolean success = counter.compareAndSet(0, 1);
// If another thread changed it first, returns false
// Thread retries with new value
```

---

## Solution 3: Thread Confinement

### Don't Share At All!

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    THREAD CONFINEMENT                                    │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  Each thread has its OWN data. No sharing = No race conditions!        │
│                                                                         │
│  Thread 1: [own counter]                                                │
│  Thread 2: [own counter]                                                │
│  Thread 3: [own counter]                                                │
│                                                                         │
│  Final step: Merge results (controlled synchronization)                │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

Examples:
- **ThreadLocal** in Java
- **Actor model** (each actor has private state)
- **Go channels** (share by communicating, not communicate by sharing)

---

## Deadlock

### What is Deadlock?

When threads wait for each other forever.

```
Thread 1: Has Lock A, wants Lock B → WAITING
Thread 2: Has Lock B, wants Lock A → WAITING

Both waiting forever = DEADLOCK!
```

### Four Conditions for Deadlock

| Condition | Meaning |
|-----------|---------|
| **Mutual Exclusion** | Resource can't be shared |
| **Hold and Wait** | Holding one, waiting for another |
| **No Preemption** | Can't forcibly take resource |
| **Circular Wait** | A waits for B, B waits for A |

### Prevention Strategies

```
1. LOCK ORDERING: Always acquire locks in same order
   Thread 1: Lock A → Lock B
   Thread 2: Lock A → Lock B  (not B → A!)

2. TIMEOUT: Give up if can't acquire lock
   if (!lock.tryLock(100, MILLISECONDS)) {
       // Release held locks, retry later
   }

3. DEADLOCK DETECTION: Monitor and kill deadlocked threads
```

---

## Livelock

### What is Livelock?

Threads actively trying to resolve conflict but making no progress.

```
Hallway Problem:
  Person A steps LEFT to avoid B
  Person B steps RIGHT to avoid A
  
  Person A steps RIGHT (still blocked!)
  Person B steps LEFT (still blocked!)
  
  Infinite dance → No progress!
```

Solution: Add randomness (random backoff).

---

## Starvation

### What is Starvation?

A thread never gets access because others keep taking priority.

```
High Priority Thread: Always gets lock
Low Priority Thread: STARVED (never gets turn!)

Solution: Fair locks (FIFO ordering)
Lock lock = new ReentrantLock(true);  // fair = true
```

---

## Synchronization Primitives Summary

| Primitive | Purpose | Blocking? |
|-----------|---------|-----------|
| **Mutex/Lock** | Exclusive access | Yes |
| **Semaphore** | Limit concurrent access (N) | Yes |
| **Condition Variable** | Wait for condition | Yes |
| **Atomic** | Lock-free single variable | No |
| **Barrier** | Wait for all threads to reach point | Yes |
| **CountDownLatch** | Wait for N events | Yes |

---

## Distributed Coordination

### Local vs Distributed Locking

```
LOCAL (single machine):
  Thread 1 ──┐
  Thread 2 ──┼──▶ JVM Lock ──▶ Shared Memory
  Thread 3 ──┘

DISTRIBUTED (multiple machines):
  Server 1 ──┐
  Server 2 ──┼──▶ Distributed Lock (Redis/Zookeeper) ──▶ Shared DB
  Server 3 ──┘
```

### Distributed Lock with Redis

```
SETNX lock_key "owner_id" EX 30  // Set if not exists, 30s timeout

If success: You have the lock!
If fail: Someone else has it, retry

When done:
IF GET lock_key == "owner_id":
    DEL lock_key  // Only owner can release
```

### Leader Election

```
Multiple servers → Only ONE should be leader (write master, cron runner)

Zookeeper: Ephemeral sequential nodes
  /election/node_0001 (Server A)
  /election/node_0002 (Server B)
  
  Lowest number = Leader!
  If leader dies → ephemeral node deleted → next becomes leader
```

---

## Common Concurrency Patterns

### 1. Producer-Consumer

```
┌──────────┐     ┌─────────────┐     ┌──────────┐
│ Producer │────▶│   QUEUE     │────▶│ Consumer │
└──────────┘     └─────────────┘     └──────────┘

Decouples production from consumption.
Queue handles synchronization.
```

### 2. Thread Pool

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         THREAD POOL                                      │
│                                                                         │
│  Task Queue: [T1] [T2] [T3] [T4] [T5] ...                              │
│                    │                                                    │
│         ┌──────────┼──────────┐                                        │
│         ▼          ▼          ▼                                        │
│     [Worker 1] [Worker 2] [Worker 3]                                   │
│                                                                         │
│  Workers pick tasks from queue. Reuse threads (avoid creation cost).   │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3. Read-Write Lock

```
Many readers OR one writer (never both):

  Readers: Can read simultaneously (no conflict)
  Writer:  Exclusive access (blocks all readers and writers)

Good for: Read-heavy caches, config stores
```

### 4. Future/Promise

```java
// Start async work
CompletableFuture<User> future = CompletableFuture
    .supplyAsync(() -> userService.getUser(id));

// Do other work...

// Get result when needed (blocks if not ready)
User user = future.get();
```

---

## Interview Questions (SDE-3 / EM Level)

1. **Race condition** - What is it? How do you prevent it?
2. **Deadlock** - Four conditions? How to prevent/detect?
3. **Atomic vs Lock** - When would you use each?
4. **Thread pool sizing** - How do you size for CPU-bound vs I/O-bound?
5. **Distributed lock** - How would you implement? What happens on failure?
6. **Leader election** - Why needed? How does it work?
7. **Producer-Consumer** - Design a rate limiter using this pattern
8. **Blocking vs Non-blocking** - What's the difference? When use each?
9. **200 threads on 2 cores** - Why does this work for web apps?
10. **Request queue chain** - Trace a request from NIC to CPU
11. **TCP backpressure** - How does it work? What queues are involved?
12. **CPU affinity** - What is it? When would you use it?

> 💡 **Key Insight**: Concurrency bugs are the hardest to find because they're non-deterministic. Design for simplicity first – avoid shared mutable state whenever possible!
