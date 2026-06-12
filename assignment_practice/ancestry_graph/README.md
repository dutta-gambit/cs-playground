# Ancestry Graph — Karat Family Tree Problem

Given a list of `(parent, child)` integer pairs describing a multi-generation
family tree, answer three escalating questions.

## Sample data (`src/main/resources/pairs.log`)

```
1 2
3 2
2 4
5 4
5 6
7 5
7 8
8 9
```

Drawn as a graph (arrows point parent → child):

```
     1     3       7
      \   /       / \
        2        5   8
         \      / \   \
          \    /   \   \
           --4      6   9
```

## Parts

| Part | Method                                          | Notes                                                          |
| ---- | ----------------------------------------------- | -------------------------------------------------------------- |
| 1    | `FamilyTree#findIndividualsWithZeroOrOneParent` | Returns two ascending lists: `result.get(0)` = no parents, `result.get(1)` = exactly one parent. |
| 2    | `FamilyTree#hasCommonAncestor`                  | Boolean — do two individuals share at least one ancestor? An individual is **not** their own ancestor. |
| 3    | `FamilyTree#earliestAncestor`                   | Returns the ancestor at the FARTHEST distance from the individual (oldest known generation). Ties: return any. No parents: `-1`. |

## Sample answers

- Part 1 → `[ [1, 3, 7], [5, 6, 8, 9] ]`  (individuals 2 and 4 have two parents and appear in neither list.)
- Part 2 → `hasCommonAncestor(4, 6) = true` (via 5 or 7).  `hasCommonAncestor(1, 3) = false`.
- Part 3 → `earliestAncestor(9) = 7`.  `earliestAncestor(4)` can be any of `{1, 3, 7}` (all at depth 2).

## Running

```bash
mvn test                                           # all 3 parts
mvn -Dtest=Part1ZeroAndOneParentTest test          # just Part 1
mvn -Dtest=Part2CommonAncestorTest test            # just Part 2
mvn -Dtest=Part3EarliestAncestorTest test          # just Part 3
```

All three test classes are red on a fresh clone — implement them one at a time.
