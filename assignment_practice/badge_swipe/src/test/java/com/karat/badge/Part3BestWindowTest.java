package com.karat.badge;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Part 3.
 *
 * Implement BadgeSystem#bestViolationWindow() so it returns
 *     name -> chronologically ordered "HH:MM" timestamps of the
 *             single largest qualifying 1-hour window for that employee
 * for every employee with at least one qualifying window.
 *
 * Tiebreaker: if two windows hold the same maximum number of swipes for the
 * same employee, return the one with the earliest start time.
 */
public class Part3BestWindowTest {

    private static final String LOG_PATH = "src/main/resources/swipes.log";

    @Test
    public void shouldReturnPaulsThreeSwipesAsHisBestWindow() throws IOException {
        Map<String, List<String>> result = BadgeSystem.fromFile(LOG_PATH).bestViolationWindow();
        assertEquals(Arrays.asList("13:00", "13:30", "13:45"), result.get("Paul"));
    }

    @Test
    public void nonViolatorsShouldNotAppearInResult() throws IOException {
        Map<String, List<String>> result = BadgeSystem.fromFile(LOG_PATH).bestViolationWindow();
        assertFalse("Jen has no qualifying window", result.containsKey("Jen"));
        assertFalse("Curtis has no qualifying window", result.containsKey("Curtis"));
    }

    @Test
    public void shouldPreferTheDenserWindowWhenAnEmployeeHasMultiple() {
        // Bob has TWO qualifying windows:
        //   [08:00, 09:00] -> 08:00, 08:15, 08:30, 08:45     = 4 swipes
        //   [10:00, 11:00] -> 10:00, 10:30                   = 2 swipes (not qualifying anyway)
        // The 4-swipe window must win.
        String raw = String.join("\n",
            "Bob 08:00",
            "Bob 08:15",
            "Bob 08:30",
            "Bob 08:45",
            "Bob 10:00",
            "Bob 10:30"
        );
        Map<String, List<String>> result = BadgeSystem.fromText(raw).bestViolationWindow();
        assertEquals(Arrays.asList("08:00", "08:15", "08:30", "08:45"), result.get("Bob"));
    }

    @Test
    public void onTieEarliestWindowWins() {
        // Two windows each holding exactly 3 swipes:
        //   [09:00, 10:00] -> 09:00, 09:30, 09:50
        //   [09:30, 10:30] -> 09:30, 09:50, 10:30
        // Earliest start wins -> first window.
        String raw = String.join("\n",
            "Tied 09:00",
            "Tied 09:30",
            "Tied 09:50",
            "Tied 10:30"
        );
        Map<String, List<String>> result = BadgeSystem.fromText(raw).bestViolationWindow();
        assertEquals(Arrays.asList("09:00", "09:30", "09:50"), result.get("Tied"));
    }

    @Test
    public void windowTimestampsShouldBeChronologicalEvenIfInputIsShuffled() {
        // Same swipes as Paul but inserted out of order.
        String raw = String.join("\n",
            "Sue 13:45",
            "Sue 13:00",
            "Sue 13:30"
        );
        Map<String, List<String>> result = BadgeSystem.fromText(raw).bestViolationWindow();
        assertEquals(Arrays.asList("13:00", "13:30", "13:45"), result.get("Sue"));
    }
}
