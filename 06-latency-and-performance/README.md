# Latency & Performance

Understanding time and efficiency in distributed systems.

---

## Latency Fundamentals

### What is Latency?

**Latency** = Time between request and response (delay)
**Throughput** = Amount of work done per unit time

```
         Request                    Response
Client ───────────▶ Server ──────────▶ Client
         │                              │
         └────── LATENCY (time) ────────┘
```

> 💡 **Key Insight**: Low latency ≠ High throughput. You can have low latency with low throughput (fast but not many), or high latency with high throughput (slow, but many parallel).

### Why Low Latency ≠ High Throughput

**Common Misconception**: "If my API takes 1ms, I can handle 1000 req/sec, right?"

Only true for a **single thread**! The missing factor is **CONCURRENCY**.

```
Throughput = Concurrency / Latency
```

```
Example 1: Fast but Limited
───────────────────────────
Latency = 1ms, Threads = 10
Throughput = 10 / 0.001 = 10,000 req/sec

Example 2: Slow but Highly Concurrent
─────────────────────────────────────
Latency = 100ms, Async connections = 10,000
Throughput = 10,000 / 0.1 = 100,000 req/sec

HIGHER LATENCY but 10x MORE THROUGHPUT!
```

**What Limits Throughput (Not Latency)?**
- Thread/connection pool size
- DB connection pool
- CPU cores
- Memory per request
- Network bandwidth
- OS socket limits

```
          Low Latency                    High Latency
          ┌─────────┐                    ┌─────────┐
          │ 1ms/req │                    │ 100ms   │
          └────┬────┘                    └────┬────┘
               │                              │
    Concurrency: 10                Concurrency: 10,000
               │                              │
               ▼                              ▼
    Throughput: 10K/sec            Throughput: 100K/sec
```

> **Interview Answer**: "Throughput depends on BOTH latency AND concurrency. A system with 100ms latency but 10,000 concurrent connections has higher throughput than a 1ms system with only 10 threads."

---

## Latency Numbers Every Engineer Should Know

### The Table (Memorize This!)

| Operation | Time | Notes |
|-----------|------|-------|
| L1 cache reference | 0.5 ns | CPU cache |
| L2 cache reference | 7 ns | ~14x L1 |
| Main memory (RAM) | 100 ns | ~20x L2 |
| SSD random read | 150 μs | 150,000 ns |
| HDD random read | 10 ms | 10,000,000 ns |
| Network: Same datacenter | 0.5 ms | 500 μs |
| Network: Cross-region | 50-150 ms | Variable |
| Network: Cross-continent | 100-300 ms | Speed of light! |

### Visualization

```
L1 Cache ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 0.5 ns
L2 Cache ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 7 ns
RAM ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 100 ns
SSD ███████████████████████████████████████████ 150,000 ns (150 μs)
HDD ████████████████████████████████████████████████████ 10,000,000 ns (10 ms)
Network (DC)  ████████████ 500,000 ns (0.5 ms)
Network (Region) ██████████████████████████████████████ 100,000,000 ns (100 ms)
```

### CPU Cache Hierarchy

```
8-Core CPU:

  Core 0    Core 1    Core 2  ...  Core 7
  ┌─────┐   ┌─────┐   ┌─────┐     ┌─────┐
  │ L1  │   │ L1  │   │ L1  │     │ L1  │  ← Per core (32-64 KB)
  │ L2  │   │ L2  │   │ L2  │     │ L2  │  ← Per core (256 KB-1 MB)
  └──┬──┘   └──┬──┘   └──┬──┘     └──┬──┘
     └─────────┴─────────┴───...────┘
                    │
             ┌──────▼──────┐
             │   L3 Cache  │  ← SHARED (8-64 MB)
             └──────┬──────┘
                    │
             ┌──────▼──────┐
             │     RAM     │  ← 100 ns
             └─────────────┘
```

### Cache-Friendly Code

**Stack/Heap = Where data lives in RAM. L1/L2/L3 = Automatic copies for fast access.**

```java
// ❌ BAD: Random access (cache misses)
for (i) for (j) sum += matrix[j][i];  // Jumps in memory

// ✅ GOOD: Sequential access (cache hits)
for (i) for (j) sum += matrix[i][j];  // Reads contiguously
```

