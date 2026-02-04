# Availability

Measure of how accessible and operational a system is to its users over time.

## The Nines Table (Know This!)

| Availability | Downtime/Year | Downtime/Month | Downtime/Day |
|--------------|---------------|----------------|--------------|
| 99% (two 9s) | 3.65 days | 7.3 hours | 14.4 min |
| 99.9% (three 9s) | 8.76 hours | 43.8 min | 1.44 min |
| 99.99% (four 9s) | 52.56 min | 4.38 min | 8.64 sec |
| 99.999% (five 9s) | 5.26 min | 25.9 sec | 864 ms |

> 💡 **Interview Tip**: Most production systems target 99.9% - 99.99%. Five 9s is extremely expensive and often unnecessary.

---

## Availability vs Reliability vs Durability

| Concept | Definition | Example |
|---------|------------|---------|
| **Availability** | System is UP and responding | "Can users access the service right now?" |
| **Reliability** | System works CORRECTLY over time | "Does it produce correct results without failures?" |
| **Durability** | Data is NOT LOST | "Will my data survive a disk failure?" |

```
A system can be:
- Available but Unreliable: Returns errors but responds (200 OK with wrong data)
- Reliable but Unavailable: Works correctly when up, but has frequent outages
- Available & Reliable but Not Durable: Works great, but loses data on failure
```

---

## Calculating Availability (Interview Essential!)

### Single Component
```
Availability = MTBF / (MTBF + MTTR)

MTBF = Mean Time Between Failures
MTTR = Mean Time To Repair/Recovery

Example: Server fails once per month (MTBF=720hr), takes 1hr to recover (MTTR=1hr)
Availability = 720 / (720 + 1) = 99.86%
```

### Components in Series (All Must Work)
```
A ──→ B ──→ C

Total = A × B × C

Example: 99.9% × 99.9% × 99.9% = 99.7%
(Each component in series REDUCES overall availability)
```

### Components in Parallel (Redundancy)
```
    ┌── A ──┐
────┤       ├────
    └── A' ─┘

Total = 1 - (failure_A × failure_A')
      = 1 - (0.001 × 0.001) = 99.9999%

(Redundancy INCREASES availability!)
```

---

## Strategies for High Availability

### 1. Eliminate Single Points of Failure (SPOF)

```
BAD (Single DB):                    GOOD (Replicated):
┌────────┐                          ┌────────┐
│  App   │                          │  App   │
└───┬────┘                          └───┬────┘
    │                                   │
    ▼                               ┌───┴───┐
┌────────┐                          ▼       ▼
│   DB   │ ← SPOF!             ┌────────┐ ┌────────┐
└────────┘                     │ Master │ │Replica │
                               └────────┘ └────────┘
```

### 2. Redundancy Patterns

| Pattern | How It Works | Use Case |
|---------|--------------|----------|
| **Active-Passive** | Standby takes over on failure | Databases, stateful services |
| **Active-Active** | All nodes serve traffic | Stateless services, web servers |
| **N+1 Redundancy** | N needed, 1 spare | Cost-effective buffer |
| **N+2 Redundancy** | N needed, 2 spares | Critical systems |

```
Active-Passive:
┌──────────┐     ┌──────────┐
│  ACTIVE  │     │ PASSIVE  │
│ (serving)│────▶│(standby) │
└──────────┘     └──────────┘
     │                │
     │    Heartbeat   │
     └───────────────-┘

On failure: Passive promotes to Active (failover)


Active-Active:
┌──────────┐     ┌──────────┐
│ ACTIVE 1 │     │ ACTIVE 2 │
│ (serving)│     │ (serving)│
└──────────┘     └──────────┘
     │                │
     └───────┬────────┘
             ▼
     ┌──────────────┐
     │ Load Balancer│
     └──────────────┘

Both serve traffic. On failure: LB routes to healthy node.
```

### 3. Failover Strategies

