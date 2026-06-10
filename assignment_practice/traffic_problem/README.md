# Traffic Problem — Citi-style Coding Assignment

A three-part, progressively harder OOP / data-grouping problem modelled on
the live-coding round used in Citi engineering interviews.

You have ~50 minutes. Two cleanly completed parts is the universal baseline
for progressing to the next round; three is the senior-engineer bar.

---

## Scenario

You are building software that analyses logs produced by sensors on a
divided, limited-access highway. Three booth types exist:

| Code | Type     | Meaning                                                                 |
|------|----------|-------------------------------------------------------------------------|
| E    | ENTRY    | Vehicle enters the highway.                                             |
| X    | EXIT     | Vehicle exits the highway.                                              |
| M    | MAINROAD | Drive-through sensor placed every **10 km** along the highway.          |

Each log line follows the schema:

```
<Timestamp> <License_Plate> <Location_and_Direction> <Booth_Type>
```

Example:

```
90750.191 JOX304 250E ENTRY
91081.684 JOX304 260E MAINROAD
91483.251 JOX304 270E MAINROAD
91874.493 JOX304 280E EXIT
```

- `Timestamp` is seconds (floating point).
- `License_Plate` is a string.
- `Location_and_Direction` is `<km-marker><direction>` (e.g. `260E`).
- `Booth_Type` is one of `ENTRY`, `EXIT`, `MAINROAD`.

The sample log file lives at [src/main/resources/tollbooth.log](src/main/resources/tollbooth.log).

---

## Layout

```
traffic_problem/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/citi/traffic/
    │   │   ├── LogEntry.java     # parsing entity (contains the Part 1 bug)
    │   │   └── LogFile.java      # collection + Part 2 / Part 3 stubs
    │   └── resources/
    │       └── tollbooth.log     # sample input
    └── test/
        └── java/com/citi/traffic/
            ├── Part1TimestampTypeTest.java
            ├── Part2CountJourneysTest.java
            └── Part3CatchSpeedersTest.java
```

## Running tests

```bash
cd cs-playground/assignment_practice/traffic_problem
mvn test
```

All three test classes are expected to **fail on the first run**. Fix them
in order — Part 1 first, since Part 3 cannot compile cleanly until the
timestamp type is fixed.

---

## Part 1 — The Pre-Existing Data Type Bug

The IDE is pre-populated with a skeletal `LogEntry`. Run the tests, see the
failure, fix the bug.

The skeleton stores the timestamp token (e.g. `90750.191`) as a `String`.
Every speed calculation in Part 3 needs to subtract timestamps, so this
must be a `double` (or `Double`).

**Fix:** change the `timestamp` field, the constructor parsing, and the
getter so the value is parsed via `Double.parseDouble(...)` and exposed as
a numeric type.

A senior engineer is expected to diagnose and resolve this in under three
minutes.

---

## Part 2 — `countJourneys()`

A **complete journey** is exactly:

```
ENTRY  ->  zero or more MAINROAD  ->  EXIT
```

Implement `LogFile#countJourneys()` so it returns the total number of
complete journeys in the log.

**Stated constraint:** the log is perfectly formatted. Every `ENTRY` has a
matching `EXIT`. No dangling sessions, no missing entries.

Because of that constraint, the count of complete journeys equals the
count of `ENTRY` records. A single `O(N)` linear pass with `O(1)` extra
space is the optimal solution.

While the simple counter is mathematically sufficient, a senior candidate
is expected to verbalise:

> "If we were designing this for a production environment where sensor
> data loss or out-of-order delivery could occur, we'd need a
> `Map<String, Boolean>` tracking the open/closed state of each vehicle's
> journey."

That architectural narration is what differentiates the AVP/VP rubric.

---

## Part 3 — `catchSpeeders()`

A driver is flagged for **one journey** if either condition holds within
that journey:

1. **Single-segment violation** — average speed `>= 130 km/h` across any
   one 10-km segment.
2. **Multi-segment violation** — average speed `>= 120 km/h` across at
   least **two** distinct 10-km segments of the same journey.

### Threshold derivation

Each consecutive pair of sensors is exactly 10 km apart (the problem
guarantees the log is well-formed).

```
130 km/h  ->  time = (10 / 130) * 3600  =  276.9230... s    (rule 1 boundary)
120 km/h  ->  time = (10 / 120) * 3600  =  300.0000   s     (rule 2 boundary)
```

So:

- If `time_diff <= 276.92` for any segment, flag the journey as a
  single-segment violation, break, move on.
- If `time_diff <= 300.0` for two segments of the same journey, flag and
  break.

### Output

Return `Map<String, Integer>` where the key is the license plate and the
value is the number of journeys in which that vehicle was flagged.
Drivers with zero violating journeys must **not** appear in the map.

### Suggested algorithmic blueprint

1. **Group** entries by license plate into a `Map<String, List<LogEntry>>`.
2. **Sort** each list chronologically (or explicitly confirm with your
   interviewer that the log is already chronological).
3. **Segment** each list into discrete journeys bounded by `ENTRY` and
   `EXIT` events.
4. **Sliding window of size two** through each journey; compute
   `time_diff = entry[i].timestamp - entry[i-1].timestamp`.
5. Evaluate the two rules sequentially per journey. On a hit, increment
   the master counter for that plate and `break` to the next journey.
6. Filter out plates with zero violations before returning.

### Bundled log expectations

The bundled `tollbooth.log` should produce:

```
countJourneys()   =>  6
catchSpeeders()   =>  { ABC123: 1, XYZ789: 1, JOX304: 1 }
```

Breakdown:

| Journey | Plate  | Segments (sec)            | Result                              |
|---------|--------|---------------------------|-------------------------------------|
| 1       | JOX304 | 331, 402, 391             | safe                                |
| 2       | ABC123 | **250**, 350              | single-segment violation (130 km/h) |
| 3       | XYZ789 | **290**, 310, **290**     | multi-segment violation (120 km/h)  |
| 4       | DEF456 | 500, 500                  | safe                                |
| 5       | JOX304 | **280**, **300**          | multi-segment violation (120 km/h)  |
| 6       | ABC123 | 500, 500                  | safe                                |