| Technique | Why It Helps |
|-----------|--------------|
| Sequential access | Uses full cache line (64 bytes) |
| Arrays over LinkedList | Contiguous memory |
| Small objects | Fit in cache |
| Object pooling | Reuse keeps data in cache |

### Practical Implications

```
1 ms = 1,000 μs = 1,000,000 ns

If your service does 10 DB queries at 5ms each:
  Total latency = 50ms (just from DB!)
  
If your service makes 3 cross-region calls at 100ms each:
  Total latency = 300ms minimum (serial)
  
Network is usually THE bottleneck, not CPU!
```

---

## Measuring Latency: Percentiles

### Why Averages Lie

```
Response times: 10, 12, 11, 9, 10, 500, 11, 10, 12, 10 ms
Average: 59.5 ms  ← Misleading!
Median (p50): 10.5 ms ← More representative
p99: 500 ms ← Worst case (matters most!)
```

### Percentile Definitions

| Percentile | Meaning | Use Case |
|------------|---------|----------|
| **p50** | 50% of requests faster than this | Typical experience |
| **p90** | 90% of requests faster | Good indicator |
| **p95** | 95% of requests faster | Most users |
| **p99** | 99% of requests faster | Tail latency |
| **p99.9** | 99.9% of requests faster | Worst case |

```
Latency Distribution:
                          p50   p90  p95    p99   p99.9
                           │     │    │       │      │
Requests ████████████████████████████████████████████████████▓▓▓▓▓▒▒▒░░
                                            ↑
                                    "Long tail" - where problems hide!
```

> 💡 **Interview Tip**: Always discuss p99, not averages. "Our p99 latency is 200ms" shows you understand real-world performance.

---

## Sources of Latency

### Network Latency Components

```
Total Network Latency = Propagation + Transmission + Queueing + Processing
```

### OSI Layer Mapping

| Delay Type | OSI Layer | What Happens | Predictable? |
|------------|-----------|--------------|--------------|
| **Propagation** | L1 Physical | Signal travels through wire/fiber | ✅ Yes (physics) |
| **Transmission** | L1-L2 Physical/Data Link | Bits pushed onto medium | ✅ Yes (bandwidth) |
| **Queueing** | L2-L3 Data Link/Network | Packets wait in buffers | ❌ No (variable!) |
| **Processing** | L2-L4 | Headers parsed, routing decisions | ✅ Mostly |

---

### 1️⃣ Propagation Delay (L1 - Physical)

Signal traveling through the medium at speed of light.

```
Speed in fiber = 200,000 km/s (2/3 speed of light due to refraction)

Formula: Time = Distance / Speed

Example: NY → London
  Distance: 5,500 km
  Time = 5,500 / 200,000 = 0.0275s = 27ms (one way)
  RTT = 54ms minimum
```

| Route | Distance | One-Way | RTT |
|-------|----------|---------|-----|
| Same datacenter | < 1 km | ~5 μs | ~10 μs |
| NY → London | 5,500 km | 27 ms | 54 ms |
| NY → Mumbai | 12,500 km | 62 ms | 125 ms |

> 🔒 **Physics limit**: No optimization can beat speed of light!

---

### 2️⃣ Transmission Delay (L1-L2)

Time to push ALL bits onto the wire.

```
Formula: Time = Data Size (bits) / Bandwidth (bits/sec)

Example: 1 MB on 100 Mbps
  Data: 1 MB = 8 Mb (megabits)
  Time = 8 Mb / 100 Mbps = 0.08s = 80ms
```

| Data | 100 Mbps | 1 Gbps | 10 Gbps |
|------|----------|--------|---------|
| 1 KB | 0.08 ms | 0.008 ms | 0.0008 ms |
| 1 MB | 80 ms | 8 ms | 0.8 ms |
| 1 GB | 80 sec | 8 sec | 0.8 sec |

---

### 3️⃣ Queueing Delay (L2-L3) ⚠️ Variable!

Packets waiting in router/switch buffers when traffic > capacity.

```
Every device has buffers:
  NIC (Network Card) → 256KB-4MB
  Switch (L2)        → 1-100MB
  Router (L3)        → 10MB-1GB
  OS Socket Buffer   → 128KB-16MB

If packets arrive faster than they leave → Queue builds up!
```

