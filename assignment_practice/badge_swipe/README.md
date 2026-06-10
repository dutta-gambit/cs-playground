# Badge Swipe — Karat Round 2 Coding

Classic Karat one-hour-window screening problem. You are given the day's badge
swipes for an office and must surface the employees who entered three or more
times within any single rolling one-hour window.

```
("Paul",   "13:00"), ("Paul",   "13:45"), ("Paul",   "13:30"),
("Jen",    "11:00"), ("Jen",    "11:40"), ("Jen",    "12:05"),
("Curtis", "09:00"), ("Curtis", "15:00"), ("Curtis", "15:30")
```

* Paul has 3 swipes between 13:00 and 13:45 → **violator**
* Jen's 3 swipes span 65 minutes (11:00 → 12:05) → no single 1-hour window
  contains all three → **not a violator**
* Curtis has only 2 close-together swipes → **not a violator**

## Window semantics

A swipe at time `t` and a swipe at `t + 60 minutes` are **both inside** the
same one-hour window (inclusive end). So three swipes at 09:00, 09:30, 10:00
*do* qualify.

## Parts

| Part | Method                                | Notes                                           |
| ---- | ------------------------------------- | ----------------------------------------------- |
| 1    | `SwipeRecord#getMinutesSinceMidnight` | Fix the parsing — currently returns 0 always.   |
| 2    | `BadgeSystem#findViolators`           | Sorted list of names with at least one bad window. |
| 3    | `BadgeSystem#bestViolationWindow`     | Per violator, return the timestamps in their largest qualifying window. Tiebreaker: earliest window. |

## Running

```bash
mvn test                                          # all 3 parts
mvn -Dtest=Part1TimeParseTest      test           # just Part 1
mvn -Dtest=Part2FindViolatorsTest  test           # just Part 2
mvn -Dtest=Part3BestWindowTest     test           # just Part 3
```

All three test classes are red on a fresh clone — fix them one at a time.
