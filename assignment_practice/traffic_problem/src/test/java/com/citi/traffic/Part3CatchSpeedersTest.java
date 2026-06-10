package com.citi.traffic;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Part 3: Catching Speeders.
 *
 * Implement LogFile#catchSpeeders() so it returns
 *     License Plate -> number of journeys flagged as speeding
 * for every vehicle with at least one violating journey.
 *
 * Violation rules (per journey):
 *   (a) Single-segment violation: average speed >= 130 km/h on any one
 *       10-km segment  (i.e. time elapsed <= 276.92 s).
 *   (b) Multi-segment violation: average speed >= 120 km/h on >= TWO
 *       distinct 10-km segments  (i.e. time elapsed <= 300.0 s twice in
 *       the same journey).
 *
 * Bundled log expectations:
 *   - JOX304 journey 1  -> safe
 *   - ABC123 journey 1  -> SINGLE-segment violation (250 s segment)
 *   - XYZ789 journey 1  -> MULTI-segment violation (290 s + 290 s)
 *   - DEF456 journey 1  -> safe
 *   - JOX304 journey 2  -> MULTI-segment violation (280 s + 300 s)
 *   - ABC123 journey 2  -> safe
 *
 * Expected map:  { ABC123 -> 1, XYZ789 -> 1, JOX304 -> 1 }.
 */
public class Part3CatchSpeedersTest {

    private static final String LOG_PATH = "src/main/resources/tollbooth.log";

    @Test
    public void shouldDetectAllThreeSpeedersWithOneViolationEach() throws IOException {
        LogFile logFile = LogFile.fromFile(LOG_PATH);

        Map<String, Integer> speeders = logFile.catchSpeeders();

        assertEquals("Expected exactly three speeders in the log",
                3, speeders.size());
        assertEquals("ABC123 should have 1 violating journey",
                Integer.valueOf(1), speeders.get("ABC123"));
        assertEquals("XYZ789 should have 1 violating journey",
                Integer.valueOf(1), speeders.get("XYZ789"));
        assertEquals("JOX304 should have 1 violating journey",
                Integer.valueOf(1), speeders.get("JOX304"));
    }

    @Test
    public void cleanDriverShouldNeverBeReported() throws IOException {
        LogFile logFile = LogFile.fromFile(LOG_PATH);

        Map<String, Integer> speeders = logFile.catchSpeeders();

        assertFalse("DEF456 never speeds and must not be in the returned map",
                speeders.containsKey("DEF456"));
    }

    @Test
    public void shouldFlagSingleSegmentViolationAt130KmphOrAbove() {
        String raw = String.join("\n",
            "1000.000 SPD001 100E ENTRY",
            "1250.000 SPD001 110E MAINROAD",
            "1600.000 SPD001 120E EXIT"
        );

        Map<String, Integer> speeders = LogFile.fromText(raw).catchSpeeders();

        assertEquals(Integer.valueOf(1), speeders.get("SPD001"));
    }

    @Test
    public void shouldFlagMultiSegmentViolationWhenTwoSegmentsHit120Kmph() {
        String raw = String.join("\n",
            "2000.000 SPD002 200E ENTRY",
            "2290.000 SPD002 210E MAINROAD",
            "2600.000 SPD002 220E MAINROAD",
            "2890.000 SPD002 230E EXIT"
        );

        Map<String, Integer> speeders = LogFile.fromText(raw).catchSpeeders();

        assertEquals(Integer.valueOf(1), speeders.get("SPD002"));
    }

    @Test
    public void boundarySegmentExactlyAt300SecondsShouldStillCountAs120Kmph() {
        String raw = String.join("\n",
            "3000.000 BND001 300E ENTRY",
            "3280.000 BND001 310E MAINROAD",
            "3580.000 BND001 320E EXIT"
        );

        Map<String, Integer> speeders = LogFile.fromText(raw).catchSpeeders();

        assertEquals(
            "Two segments at exactly the 300 s boundary must be flagged as a multi-segment violation",
            Integer.valueOf(1), speeders.get("BND001"));
    }

    @Test
    public void slowDriverShouldNeverBeFlagged() {
        String raw = String.join("\n",
            "4000.000 SLW001 400E ENTRY",
            "4500.000 SLW001 410E MAINROAD",
            "5000.000 SLW001 420E EXIT"
        );

        Map<String, Integer> speeders = LogFile.fromText(raw).catchSpeeders();

        assertTrue("Slow driver should not appear in the speeders map",
                speeders.isEmpty());
    }

    @Test
    public void sameVehicleSpeedingOnTwoSeparateJourneysShouldCountTwice() {
        String raw = String.join("\n",
            "1000.000 RPT001 100E ENTRY",
            "1250.000 RPT001 110E MAINROAD",
            "1600.000 RPT001 120E EXIT",
            "9000.000 RPT001 100E ENTRY",
            "9250.000 RPT001 110E MAINROAD",
            "9600.000 RPT001 120E EXIT"
        );

        Map<String, Integer> speeders = LogFile.fromText(raw).catchSpeeders();

        assertEquals("Two separate violating journeys must accumulate to 2",
                Integer.valueOf(2), speeders.get("RPT001"));
    }
}