**Bufferbloat Problem:**
```
Small buffer: Drop quickly → TCP backs off → Recovers fast
Large buffer: Queue 500ms → TCP doesn't know → Keeps sending → HUGE latency!
```

**Why p99 spikes:**
```
p50: Most requests see 1-5ms queueing
p99: Unlucky requests hit full queues → 100-500ms!
```

---

### 4️⃣ Processing Delay (L2-L4)

Parsing headers, making routing decisions, protocol handshakes.

```
TCP Handshake: 1 RTT
  Client ──SYN──▶ Server
  Client ◀──SYN-ACK── Server
  Client ──ACK──▶ Server

TLS 1.2: 2 RTT (additional key exchange)
TLS 1.3: 1 RTT (optimized)
```

---

### TCP Congestion Control (How TCP Manages Queueing)

```
cwnd (Congestion Window): Max packets "in flight" before ACK needed
RTT (Round Trip Time): Packet + ACK round trip time

Throughput = cwnd / RTT
```

```
SLOW START: cwnd doubles each RTT (1→2→4→8→16...)
PACKET LOSS: cwnd = cwnd/2 (cut in half, back off!)
CONGESTION AVOIDANCE: cwnd grows linearly after threshold
```

When TCP "backs off", unsent data stays in **sender's socket buffer** (not in network).

---

### Summary: What You Can Control

| Component | Can You Control? | How |
|-----------|-----------------|-----|
| Propagation | ❌ No | Use CDN/edge (reduce distance) |
| Transmission | ⚠️ Somewhat | Compress data, smaller payloads |
| Queueing | ❌ No (in network) | Rate limiting, backpressure |
| Processing | ✅ Yes | Tune socket buffers, connection pooling |

### Application Latency

```
┌────────────────────────────────────────────────────────────────┐
│                    REQUEST LIFECYCLE                           │
├────────────────────────────────────────────────────────────────┤
│ 1. DNS Lookup          │  0-100ms (cached: 0ms)               │
│ 2. TCP Handshake       │  1 RTT = 50-150ms                    │
│ 3. TLS Handshake       │  1-2 RTT = 50-300ms                  │
│ 4. Request Transfer    │  Depends on size                     │
│ 5. Server Processing   │  Your code + dependencies            │
│    - Parse request     │  < 1ms                               │
│    - Auth check        │  5-50ms (if external)                │
│    - Business logic    │  Variable                            │
│    - DB queries        │  5-100ms each                        │
│    - External APIs     │  50-500ms each                       │
│ 6. Response Transfer   │  Depends on size                     │
└────────────────────────────────────────────────────────────────┘
```

---

## Performance Optimization Strategies

### 1. Caching (Fastest Win!)

```
┌──────────────────────────────────────────────────────────────┐
│                    CACHING LAYERS                             │
├──────────────────────────────────────────────────────────────┤
│ Browser Cache (0ms)                                          │
│    ↓ miss                                                    │
│ CDN Edge (5-20ms)                                            │
│    ↓ miss                                                    │
│ API Gateway Cache                                            │
│    ↓ miss                                                    │
│ Application Cache - Redis (1-5ms)                            │
│    ↓ miss                                                    │
│ Database Query Cache                                         │
│    ↓ miss                                                    │
│ Database Disk (10-100ms)                                     │
└──────────────────────────────────────────────────────────────┘

Cache hit = 100-1000x faster than origin!
```

### 2. Connection Pooling

```
WITHOUT POOL:
Request → New Connection (TCP+TLS: 150ms) → Query → Close
Request → New Connection (TCP+TLS: 150ms) → Query → Close

WITH POOL:
┌─────────────────────────────────┐
│     CONNECTION POOL             │
│  ┌────┐ ┌────┐ ┌────┐ ┌────┐   │
│  │ C1 │ │ C2 │ │ C3 │ │ C4 │   │  Pre-established!
│  └────┘ └────┘ └────┘ └────┘   │
└─────────────────────────────────┘
Request → Borrow Connection (0ms) → Query → Return

Saves: 150ms per request!
```

### 3. Async & Parallel Processing

