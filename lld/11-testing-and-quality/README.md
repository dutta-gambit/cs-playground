# 11 - Testing and Quality

This module covers testing strategies and code quality practices for production-grade code.

## Topics Covered

- Unit Testing with JUnit 5
- Mocking with Mockito
- Test-Driven Development (TDD)
- Code Coverage
- Integration Testing
- Testing Design Patterns

## Key Learnings

- [ ] Write effective unit tests with JUnit 5
- [ ] Mock dependencies with Mockito
- [ ] Practice TDD red-green-refactor cycle
- [ ] Achieve meaningful code coverage
- [ ] Test design patterns implementations

## Testing Pyramid

```
         /\
        /  \       E2E Tests (few)
       /----\
      /      \     Integration Tests (some)
     /--------\
    /          \   Unit Tests (many)
   /------------\
```

## JUnit 5 Essentials

- `@Test`, `@BeforeEach`, `@AfterEach`
- `@ParameterizedTest`
- `@Nested` for test organization
- Assertions and assumptions

## Mockito Essentials

- `@Mock`, `@InjectMocks`
- `when().thenReturn()`
- `verify()` method calls
- Argument matchers

## Practice Exercises

- [ ] Write tests for Singleton pattern
- [ ] TDD a Calculator class
- [ ] Mock database layer in Repository tests
- [ ] Test Observer pattern with multiple subscribers

## Interview Questions

1. What is the difference between mocking and stubbing?
2. How do you test private methods?
3. What is code coverage and why does 100% not mean bug-free?
