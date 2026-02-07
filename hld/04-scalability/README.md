# Scalability

Ability of a system to handle increasing workload while maintaining performance.

---

## Types of Scaling

### Vertical Scaling (Scale Up)

```
Before:                     After:
┌────────────────────┐      ┌────────────────────┐
│    4 CPU, 16GB     │  →   │   32 CPU, 256GB    │
│    Single Server   │      │   Bigger Server    │
└────────────────────┘      └────────────────────┘
```

| Pros | Cons |
|------|------|
| Simple - no code changes | Hardware limits (max RAM/CPU exists) |
| No distributed complexity | Single point of failure |
| Strong consistency easy | Expensive at high end |
| | Often requires downtime |

### Horizontal Scaling (Scale Out)

```
Before:                     After:
┌────────────────────┐      ┌──────┐ ┌──────┐ ┌──────┐
│    Single Server   │  →   │ Srv1 │ │ Srv2 │ │ Srv3 │
└────────────────────┘      └──────┘ └──────┘ └──────┘
                                   ↑
                            ┌──────────────┐
                            │ Load Balancer│
                            └──────────────┘
```

| Pros | Cons |
|------|------|
| Near-infinite scaling | Distributed system complexity |
| Fault tolerant | Data consistency challenges |
| Cost-effective (commodity hardware) | Stateless requirement |
| No downtime for scaling | Network overhead |

---

## Stateless vs Stateful Services

### Stateless (Easy to Scale)

```
┌────────┐     ┌──────────────┐     ┌─────────┐
│ Client │────▶│ Load Balancer│────▶│ Server 1│ ─┐
└────────┘     └──────────────┘     ├─────────┤  │  ┌───────────┐
                                    │ Server 2│ ─┼─▶│ Shared DB │
                                    ├─────────┤  │  └───────────┘
                                    │ Server 3│ ─┘
                                    └─────────┘

Any server can handle any request - no local state!
Session stored externally (Redis, DB)
```

### Stateful (Hard to Scale)

```
┌────────┐          ┌─────────┐
│ Client │─────────▶│ Server 1│ ← Session state HERE
└────────┘          └─────────┘

Must ALWAYS go to same server (sticky sessions)
Scaling = complex state migration
```

> 💡 **Interview Tip**: Always design for stateless. Store state in Redis/DB.

---

## Database Scaling (Interview Favorite!)

### Read Replicas

```
                    ┌──────────────┐
     ALL WRITES ───▶│    Master    │
                    └──────┬───────┘
                           │ Async Replication
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
     ┌──────────┐    ┌──────────┐    ┌──────────┐
     │ Replica  │    │ Replica  │    │ Replica  │
     └──────────┘    └──────────┘    └──────────┘
           ↑               ↑               ↑
           └───────────────┴───────────────┘
                   ALL READS

Works when: Read-heavy workload (80%+ reads)
Problem: Write bottleneck still exists
```

### Sharding (Horizontal Partitioning)

```
┌─────────────────────────────────────────────────┐
│                  Shard Router                    │
└─────────────────────────────────────────────────┘
         │              │              │
         ▼              ▼              ▼
   ┌──────────┐   ┌──────────┐   ┌──────────┐
   │ Shard 0  │   │ Shard 1  │   │ Shard 2  │
   │ Users    │   │ Users    │   │ Users    │
   │ A-H      │   │ I-P      │   │ Q-Z      │
   └──────────┘   └──────────┘   └──────────┘
```

#### Sharding Strategies

| Strategy | How | Pros | Cons |
|----------|-----|------|------|
| **Range-based** | user_id 1-1M → Shard1 | Simple, range queries | Hotspots possible |
| **Hash-based** | hash(user_id) % N | Even distribution | Hard to add shards |
| **Directory-based** | Lookup table | Flexible | Lookup = bottleneck |
| **Geo-based** | Region → Shard | Data locality | Uneven if regions differ |

#### Consistent Hashing (Interview Must-Know!)

```
Regular hashing problem:
  hash(key) % 3 → Node 0, 1, or 2
  Add Node 3: hash(key) % 4 → EVERYTHING REMAPS! 💀

Consistent Hashing:
  Nodes on a ring, keys map to nearest node clockwise
  Add Node 3: Only keys between Node2 and Node3 move ✅

      Node0
        │
    ────┼────
   ╱         ╲
Node3         Node1
   ╲         ╱
    ────┬────
        │
      Node2
```

### SQL vs NoSQL Scaling

| | SQL (MySQL, PostgreSQL) | NoSQL (MongoDB, Cassandra) |
|-|-------------------------|---------------------------|
| **Horizontal Scaling** | Hard (need Vitess, Citus) | Built-in |
| **Sharding** | Manual, complex | Automatic |
| **JOINs across shards** | Very difficult | N/A (denormalized) |
| **Strong Consistency** | Default | Optional (eventual) |
| **Use When** | Complex queries, ACID | Simple queries, massive scale |

