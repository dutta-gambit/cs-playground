# 🚀 System Design Playground

A hands-on learning repository for mastering system design concepts with **Java, Spring Boot, and MySQL**.

Based on the [Grokking System Design Fundamentals](https://www.designgurus.io/course/grokking-system-design-fundamentals) course.

---

## 📁 Project Structure

```
system_design/
├── 01-foundation/              # Intro, monitoring, networking basics
├── 02-load-balancing/          # NGINX, load balancing algorithms
├── 03-api-gateway/             # Spring Cloud Gateway, rate limiting
├── 04-caching/                 # Redis, Caffeine, cache strategies
├── 05-data-partitioning/       # Sharding, consistent hashing
├── 06-replication/             # Master-slave, read-write splitting
├── 07-databases/               # Polyglot persistence (SQL + NoSQL)
├── 08-distributed-patterns/    # Bloom filters, quorum, leader election
├── 09-realtime/                # WebSocket, SSE, long-polling
├── 10-security/                # JWT, OAuth2, RBAC
├── 11-messaging/               # Kafka, event-driven architecture
├── 12-file-systems/            # Distributed file storage
├── capstone-ecommerce/         # Final integration project
└── docs/                       # Notes and learnings
```

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17+ |
| Framework | Spring Boot 3.x |
| Database | MySQL 8.x |
| Cache | Redis |
| Message Queue | Apache Kafka |
| Containerization | Docker & Docker Compose |
| Build Tool | Maven |

---

## 📚 Learning Progress

| Module | Status | Key Concepts |
|--------|--------|--------------|
| 01-foundation | ⬜ | Actuator, Prometheus, Grafana |
| 02-load-balancing | ⬜ | NGINX, Round Robin, Health Checks |
| 03-api-gateway | ⬜ | Rate Limiting, JWT, Circuit Breaker |
| 04-caching | ⬜ | Cache-Aside, Write-Through, LRU |
| 05-data-partitioning | ⬜ | Hash Sharding, Consistent Hashing |
| 06-replication | ⬜ | Master-Slave, Read-Write Split |
| 07-databases | ⬜ | ACID vs BASE, Polyglot Persistence |
| 08-distributed-patterns | ⬜ | Bloom Filter, Quorum, Heartbeat |
| 09-realtime | ⬜ | WebSocket, SSE, Long-Polling |
| 10-security | ⬜ | OAuth2, JWT, RBAC |
| 11-messaging | ⬜ | Kafka, Pub/Sub, DLQ |
| 12-file-systems | ⬜ | Chunking, Replication |
| capstone | ⬜ | Full Integration |

**Legend:** ⬜ Not Started | 🟡 In Progress | ✅ Completed

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.x (or use Docker)

### Run a Module
```bash
cd 01-foundation/user-service
mvn spring-boot:run
```

### Run with Docker Compose
```bash
cd 02-load-balancing
docker-compose up -d
```

---

## 📖 Resources

- [Grokking System Design Fundamentals](https://www.designgurus.io/course/grokking-system-design-fundamentals)
- [System Design Primer](https://github.com/donnemartin/system-design-primer)
- [Spring Boot Guides](https://spring.io/guides)

---

Happy Learning! 🎉
