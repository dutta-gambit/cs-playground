# 03 - Design Principles

This module covers essential software design principles beyond SOLID.

## Chapters Covered

- DRY (Don't Repeat Yourself)
- KISS (Keep It Simple, Stupid)
- YAGNI (You Aren't Gonna Need It)
- Composition over Inheritance
- Law of Demeter (Principle of Least Knowledge)
- Separation of Concerns
- Code Smells and Refactoring

## Key Learnings

- [ ] Identify and eliminate code duplication (DRY)
- [ ] Write simple, readable code (KISS)
- [ ] Avoid premature optimization (YAGNI)
- [ ] Prefer composition over inheritance
- [ ] Understand and apply Law of Demeter
- [ ] Recognize common code smells

## Code Smells to Study

| Smell | Description | Solution |
|-------|-------------|----------|
| Long Method | Methods doing too much | Extract Method |
| God Class | Class with too many responsibilities | Split into smaller classes |
| Feature Envy | Method using other class's data excessively | Move Method |
| Shotgun Surgery | One change requires many file edits | Consolidate logic |

## Practice Exercise

**Code Review Challenge:**
Review a codebase with intentional design violations and identify/fix them.

## Interview Questions

1. What is the difference between DRY and WET code?
2. When might you intentionally violate YAGNI?
3. Explain Law of Demeter with an example.
