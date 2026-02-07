# 05 - Structural Patterns

This module covers structural design patterns that deal with object composition.

## Patterns Covered

| Pattern | Intent | When to Use |
|---------|--------|-------------|
| **Adapter** | Convert interface to compatible one | Legacy code integration, third-party libs |
| **Bridge** | Separate abstraction from implementation | Platform independence |
| **Composite** | Treat individual and compositions uniformly | Tree structures, UI components |
| **Decorator** | Add behavior dynamically | Extending functionality without subclassing |
| **Facade** | Provide simplified interface | Complex subsystems |
| **Flyweight** | Share common state efficiently | Large number of similar objects |
| **Proxy** | Placeholder for another object | Lazy loading, access control, logging |

## Key Learnings

- [ ] Adapter Pattern
- [ ] Bridge Pattern
- [ ] Composite Pattern
- [ ] Decorator Pattern
- [ ] Facade Pattern
- [ ] Flyweight Pattern
- [ ] Proxy Pattern

## Java-Specific Examples

- **Adapter**: `Arrays.asList()`, `InputStreamReader`
- **Decorator**: Java I/O streams (`BufferedInputStream`, `DataInputStream`)
- **Proxy**: `java.lang.reflect.Proxy`, Spring AOP
- **Composite**: Swing UI components

## Practice Implementation

- [ ] Payment Gateway Adapter (Stripe, PayPal, Razorpay)
- [ ] Coffee shop Decorator (add milk, sugar, whipped cream)
- [ ] File System Composite (files and folders)
- [ ] Image Proxy (lazy loading)

## Interview Questions

1. Decorator vs Proxy - what's the difference?
2. When would you use Flyweight pattern?
3. How does Java I/O use Decorator pattern?
