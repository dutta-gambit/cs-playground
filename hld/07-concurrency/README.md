# Concurrency, Synchronization & Coordination

Understanding concurrent systems for distributed system design.

---

## Fundamentals

### Precise Definitions

| Concept | One-Line Definition | About |
|---------|---------------------|-------|
| **Concurrency** | Tasks that **can** run independently | Structure / Design |
| **Synchronization** | Ensuring **safe** access to shared data | Protection / Coordination |
| **Parallelism** | Tasks that **do** run at the same time | Execution / Hardware |

### How They Relate

```
CONCURRENCY (Design)
     │
     ├──── Introduces SHARED RESOURCES
     │            │
     │            ▼
     │     SYNCHRONIZATION (Protection)
     │     Needed to prevent race conditions
     │
     └──── Enables PARALLELISM (Execution)
           If hardware has multiple cores
```

> 💡 **Key Insight**: Concurrency creates the NEED for synchronization (shared resources). Parallelism is a RESULT of concurrency + multiple cores.

> ⚠️ **Refined Insight**: Synchronization is needed whenever you have **concurrent code with shared resources** - whether running on 1 core (interleaved) or many cores (parallel). Parallel execution makes race conditions FASTER to occur, but they can happen with plain concurrency too!

### What is Interleaved Execution?

```
Interleaved = Tasks take TURNS on a single CPU (time-slicing)

Thread A: ████░░░░████░░░░████
Thread B: ░░░░████░░░░████░░░░
          ─────────────────────▶ Time

CPU runs Thread A for a bit, PAUSES it, switches to Thread B,
then back to Thread A. Like shuffling a deck of cards!

Key point: Even interleaved execution (1 CPU) can cause race conditions
           because context switches can happen MID-OPERATION!
```

---

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

### CPU Scheduling Strategies

| Strategy | How It Works | Use Case |
|----------|--------------|----------|
| **Time Slicing (Round Robin)** | Fixed time per thread, rotate | General purpose OS |
| **Priority Scheduling** | High priority threads run first | Real-time systems |
| **Cooperative** | Thread voluntarily yields CPU | Old MacOS, some embedded |
| **Preemptive** | OS can interrupt any thread | Modern OS (Linux, Windows) |
| **Work Stealing** | Idle cores steal from busy ones | Go runtime, Java ForkJoin |

---

## Execution Types

```
1. SEQUENTIAL (No concurrency)
   Task A: ████████████████████
   Task B:                     ████████████████████
   One thing at a time, in order

2. INTERLEAVED (Concurrent, 1 CPU)
   Task A: ████░░░░████░░░░████
   Task B: ░░░░████░░░░████░░░░
   Tasks take turns (time slicing)

3. PARALLEL (Concurrent, N CPUs)
   Task A: ████████████████████  (CPU 1)
   Task B: ████████████████████  (CPU 2)
   Truly simultaneous

4. PIPELINED (Overlapping stages)
   Task 1: [Stage1][Stage2][Stage3]
   Task 2:         [Stage1][Stage2][Stage3]
   Task 3:                 [Stage1][Stage2][Stage3]
   Different stages run in parallel (like assembly line)

5. ASYNC/EVENT-DRIVEN (Non-blocking I/O)
   Thread:  [Start A][Start B][Start C]...[Finish A][Finish B]
   I/O:              ══════A══════
                         ═══════B═══════
   Thread doesn't wait, handles completions via callbacks
```

### Pipeline: Assembly Line for Data

```
Example: CPU Instruction Pipeline (every modern CPU!)

Stages: Fetch → Decode → Execute → Memory → Writeback

Without pipeline: 1 instruction per 5 cycles
With pipeline: 1 instruction per cycle (5x throughput!)

Real-world uses:
  - Kafka (batch → compress → send → ack)
  - Video streaming (download → decode → render)
  - ETL jobs (extract → transform → load)
```

### Async: Don't Wait for I/O

```
Blocking (1 thread waits):
  Thread: [Call DB]═══WAIT═══[Got result][Call API]═══WAIT═══[Done]
  
Async (1 thread, no wait):
  Thread: [Start DB][Start API][Handle DB result][Handle API result]
  I/O:    ═══DB═══════════
               ═══API════════
  
  I/O happens in parallel. Thread is always busy!

Real-world uses:
  - Node.js (event loop)
  - WebFlux/Netty (reactor pattern)
  - Redis (single-threaded, event-driven)
```

| Type | Focus | When to Use |
|------|-------|-------------|
| **Pipeline** | THROUGHPUT (items/sec) | Data processing, streaming |
| **Async** | EFFICIENCY (no wasted wait) | Many I/O operations |

