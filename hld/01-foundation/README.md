# 01 - Foundation

This module covers the foundational concepts of system design.

## Chapters Covered
- Chapter 1: Introduction to System Design
- Chapter 4: Key Characteristics of Distributed Systems
- Chapter 5: Network Essentials
- Chapter 6: Domain Name System (DNS)

## Project: User Service with Monitoring

A basic Spring Boot REST API with:
- CRUD operations for User entity
- MySQL database connection
- Actuator endpoints for health/metrics
- Prometheus + Grafana integration

## Getting Started

```bash
# Start infrastructure
docker-compose up -d

# Run the service
cd user-service
mvn spring-boot:run
```

## Key Learnings

- [ ] Spring Boot project setup
- [ ] JPA/Hibernate with MySQL
- [ ] Actuator endpoints
- [ ] Prometheus metrics
- [ ] Grafana dashboards