```
SEQUENTIAL (Bad):
Task A (100ms) → Task B (100ms) → Task C (100ms) = 300ms total

PARALLEL (Good):
Task A (100ms) ─┐
Task B (100ms) ─┼─▶ Wait for all = 100ms total
Task C (100ms) ─┘

ASYNC (Non-blocking):
Request → Queue Task → Return "accepted" (5ms)
          └── Process in background
```

---

## Deep Dive: Blocking vs Non-Blocking

### What Defines Blocking?

```
BLOCKING:   Thread WAITS and does NOTHING until I/O completes
NON-BLOCKING: Thread starts I/O, continues other work, gets notified later
```

| Blocking Operations | Non-Blocking Alternatives |
|---------------------|---------------------------|
| `RestTemplate` | `WebClient` |
| JDBC | R2DBC |
| `Thread.sleep()` | Scheduler |
| `InputStream` | `AsynchronousChannel` |

---

## Tomcat vs Netty/WebFlux Architecture

### Tomcat (Thread-per-request)

```
Thread Pool: 200 threads

Request 1 ──▶ Thread 1 ──▶ [DB call... waiting 100ms] ──▶ Response
Request 201 ──▶ ??? NO THREADS! QUEUED!

10,000 concurrent requests = 10,000 threads needed!
```

### Netty/WebFlux (Event Loop)

```
Event Loop: 4-8 threads

Request 1 ──▶ Thread 1 ──▶ "Start DB call" ──▶ Thread FREE!
Request 10000 ──▶ Same threads handling all!

When I/O completes: OS notifies ──▶ Any thread picks up response

10,000 concurrent requests = 8 threads enough!
```

**The Magic**: OS kernel (epoll/kqueue) tracks pending I/O, not threads!

```
epoll_wait() → Returns ONLY sockets with data ready
             → Thread never waits, just checks "who's ready?"
```

### CPU Cores vs Threads

```
8 cores, 200 threads (Tomcat):
  - Only 8 threads run TRULY in parallel at any instant
  - Other 192 = context switching (OS rapidly swaps them)
  - Context switch cost: ~1-10 μs each

8 cores, 8 threads (WebFlux):
  - 1 thread per core = perfect match
  - No context switching overhead
  - Each thread busy 100% of time (no waiting)
```

| | Tomcat (200 threads) | WebFlux (8 threads) |
|-|----------------------|---------------------|
| Threads | 200 | 8 (= cores) |
| Memory | 200 MB (1 MB/thread stack) | 8 MB |
| Context switches | High | Minimal |
| 10K connections | Need 10K threads | 8 threads handle all |

### Trade-offs

| | Tomcat | WebFlux/Netty |
|-|--------|---------------|
| Single request latency | Slightly better | Tiny overhead (μs) |
| High load performance | Degrades (thread exhaustion) | Stable |
| Learning curve | Easy | Hard (reactive) |
| Debugging | Simple stack traces | Complex, thread jumps |
| Ecosystem | Mature (JDBC works) | Growing (need R2DBC) |

---

## Networking Fundamentals

### Switch vs Router

| | Switch (L2) | Router (L3) |
|-|-------------|-------------|
| Uses | MAC addresses | IP addresses |
| Scope | Same network (LAN) | Between networks |
| Example | Devices on same floor | Office to internet |

### Buffer Types

| Buffer | Owner | Location | Tunable? |
|--------|-------|----------|----------|
| Socket Buffer | OS Kernel | RAM (per connection) | ✅ setsockopt() |
| NIC Buffer | Network card | Hardware | ❌ No |
| Switch/Router Buffer | Network device | Device memory | ❌ No |

### Backpressure

```
Producer too fast, Consumer slow?

WITHOUT Backpressure: Queue fills → Memory explodes → Crash!
WITH Backpressure: "Slow down!" signal → Producer waits
```

Examples: TCP cwnd, Message queue limits, HTTP 429

### 4. Database Optimization

```
INDEX: B-tree lookup O(log n) vs Full scan O(n)
  1M rows: 20 comparisons vs 1M comparisons

QUERY OPTIMIZATION:
  Bad:  SELECT * FROM orders WHERE user_id = 123
  Good: SELECT id, status FROM orders WHERE user_id = 123 LIMIT 10

N+1 QUERY PROBLEM:
  Bad:  1 query for users + N queries for each user's orders
  Good: 1 query with JOIN or 2 queries with IN clause
```

