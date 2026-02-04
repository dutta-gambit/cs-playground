# Scalability

Ability of a system to handle increasing workload by:
- **Scaling Out** (Horizontal) - Adding more resources
- **Scaling Up** (Vertical) - Upgrading capacity of existing resources


## Types of Scalability

### Vertical Scalability (Scale Up)
- Increasing the capacity of existing resources
- Example: Adding more RAM, CPU, or storage to a server
- ✅ Advantage: Simple to implement
- ❌ Disadvantage: Limited by max capacity of single server, often requires downtime

### Horizontal Scalability (Scale Out)
- Adding more resources/nodes to the system
- Example: Adding more servers to a cluster
- ✅ Advantage: Can handle massive workloads, no single point of failure
- ❌ Disadvantage: More complex to implement


## Database Scaling: The Nuance

| Database | Horizontal Scaling | Why |
|----------|-------------------|-----|
| **MongoDB/Cassandra** | ✅ Easy (built-in sharding) | Designed for distributed, eventual consistency |
| **MySQL** | ⚠️ Possible but complex | ACID + JOINs + Foreign Keys need coordination |

### Why NoSQL Scales Easier

```
MongoDB/Cassandra:
┌────────┐ ┌────────┐ ┌────────┐
│ Node 1 │ │ Node 2 │ │ Node 3 │   ← Data auto-sharded
│ Data A │ │ Data B │ │ Data C │   ← Add node = auto-rebalance
└────────┘ └────────┘ └────────┘
```

### Why MySQL Horizontal Scaling is Hard

```sql
-- This query needs ALL data in one place:
SELECT orders.*, users.name, products.title
FROM orders
JOIN users ON orders.user_id = users.id
JOIN products ON orders.product_id = products.id;

-- If data is on different servers, JOINs become very complex!
```

### MySQL Horizontal Scaling Options

| Method | How It Works |
|--------|--------------|
| **Read Replicas** | Writes → Master, Reads → Replicas |
| **Manual Sharding** | Split by key (user_id % N) |
| **Vitess** | Google's sharding solution (used by YouTube, Slack) |
| **PlanetScale** | Managed Vitess |
| **MySQL Cluster (NDB)** | Shared-nothing architecture |

```
Read Replicas Pattern:
                    ┌──────────────┐
        Writes ────→│    Master    │
                    └──────┬───────┘
                           │ Replication
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
     ┌──────────┐    ┌──────────┐    ┌──────────┐
     │ Replica  │    │ Replica  │    │ Replica  │
     └──────────┘    └──────────┘    └──────────┘
                      ↑ Reads

Limitation: Writes still bottleneck at single Master
```

> 💡 **Key Insight**: MySQL *can* scale horizontally, but requires extra tooling. NoSQL databases have horizontal scaling built into their architecture from day one.
