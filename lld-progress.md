# LLD Prep Progress — SDE-3 Java Track

## Target Companies
Meta, Google, Apple, Intuit, Adobe, InMobi, OpenAI, Anthropic

## Approach
- Java-first, interview-style LLD
- Mentor-led: problem presented → solve → review → commit
- Practice lives in `lld_practice/` for warm-ups and `lld/07-practice-*` for structured problems

---

## Topics / Problems

### Warm-up: Implement Data Structures (lld_practice/)
| # | Problem | Status | Notes |
|---|---------|--------|-------|
| 1 | Implement HashMap | 🔄 Requirements clarified, design pending | First LLD warm-up — tests generics, collision handling, resize, API design |

#### HashMap — Locked Scope
- No generics for first pass (will add at the end as a stretch)
- Support `null` keys/values (need special-case handling — `null.hashCode()` blows up)
- Single-threaded (no thread safety)
- API: `put`, `get`, `remove` only (no iteration / views / size for now)
- Resizing required (load-factor driven rehash)

#### HashMap — Open Questions (next session)
- **Collision strategy**: separate chaining vs open addressing — user to choose with reasoning
- **Load factor + resize policy**: threshold (e.g. 0.75) and growth factor (e.g. 2×) — user to defend specific numbers

#### HashMap — Notes / Weak spots noted
- User used "salt" for collision resolution — confused security concept (password salting) with collision handling. Corrected: real strategies are separate chaining vs open addressing.

### Foundations (lld/01 - lld/03)
- ⬜ OOP Fundamentals
- ⬜ SOLID Principles
- ⬜ Design Principles (DRY, KISS, YAGNI)

### Design Patterns (lld/04 - lld/06)
- ⬜ Creational
- ⬜ Structural
- ⬜ Behavioral

### Classic LLD Problems
- ⬜ Parking Lot
- ⬜ Tic-Tac-Toe
- ⬜ Library Management
- ⬜ Vending Machine
- ⬜ Elevator System

---

## Observations / Weak Spots
_To be filled in as sessions progress._

## Legend
- ⬜ Not Started  🔄 In Progress  ✅ Done
