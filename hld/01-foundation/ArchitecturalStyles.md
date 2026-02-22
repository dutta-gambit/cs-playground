# Architectural Styles

> Core patterns for structuring systems. Key goal: **High Cohesion + Low Coupling**.

---

## 🔑 Cohesion & Coupling

### Cohesion = how focused a module is internally

```
High Cohesion (GOOD):  UserAuthService → login(), logout(), resetPassword()
Low Cohesion (BAD):    UtilService → login(), sendEmail(), generatePDF()
```

### Coupling = how dependent modules are on each other

```
Low Coupling (GOOD):   Module A calls B through an interface, doesn't know internals
High Coupling (BAD):   Module A directly accesses B's private fields/DB
```

> In practice they are **inversely proportional** — well-designed systems have high cohesion + low coupling.

---

## 📐 The 7 Architectural Styles

### 1. Layered (MVC, 3-tier)

```
┌──────────────┐
│ Presentation │  ← UI/Controllers
├──────────────┤
│  Business    │  ← Logic/Services
├──────────────┤
│    Data      │  ← Repository/DB
└──────────────┘
Each layer ONLY talks to the layer below it.
```

- **Example:** Spring Boot → Controller → Service → Repository
- **Cohesion:** ✅ High | **Coupling:** 🟡 Medium (layers depend on adjacent layers)

---

### 2. Client-Server

```
┌────────┐         ┌────────┐
│ Client │ ──HTTP──→│ Server │
│(Browser)│←──JSON──│ (API)  │
└────────┘         └────────┘
```

- **Example:** React frontend ↔ Spring Boot API
- **Cohesion:** ✅ High | **Coupling:** ✅ Low (API contract is the only dependency)

---

### 3. Primary-Replica (Master-Slave)

```
┌──────────┐                  ┌──────────┐
│ Primary  │ ──replicates──→  │ Replica 1│ (reads)
│ (writes) │ ──replicates──→  │ Replica 2│ (reads)
└──────────┘                  └──────────┘
```

- **Example:** MySQL primary + read replicas, Redis cluster
- **Cohesion:** ✅ High | **Coupling:** 🟡 Medium (replicas depend on primary for sync)

---

### 4. Pipe & Filter

```
Input → [Filter A] → [Filter B] → [Filter C] → Output
         (parse)      (transform)   (validate)
```

- **Example:** Unix `cat | grep | sort`, Kafka streams, ETL pipelines
- **Cohesion:** ✅ Very high | **Coupling:** ✅ Very low (filters only know input/output format)

---

### 5. Microservices

```
┌─────────┐   ┌─────────┐   ┌─────────┐
│  User   │   │  Order  │   │ Payment │
│ Service │──→│ Service │──→│ Service │
└─────────┘   └─────────┘   └─────────┘
Each: own DB, own deployment, own team
```

- **Example:** Netflix, Uber, Amazon
- **Cohesion:** ✅ Very high | **Coupling:** ✅ Low (but can become high if services share DBs!)

---

### 6. Event-Driven / Pub-Sub

```
Producer → [Event Bus/Kafka] → Consumer 1
                              → Consumer 2
                              → Consumer 3
Producers don't know consumers. Consumers don't know producers.
```

- **Example:** Order placed → Notification, Inventory, Analytics all react
- **Cohesion:** ✅ Very high | **Coupling:** ✅ Very low (fully decoupled)

---

### 7. Peer-to-Peer

```
┌──────┐ ←→ ┌──────┐
│Node A│ ←→ │Node B│
└──────┘     └──────┘
    ↕           ↕
┌──────┐ ←→ ┌──────┐
│Node C│ ←→ │Node D│
└──────┘    └──────┘
No central server. Everyone is equal.
```

- **Example:** BitTorrent, Blockchain
- **Cohesion:** 🟡 Medium | **Coupling:** ✅ Low (nodes are autonomous)

---

## 📊 Comparison

| Style | Cohesion | Coupling | Best for |
|-------|----------|----------|----------|
| Layered | ✅ High | 🟡 Medium | Monolithic apps, CRUD |
| Client-Server | ✅ High | ✅ Low | Web apps, mobile apps |
| Primary-Replica | ✅ High | 🟡 Medium | Read-heavy databases |
| Pipe & Filter | ✅ Very high | ✅ Very low | Data processing, ETL |
| Microservices | ✅ Very high | ✅ Low* | Large-scale systems |
| Event-Driven | ✅ Very high | ✅ Very low | Async, reactive systems |
| Peer-to-Peer | 🟡 Medium | ✅ Low | Decentralized systems |

*Microservices coupling can become high if not designed carefully (shared DBs, synchronous calls)

> 🧠 **Interview:** "I want high cohesion and low coupling. Pipe & Filter and Event-Driven give the best decoupling. Layered is simplest but has medium coupling between layers."