---

## Caching Strategies

### Cache Patterns

```
1. CACHE-ASIDE (Lazy Loading):
   ┌────────┐  Miss  ┌────────┐        ┌────────┐
   │  App   │───────▶│ Cache  │        │   DB   │
   │        │        └────────┘        └────────┘
   │        │────────────────────────────▶│
   │        │◀────────────────────────────│ Read
   │        │────────▶│ Set Cache        │
   └────────┘        └────────┘        └────────┘

2. WRITE-THROUGH:
   ┌────────┐        ┌────────┐        ┌────────┐
   │  App   │───────▶│ Cache  │───────▶│   DB   │
   └────────┘  Write └────────┘  Write └────────┘
   (Cache and DB always in sync)

3. WRITE-BEHIND (Write-Back):
   ┌────────┐        ┌────────┐   Async ┌────────┐
   │  App   │───────▶│ Cache  │ ──────▶│   DB   │
   └────────┘  Write └────────┘  Later  └────────┘
   (Fast writes, eventual consistency)
```

### Cache Eviction Policies

| Policy | How | Use Case |
|--------|-----|----------|
| **LRU** | Least Recently Used | General purpose |
| **LFU** | Least Frequently Used | Popular content |
| **FIFO** | First In First Out | Time-based |
| **TTL** | Time To Live | Expiring data |

---

## Async Processing & Queues

```
Synchronous (Blocking):
┌────────┐     ┌────────┐     ┌────────┐
│ Client │────▶│  API   │────▶│ Heavy  │──── Wait 30s ────▶ Response
└────────┘     └────────┘     │ Task   │
                              └────────┘

Asynchronous (Non-blocking):
┌────────┐     ┌────────┐     ┌────────┐     ┌────────┐
│ Client │────▶│  API   │────▶│ Queue  │     │ Worker │
└────────┘     └────────┘     └────────┘     └────────┘
      ▲             │              │              │
      │             ▼              ▼              ▼
      │        "Job queued"   Process async   Complete
      └──────── Immediate ◀─────────────────────────
                Response

Technologies: Kafka, RabbitMQ, SQS, Redis Streams
```

---

## CDN (Content Delivery Network)

```
Without CDN:
User in India ──────────────────────────▶ Server in USA
              (300ms latency × many requests = slow!)

With CDN:
User in India ──▶ Edge Server ──cache hit──▶ Response (20ms!)
                   Mumbai     
                      │
                      │ cache miss
                      ▼
                 Origin Server (USA)
```

---

## Scaling Metrics to Know

| Metric | What It Measures |
|--------|------------------|
| **RPS/QPS** | Requests/Queries per second |
| **Throughput** | Data processed per second |
| **Latency (p50/p99)** | Response time at percentiles |
| **Concurrent Users** | Simultaneous active users |
| **Error Rate** | % of failed requests |

### Back-of-Envelope Calculations (Interview Essential!)

```
Estimate Twitter's tweet storage:
- 500M users, 100M daily active
- 20% tweet daily = 20M tweets/day
- Avg tweet = 300 bytes (text + metadata)
- Daily: 20M × 300B = 6GB/day
- Yearly: 6GB × 365 = 2.2TB/year
- 5 years: ~11TB (just text!)

Estimate QPS for a service:
- 100M daily users
- 10 requests/user/day
- 100M × 10 = 1B requests/day
- 1B / 86,400 sec = ~11,500 QPS average
- Peak (3x) = ~35,000 QPS
```

---

## Scalability Anti-Patterns

| Anti-Pattern | Problem | Solution |
|--------------|---------|----------|
| **Monolith everything** | Can't scale parts independently | Microservices |
| **Synchronous calls** | Chain of blocking calls | Async + queues |
| **Shared mutable state** | Contention, locks | Stateless + external store |
| **Single DB for all** | Bottleneck | Read replicas, sharding |
| **No caching** | DB overload | Multi-layer cache |

---

## Interview Questions (SDE-3 Level)

1. **Design for 100x traffic** - How would you scale an existing system?
2. **Sharding key selection** - What key would you choose for an e-commerce orders table?
3. **Cache stampede** - What happens when cache expires and 1M requests hit DB?
4. **Consistent hashing** - Explain and when to use?
5. **Stateful to Stateless** - How would you migrate a stateful service?
6. **Write-heavy workload** - How to scale when 80% writes?
7. **Global scale** - How to serve users across continents?

> 💡 **Key Insight**: Scalability is about making good TRADE-OFFS. Know what you're giving up for scale (consistency, complexity, cost).