### 5. Compression

```
┌────────────────────────────────────────────────────────────┐
│ Original: 100 KB                                           │
│ Gzip:     25 KB (75% smaller)                              │
│ Brotli:   20 KB (80% smaller)                              │
├────────────────────────────────────────────────────────────┤
│ Transfer time at 10 Mbps:                                  │
│   100 KB = 80ms                                            │
│   25 KB = 20ms  (60ms saved!)                              │
└────────────────────────────────────────────────────────────┘
```

---

## Tail Latency & Amplification

### The Problem

```
Single Service:
  p99 = 100ms (1% of requests are slow)

Microservices (5 serial calls):
  Probability ALL are fast = 0.99^5 = 95%
  Probability AT LEAST ONE is slow = 5%
  
  Your p99 becomes their p95!
```

### Fan-out Amplification

```
API Gateway fans out to 10 services in parallel:
  Each service p99 = 100ms
  
  Probability ALL 10 respond fast = 0.99^10 = 90%
  10% of requests hit tail latency!
  
  p99 of gateway = ~100ms (one slow service blocks all)
```

### Mitigation Strategies

| Strategy | How |
|----------|-----|
| **Hedged Requests** | Send duplicate request to 2 servers, use first response |
| **Timeouts** | Fail fast instead of waiting forever |
| **Circuit Breaker** | Stop calling failing services |
| **Graceful Degradation** | Return cached/default response |

---

## Throughput vs Latency Trade-off

```
                      ┌─────────────────────────────┐
    Latency           │                             │
       ▲              │                        ╱    │
       │              │                      ╱      │
       │              │                    ╱        │
       │              │       ──────────╱          │
       │              │                             │
       └──────────────┴─────────────────────────────▶
                                          Throughput (Load)

As load increases → Latency increases exponentially near capacity
"Hockey stick" curve - stay below the knee!
```

### Little's Law (Interview Favorite!)

```
L = λ × W

L = Average number of items in system
λ = Arrival rate (requests/second)  
W = Average time in system (latency)

Example:
  1000 requests/sec, each takes 100ms
  L = 1000 × 0.1 = 100 concurrent requests

Capacity Planning:
  Need to handle 10,000 req/s at 50ms latency?
  Need 10,000 × 0.05 = 500 concurrent capacity
```

---

## Amdahl's Law (Parallel Speedup Limit)

```
Speedup = 1 / (S + P/N)

S = Serial portion (cannot parallelize)
P = Parallel portion
N = Number of processors

Example: 90% parallelizable (P=0.9, S=0.1)
  N=2:   Speedup = 1/(0.1 + 0.9/2) = 1.82x
  N=10:  Speedup = 1/(0.1 + 0.9/10) = 5.26x
  N=100: Speedup = 1/(0.1 + 0.9/100) = 9.17x
  N=∞:   Speedup = 1/0.1 = 10x MAX

Even with infinite cores, 10% serial = max 10x speedup!
```

---

## Performance Testing Types

| Type | Purpose | Tool |
|------|---------|------|
| **Load Testing** | Expected load behavior | k6, JMeter |
| **Stress Testing** | Find breaking point | Locust |
| **Spike Testing** | Sudden traffic burst | Gatling |
| **Soak Testing** | Memory leaks over time | ab |
| **Profiling** | Find code bottlenecks | async-profiler, pprof |

---

## Interview Questions (SDE-3 Level)

1. **Latency numbers** - What's faster: RAM access or SSD read?
2. **p99 vs average** - Why do we care about p99?
3. **Tail latency** - How does fan-out amplify latency?
4. **Little's Law** - Calculate concurrent capacity needed
5. **Optimization** - How would you reduce API latency from 500ms to 50ms?
6. **Trade-offs** - Latency vs throughput vs cost
7. **Back-of-envelope** - Estimate if design meets latency SLA

> 💡 **Key Insight**: Performance is about understanding WHERE time goes and optimizing the biggest bottleneck first. Profile before optimizing!