---

## Network Hardware: NIC Deep Dive

### What is a NIC?

```
NIC = Network Interface Card (hardware connecting server to network)

┌─────────────────────────────────────────────────────────────────────────┐
│                         YOUR SERVER                                      │
├─────────────────────────────────────────────────────────────────────────┤
│  ┌────────┐    ┌────────┐    ┌────────┐                                │
│  │  CPU   │    │  RAM   │    │  NIC   │──────▶ Network Cable/WiFi     │
│  └────────┘    └────────┘    └────────┘                                │
│       └─────────────┴─────────────┘                                    │
│                   PCIe Bus                                              │
└─────────────────────────────────────────────────────────────────────────┘
```

### NIC Internals

```
┌─────────────────────────────────────────────────────────────────────────┐
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐            │
│  │   TX Queue   │────▶│  DMA Engine  │────▶│   PHY Chip   │──▶ Wire   │
│  │  (send ring) │     │   (Copies    │     │  (Physical)  │            │
│  └──────────────┘     │    without   │     └──────────────┘            │
│                       │    CPU!)     │                                  │
│  ┌──────────────┐     │              │     ┌──────────────┐            │
│  │   RX Queue   │◀────│              │◀────│   PHY Chip   │◀── Wire   │
│  │  (recv ring) │     └──────────────┘     └──────────────┘            │
│  └──────────────┘                                                       │
│  MAC Address: Unique hardware ID (e.g., AA:BB:CC:DD:EE:FF)             │
└─────────────────────────────────────────────────────────────────────────┘
```

### Ring Buffers (TX/RX Queues)

```
Ring Buffer = Circular queue for packets

     ┌─────┐
  ┌──│  0  │◀── Write pointer
  │  ├─────┤
  │  │  1  │◀── Data
  │  ├─────┤
  │  │  2  │◀── Read pointer
  │  ├─────┤
  └─▶│  3  │── Wraps to 0!
     └─────┘

TX Ring: OS writes packets → NIC reads and sends
RX Ring: NIC writes received packets → OS reads

Why Ring? Fixed size, no malloc, lock-free, very fast!
Typical size: 256-4096 descriptors
```

### DMA (Direct Memory Access)

```
WITHOUT DMA:
  CPU: Read byte → Write to NIC → Repeat...
  CPU is BUSY copying!

WITH DMA:
  CPU: "NIC, here's the address. Copy it yourself."
  NIC: Reads directly from RAM
  CPU: FREE to do other work!

This is why I/O is "parallel" - NIC works independently!
```

### Interrupts vs Polling

```
INTERRUPTS (Traditional):
  NIC: "INTERRUPT! Packet arrived!"
  CPU: Stop, handle packet
  Problem: 1M packets/sec = 1M interrupts!

POLLING:
  CPU: Keeps checking "Any packets?"
  Problem: Wastes cycles when no traffic

NAPI (Linux Hybrid - Best of both):
  Low traffic → Use interrupts
  High traffic → Switch to polling
```

### NIC Offloading Features

| Feature | What NIC Does |
|---------|---------------|
| Checksum Offload | NIC calculates TCP/IP checksums |
| TSO (TCP Segmentation) | NIC splits large data into packets |
| LRO (Large Receive) | NIC combines small packets into big |
| RSS (Receive Side Scale) | NIC distributes packets across cores |
| VLAN Tagging | NIC handles VLAN headers |

### Packet Flow: Sending

```
App → send() → OS copies to kernel buffer
           → OS writes to TX ring
           → NIC reads via DMA
           → NIC calculates checksum
           → NIC sends signals on wire
           → NIC updates descriptor
```

### Packet Flow: Receiving

```
Wire → NIC receives signals
    → NIC validates checksum
    → NIC writes to RX ring via DMA
    → NIC interrupts CPU (or polled)
    → OS reads from RX ring
    → OS copies to app buffer
    → App's recv() returns
```

### NIC Interview Questions

**Basic:**
1. What is a NIC? What components does it have?
2. What is DMA? Why important for performance?
3. TX vs RX ring buffers - Why ring structure?

**Intermediate:**
4. Interrupts vs Polling - Tradeoffs? What is NAPI?
5. What is RSS? Why matters for multi-core?
6. What happens when RX ring buffer is full? (Drops!)

**Advanced:**
7. What is kernel bypass (DPDK)? When use it?
8. Your server has 100Gbps NIC but 20Gbps actual. Why?
9. How would you debug packet drops?

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

