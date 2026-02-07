# 06 - Behavioral Patterns

This module covers behavioral design patterns that deal with object communication.

## Patterns Covered

| Pattern | Intent | When to Use |
|---------|--------|-------------|
| **Chain of Responsibility** | Pass request along chain | Filters, handlers, middleware |
| **Command** | Encapsulate request as object | Undo/redo, queuing, logging |
| **Iterator** | Sequential access without exposing internals | Collections traversal |
| **Mediator** | Centralize complex communication | Chat rooms, air traffic control |
| **Memento** | Capture and restore object state | Undo, snapshots |
| **Observer** | Notify dependents of state changes | Event handling, pub/sub |
| **State** | Alter behavior based on state | State machines, workflows |
| **Strategy** | Define family of interchangeable algorithms | Sorting, validation, pricing |
| **Template Method** | Define skeleton, let subclasses fill in | Frameworks, lifecycle hooks |
| **Visitor** | Add operations without modifying classes | AST processing, report generation |

## Key Learnings

- [ ] Strategy Pattern (most common in interviews!)
- [ ] Observer Pattern
- [ ] State Pattern
- [ ] Command Pattern
- [ ] Chain of Responsibility
- [ ] Template Method
- [ ] Iterator, Mediator, Memento, Visitor

## Java-Specific Examples

- **Iterator**: `java.util.Iterator`
- **Observer**: `java.util.Observer`, event listeners
- **Strategy**: `Comparator`, `Executor`
- **Template Method**: `HttpServlet.doGet/doPost`

## Practice Implementation

- [ ] Payment Strategy (Credit, Debit, UPI, Wallet)
- [ ] Order State Machine (Created → Paid → Shipped → Delivered)
- [ ] Logger Chain of Responsibility
- [ ] Stock Price Observer

## Interview Questions

1. Strategy vs State - what's the difference?
2. Implement Observer pattern from scratch.
3. When would you use Command pattern?
