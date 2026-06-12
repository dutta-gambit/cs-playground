# Citi Bank — Retake Prep Plan (1 week)

**Target:** In-person coding round at a Citi Bank office (retake of first round).
**Last score:** 220 / cutoff 240 — the gap is small. The goal is to close it with speed, accuracy, and airtight theory.
**Round date:** TBD — start this plan exactly 7 days before.

## What the gap tells us

220 vs 240 means a handful of dropped points, likely from:
1. Theory answers that were *mostly* right but missed key details (HashMap internals, static, GC).
2. The spot-the-violation question.
3. Slower / less polished coding than the scoring bar.

The retake is a **coding round**, so the plan front-loads theory early in the week and shifts to timed coding as the round approaches.

## Day-by-day

### Day 1 — HashMap internals (theory + build)
- Whiteboard the full `put()` / `get()` flow: hash spreading, index = `(n-1) & hash`, collision chaining, treeify at 8 / untreeify at 6, resize at load factor 0.75.
- Implement a HashMap from scratch in Java (ties into the `lld/hashmap-kickoff` work in `lld_practice/`).
- Cover: `hashCode()`/`equals()` contract, fail-fast iterators, HashMap vs Hashtable vs ConcurrentHashMap.

### Day 2 — `static` + garbage collection
- `static`: initialization order, static blocks, statics + inheritance (no overriding, only hiding), where statics live (metaspace), statics and testability.
- GC: generational hypothesis, minor vs major GC, reachability, G1 overview, memory-leak patterns (static collections, unremoved listeners, ThreadLocal).
- End the day with a rapid-fire self-quiz on Days 1–2.

### Day 3 — Design patterns & spot-the-violation
- Review SOLID with one concrete Java example each.
- Drill 8–10 "what's wrong with this code" snippets: name the violated principle/pattern, then refactor it verbally.
- Cover the most commonly violated ones: SRP, OCP, DIP, singleton misuse, missing strategy/factory.

### Day 4 — Timed Karat-style coding, set 1
- Re-solve the existing practice set from scratch, timed: badge swipe, traffic problem, nsync, treecraft (`assignment_practice/`).
- Karat format discipline: clarify → state approach out loud → code fast → test with an example. ~20–25 min per problem.

### Day 5 — Timed Karat-style coding, set 2
- Fresh problems in the typical Karat sweet spot: hashmap-heavy log/event processing, string parsing, interval/sliding-window, grid traversal.
- Focus on finishing **completely** — Karat scores partial solutions lower; a working brute force beats an unfinished optimal one.

### Day 6 — Full mock round
- One sitting, interview conditions: ~15 min theory rapid-fire (Days 1–3 topics) + 2 timed coding problems + 1 spot-the-violation snippet.
- Score it honestly; list every dropped point and patch each gap the same day.

### Day 7 — Light review + rest
- Re-do only the weak spots from the Day 6 mock.
- Skim notes; no new material. Logistics check for the in-person round (office location, ID, timing). Sleep well.

## Rules for the week
- Every coding session is **timed** — speed was likely part of the 20-point gap.
- Always narrate the approach before coding; Karat-style rounds score communication.
- Track each day's completion below.

## Progress
- [ ] Day 1 — HashMap internals
- [ ] Day 2 — static + GC
- [ ] Day 3 — patterns & spot-the-violation
- [ ] Day 4 — timed coding set 1
- [ ] Day 5 — timed coding set 2
- [ ] Day 6 — full mock
- [ ] Day 7 — review + rest
