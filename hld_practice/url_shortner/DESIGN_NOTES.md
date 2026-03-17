# URL Shortener — HLD Design Notes

## Functional Requirements
| # | Requirement | Scope |
|---|---|---|
| 1 | Shorten a long URL → return short URL | In scope |
| 2 | Redirect short URL → long URL (302) | In scope |
| 3 | URL expiration (user-defined TTL, default 5 years) | In scope |
| 4 | Analytics (click count, tracking) | Out of scope |
| 5 | Custom aliases | Out of scope |

## Non-Functional Requirements
| NFR | Value | Reasoning |
|---|---|---|
| DAU | 1M | Assumption |
| Write QPS | ~5 avg, ~15 peak | 500K URLs/day, 3x peak multiplier |
| Read QPS | ~500 avg, ~1500 peak | 100:1 read:write ratio |
| Availability | 99.99% (four nines) | ~1 hour downtime/year; reads survive via cache |
| Latency | <50ms for redirects | Redirects must be near-instant |
| Durability | Zero data loss | Shortened URLs cannot be lost once created |
| Storage | ~270 GB over 5 years | ~300 bytes/record x 900M records |
| System characteristic | Read-heavy (100:1) | Drives caching strategy |

---

## Back-of-Envelope Estimation

### Core Formulas
```
QPS         = Total daily requests / 100K (seconds in a day, rounded)
Peak QPS    = QPS x 3 (typical multiplier)
Storage     = records_per_day x record_size x retention_days
Cache size  = hot_records x record_size
```

### Write Estimation
```
1M DAU x 0.5 URLs per user/day = 500K writes/day
500K / 100K = 5 write QPS (average)
5 x 3 = 15 write QPS (peak)
```

### Read Estimation
```
Read:Write ratio = 100:1
500K x 100 = 50M reads/day
50M / 100K = 500 read QPS (average)
500 x 3 = 1500 read QPS (peak)
```

### Storage Estimation
```
Each record: short_code (7 bytes) + original_url (~200 bytes) + metadata (~100 bytes) = ~300 bytes
500K URLs/day x 365 days x 5 years = ~900M records
900M x 300 bytes = ~270 GB
```

### Cache Sizing
```
43M reads/day, avg hot URL clicked ~5-10 times/day
~8M unique hot URLs at any given time
8M x 300 bytes = ~2.4 GB (single Redis instance handles this easily)
Even at 50M hot URLs = 15 GB — still fine.
```

### Useful Numbers to Memorize
- 1 day = ~86,400 seconds, round to 100K
- 1M requests/day = ~12 QPS
- 1B requests/day = ~12K QPS
- Redis GET = 0.1-0.5ms
- MySQL simple query = 1-5ms
- Cross-continent round trip = 100-150ms

---

## Core Entity

### mapped_url table
| Column | Type | Notes |
|---|---|---|
| id | BIGINT AUTO_INCREMENT | Primary key |
| short_code | VARCHAR(7) | Unique index, lookup key |
| original_url | TEXT | The long URL |
| is_active | BOOLEAN | Active/inactive flag |
| expires_at | TIMESTAMP | User-defined or default (created_at + 5 years) |
| last_accessed_at | TIMESTAMP | Updated async (not on every read) |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

---

## API Design
| Endpoint | Method | Purpose | Status Code |
|---|---|---|---|
| `/urls` | POST | Create short URL | 201 Created |
| `/{shortCode}` | GET | Redirect to original | 302 Found |

### POST /urls
```
Request:  { "original_url": "https://example.com/long", "ttl": "5000 days" }
Response: { "short_url": "https://short.url/abd9823" }
```

### GET /{shortCode}
```
Response: 302 redirect to original URL
Header:   Location: https://example.com/long
```

302 (not 301) — so every click hits our server, enabling last_accessed_at tracking and future analytics.

---

## Architecture

### Short Code Generation — Key Generation Service (KGS)
- Pre-generates millions of unique 7-char base62 keys
- Stores in DB with used/unused flag
- API servers grab keys in batches (e.g., 1000 at a time) into local memory via Redis
- No collision, no hashing, O(1) key assignment
- Batch allocation avoids per-request DB contention

### Write Path
```
Client -> LB -> API Server -> grab key from Redis (pre-loaded from KGS)
                            -> write (short_code, original_url) to DB
                            -> populate Redis cache with new mapping
```

### Read Path
```
Client -> LB -> API Server -> check Redis cache
                            -> HIT: return 302 redirect
                            -> MISS: read DB -> populate cache -> return 302
```

### Caching Strategy
- Cache-aside on reads, write-through on creates
- LRU eviction + 1-week TTL
- ~3 GB cache for 10M hot URLs — single Redis instance

### last_accessed_at — Async Update
- Reads should not become synchronous writes
- Buffer access events in memory, batch flush to DB periodically
- Or fire events to a queue, consumer updates DB async
- Precision doesn't matter — cleanup job runs daily

---

## Key Trade-Off Decisions

| Decision | Chose | Over | Because |
|---|---|---|---|
| Short code generation | KGS (pre-generated) | Hash truncation | Zero collisions, no rehash loops |
| Short code generation | KGS | Auto-increment + base62 | Auto-increment is predictable/guessable |
| Cache eviction | LRU + TTL | LFU | Temporal locality suits URL access patterns |
| Redirect status | 302 | 301 | 301 gets browser-cached, lose visibility |
| last_accessed_at update | Async | Sync | Don't let background concern degrade critical path |

---

## Areas to Improve (Interview Feedback)
1. **Back-of-envelope estimation** — needs to be muscle memory, not guided
2. **Trade-off articulation** — always say "I chose X over Y because Z"
3. **Failure mode thinking** — for every component, ask "what if this dies?"
4. **Database choice justification** — why SQL? why not DynamoDB or pure Redis?
5. **Multi-region / scaling discussion** — proactively address "what changes at 10x?"
