# Citi Bank — First Round (Karat-hosted)

**Date:** June 10, 2026, 10:00 PM
**Format:** Karat-hosted technical screen
**Score:** 220 (cutoff to move ahead: 240)
**Outcome:** Did not clear the cutoff, but Citi HR offered a **retake: an in-person coding round at a Citi Bank office**. Round was mostly theory-heavy; caught off guard expecting more coding. Goal: prepare for a full week before the retake — see [retake-prep-plan.md](retake-prep-plan.md).

## Prep done before the round

Solved Karat-style practice problems under [`assignment_practice/`](../../../assignment_practice/):

- nsync
- treecraft
- badge swipe
- traffic problem

The actual round turned out to be far more **theory-oriented** than these coding simulations.

## What was actually asked

### 1. Java theory

- **HashMap internals** — bucketing, hashing, collision handling (chaining → treeification at threshold 8), resizing/load factor, why `hashCode()`/`equals()` contract matters.
- **`static` keyword** — static fields/methods/blocks/nested classes, class-level vs instance-level memory, when statics are initialized (class loading).
- **Garbage collection (basics)** — heap generations (young/old), reachability, when objects become eligible for GC, common collectors at a high level.

### 2. "What's the problem with this code?"

Given a code snippet, identify **which design pattern / principle is being violated**. This is spot-the-smell style: SOLID violations (e.g. SRP, OCP, DIP), misuse of singletons, tight coupling instead of strategy/factory, etc.

## Takeaways / prep plan for next time

- [ ] Be able to **whiteboard HashMap internals end-to-end**: put/get flow, hash spreading, collision, treeify, resize, fail-fast iterators, HashMap vs ConcurrentHashMap.
- [ ] Deep-dive **`static`**: initialization order, statics + inheritance, statics in memory (metaspace), why statics hurt testability.
- [ ] **GC**: generational hypothesis, minor vs major GC, G1 at a high level, `finalize()` deprecation, memory-leak patterns in Java (static collections, listeners).
- [ ] Drill **spot-the-violation** snippets: take small code samples and name the violated SOLID principle / pattern misuse, then say how to fix it.
- [ ] Timed coding practice — the retake is a **coding round**, so theory alone won't be enough this time. Full schedule in [retake-prep-plan.md](retake-prep-plan.md).