```
A race condition happens when CORRECTNESS depends on 
TIMING or INTERLEAVING of threads/processes.

Same code, same inputs, DIFFERENT outputs depending on thread timing!
```

### Root Causes

```
1. NON-ATOMIC OPERATIONS - What looks like 1 step is multiple steps
2. SHARED MUTABLE STATE - Multiple threads read AND write same data
3. CPU CACHES - Each CPU has its own view of memory
4. INSTRUCTION REORDERING - CPU/compiler changes order for optimization
5. CONTEXT SWITCHES - OS can pause any thread at any instruction
```

---

## Race Condition Types

### Type 1: Read-Modify-Write

```
Three steps that should be atomic but aren't:
  1. READ:   Get current value
  2. MODIFY: Change it
  3. WRITE:  Store new value

If interrupted between steps → DATA CORRUPTION
```

```java
int counter = 0;

// Thread A           // Thread B
counter++;            counter++;

// counter++ breaks into:
Thread A: READ(0)  ADD(1)  WRITE(1)
Thread B:      READ(0)  ADD(1)  WRITE(1)

Both read 0, both write 1 → counter = 1 (not 2!)
```

### Type 2: Check-Then-Act (TOCTOU)

```
TOCTOU = Time Of Check To Time Of Use

  1. CHECK: Is condition true?
  2. ACT:   Do something based on check

If condition CHANGES between check and act → BUG!
```

```java
// Bank account withdrawal
void withdraw(int amount) {
    if (balance >= amount) {      // CHECK
        // --- Thread B withdraws here! ---
        balance = balance - amount;  // ACT - OVERDRAFT!
    }
}

Thread A: balance=100, checks 100>=100? YES
Thread B: balance=100, checks 100>=100? YES, withdraws → balance=0
Thread A: withdraws → balance = -100!  (OVERDRAFT!)
```

### Type 3: Publishing Partially Constructed Objects

```
Making object visible BEFORE fully initialized.
Other threads see HALF-BUILT object!
```

```java
// "instance = new Singleton()" is 3 steps:
1. Allocate memory (address 0x123)
2. Initialize fields
3. Assign reference

CPU can REORDER to: 1 → 3 → 2

Thread A: Allocate → Assign (VISIBLE!) → Init fields...
Thread B: Sees instance != null → Uses it → Fields not set!
```

Fix: Use `volatile` to prevent reordering!

### Type 4: Missed Signals / Lost Wakeups

```
Thread A waits for signal from Thread B.
Thread B sends signal BEFORE Thread A starts waiting.
Thread A waits forever (signal already gone!)
```

```java
// Producer-Consumer
void produce(Object o) {
    item = o;
    notify();  // Signal sent!
}

Object consume() {
    while (item == null) {
        wait();  // Wait for signal
    }
    return item;
}

// If produce() runs BEFORE consume() calls wait():
notify() fires → no one waiting → SIGNAL LOST!
consume() calls wait() → STUCK FOREVER!
```

### Type 5: Unsafe Lazy Initialization

```
Creating object "lazily" without synchronization.
Multiple threads may create separate instances!
```

```java
private static Connection connection;  // SHARED

Connection getConnection() {
    if (connection == null) {           // Thread A checks
        connection = createConnection(); // Thread A creates
    }
    return connection;
}

// Both threads check null at same time:
Thread A: null? YES → creates Connection1
Thread B: null? YES → creates Connection2 (LEAK!)

// Or Thread B gets Thread A's half-initialized connection!
```

### Type 6: Visibility and Memory Ordering

```
Changes by Thread A NOT VISIBLE to Thread B.
Each CPU has its own CACHE, changes may not reach main memory!
```

```java
boolean running = true;  // Not volatile!

// Thread A
while (running) {  // Reads from CPU cache
    doWork();
}

// Thread B  
running = false;   // Writes to ITS cache

// Thread A never sees running=false! Loop forever!
```

Fix: Mark as `volatile` to ensure visibility!

---

### Summary: Race Condition Types

| Type | What Happens | Example |
|------|--------------|---------|
| **Read-Modify-Write** | Multi-step op interrupted | counter++ |
| **Check-Then-Act** | Condition changes mid-check | if(balance>=amt) withdraw |
| **Partial Publication** | Object visible before built | Double-checked locking |
| **Missed Signals** | notify() before wait() | Producer-consumer |
| **Unsafe Lazy Init** | Multiple instances created | Singleton without sync |
| **Visibility** | Changes not seen across threads | Non-volatile flag |

> 💡 **Key Insight**: Race conditions are TIMING BUGS - may work 99% of the time, fail randomly. Prevention is easier than debugging!

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