| Type | RTO | Data Loss | Complexity |
|------|-----|-----------|------------|
| **Cold Standby** | Hours | Possible | Low |
| **Warm Standby** | Minutes | Minimal | Medium |
| **Hot Standby** | Seconds | None | High |

> **RTO** = Recovery Time Objective (how fast to recover)
> **RPO** = Recovery Point Objective (how much data loss acceptable)

### 4. Health Checks & Circuit Breakers

```java
// Circuit Breaker Pattern (Interview favorite!)
public class CircuitBreaker {
    enum State { CLOSED, OPEN, HALF_OPEN }
    
    // CLOSED: Normal operation
    // OPEN: Failing fast, not calling downstream
    // HALF_OPEN: Testing if service recovered
    
    public Response call(Request req) {
        if (state == OPEN && !cooldownExpired()) {
            return fallbackResponse();  // Fail fast
        }
        try {
            Response resp = downstream.call(req);
            onSuccess();
            return resp;
        } catch (Exception e) {
            onFailure();
            return fallbackResponse();
        }
    }
}
```

### 5. Graceful Degradation

```
Full Service:     Degraded Mode:      Minimal Mode:
┌────────────┐    ┌────────────┐      ┌────────────┐
│ • Search   │    │ • Search   │      │ • Search   │
│ • Recommend│    │ • Recommend│ (off)│   (cached) │
│ • Reviews  │    │ • Reviews  │      │            │
│ • Live Chat│    │   (cached) │      │            │
└────────────┘    └────────────┘      └────────────┘

Shed non-essential features to keep core working.
```

---

## Distributed Systems Availability

### CAP Theorem (Interview Must-Know!)

```
       Consistency
           /\
          /  \
         /    \
        /  CP  \    CA = Single node only (not distributed)
       /________\   CP = Consistent + Partition tolerant
      /    AP    \  AP = Available + Partition tolerant
     /______________\
Availability    Partition Tolerance

During network partition, choose:
- CP: Reject requests to stay consistent (e.g., Banking)
- AP: Serve stale data to stay available (e.g., Social media feeds)
```

### PACELC Theorem (Advanced)

```
If Partition:
  Choose Availability or Consistency (CAP)
Else (normal operation):
  Choose Latency or Consistency

Example: DynamoDB = PA/EL (Available during partition, Low latency normally)
Example: MongoDB = PC/EC (Consistent always)
```

---

## Real-World Architectures

### Multi-Region Active-Active

```
┌─────────────────────┐       ┌─────────────────────┐
│      REGION A       │       │      REGION B       │
│  ┌───────────────┐  │       │  ┌───────────────┐  │
│  │   App Tier    │  │       │  │   App Tier    │  │
│  └───────────────┘  │       │  └───────────────┘  │
│  ┌───────────────┐  │       │  ┌───────────────┐  │
│  │   Database    │◀─┼──────▶│──│   Database    │  │
│  └───────────────┘  │ Sync  │  └───────────────┘  │
└─────────────────────┘       └─────────────────────┘
           │                            │
           └──────────────┬─────────────┘
                          ▼
                   ┌──────────────┐
                   │  Global DNS  │
                   │  (Route 53)  │
                   └──────────────┘
                          ▲
                          │
                       Users
```

---

## Interview Questions (SDE-3 Level)

1. **Design for 99.99% availability** - How would you architect it?
2. **Failover trade-offs** - Active-Active vs Active-Passive?
3. **Calculate availability** - Given component reliabilities, find system availability
4. **CAP during partition** - Which would you choose for a payment system?
5. **Graceful degradation** - How would you degrade an e-commerce site?
6. **Blast radius** - How to limit impact of failures?
7. **Chaos engineering** - How to test availability?

> 💡 **Key Insight**: High availability is about DESIGN choices, not just adding servers. Think: redundancy, fault isolation, fast detection, automatic recovery.