# HLD Prep Progress

## Target Companies
Meta, Google, Apple, Intuit, Adobe, InMobi, OpenAI, Anthropic

---

## Phase A: Building Blocks (Concepts)

### 1. Networking Fundamentals — IN PROGRESS
- Architectural styles covered (layered, client-server, primary-replica, pipe & filter, microservices, event-driven, P2P)
- Foundation module started (DNS, network essentials, key characteristics of distributed systems)
- **Gaps:** Load balancers (L4 vs L7), CDN, reverse proxy — need depth
- **Status:** Started, needs structured completion.

### 2. Databases — NOT STARTED
- Has production MariaDB experience at Vidyut — not a blank slate
- **Status:** Needs structured HLD-style coverage (sharding, replication, partitioning, CAP)

### 3. Caching — NOT STARTED
### 4. Message Queues & Event Streaming — NOT STARTED
### 5. Storage — NOT STARTED
### 6. API Design — NOT STARTED
### 7. Consistency & Availability — NOT STARTED
### 8. Scaling Patterns — NOT STARTED
### 9. Reliability — NOT STARTED
### 10. Observability — NOT STARTED
- Has New Relic production experience — strong baseline
### 11. Security Basics — NOT STARTED
### 12. Back-of-Envelope Estimation — NOT STARTED

---

## Phase B: Classic HLD Problems

### 1. URL Shortener — COMPLETED (first pass)
- Full interview simulation done
- Requirements, estimation, entity design, API design, architecture all covered
- Design notes saved at `hld_practice/url_shortner/DESIGN_NOTES.md`
- Java project scaffold set up for implementation practice
- **Weak spots:** back-of-envelope estimation needed guidance, no failure mode discussion, DB choice not justified, no multi-region/scaling discussion
- **Strong spots:** caught hidden write problem (last_accessed_at), 302 vs 301 reasoning, KGS approach, async update pattern, cache sizing, LRU + TTL justification
- **Status:** Design done. Code implementation pending.

### 2. Paste Tool (Pastebin) — NOT STARTED
### 3. Rate Limiter — NOT STARTED
### 4. Consistent Hashing — NOT STARTED
### 5. Key-Value Store — NOT STARTED
### 6. Unique ID Generator (Snowflake) — NOT STARTED
### 7. News Feed / Timeline — NOT STARTED
### 8. Chat System — NOT STARTED
### 9. Notification System — NOT STARTED
### 10. Search Autocomplete — NOT STARTED

---

## Strengths
- Production backend experience (Java/Spring Boot, MariaDB, SFTP, New Relic)
- Good note-taking and structured study approach
- Understands architectural styles and cohesion/coupling fundamentals
- Good instinct for identifying hidden operational concerns (reads becoming writes)
- Practical caching reasoning (LRU + TTL, cache-aside, write-through)
- Knows 301 vs 302 trade-offs

## Areas to Build
- **Back-of-envelope estimation** — biggest gap, needs to be muscle memory (DAU → QPS → Storage)
- **Trade-off articulation** — needs to proactively say "I chose X over Y because Z"
- **Failure mode thinking** — doesn't yet ask "what if this component dies?" for each box
- **Database selection reasoning** — defaults to SQL without articulating why over NoSQL
- **Scaling / multi-region** — needs to proactively volunteer what changes at 10x

---

## Session Log
- **2026-03-17:** First HLD mentor session. Assessed existing notes — foundation/architectural styles covered. Did full URL Shortener interview simulation. Strong on requirements, API design, caching, and async patterns. Weak on estimation and failure modes. Design notes saved. Java project scaffold created.
