package com.karat.badge;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Part 2.
 *
 * Implement BadgeSystem#findViolators() so it returns the names of every
 * employee who badged in three or more times within ANY single one-hour
 * window. Output sorted alphabetically.
 *
 * Bundled log (src/main/resources/swipes.log):
 *   - Paul    13:00, 13:30, 13:45   -> 3 swipes in 45 min            -> VIOLATOR
 *   - Jen     11:00, 11:40, 12:05   -> span is 65 min, no window fits -> safe
 *   - Curtis  09:00, 15:00, 15:30   -> only 2 close swipes            -> safe
 * Expected: ["Paul"].
 */
public class Part2FindViolatorsTest {

    private static final String LOG_PATH = "src/main/resources/swipes.log";

    @Test
    public void shouldReturnPaulAsOnlyViolatorInSampleLog() throws IOException {
        List<String> violators = BadgeSystem.fromFile(LOG_PATH).findViolators();
        assertEquals(Collections.singletonList("Paul"), violators);
    }

    @Test
    public void jenShouldNotBeAViolator_ThreeSwipesSpan65Minutes() throws IOException {
        List<String> violators = BadgeSystem.fromFile(LOG_PATH).findViolators();
        assertFalse("Jen's three swipes span 65 min; no 1-hour window holds all three",
                violators.contains("Jen"));
    }

    @Test
    public void curtisShouldNotBeAViolator_OnlyTwoCloseSwipes() throws IOException {
        List<String> violators = BadgeSystem.fromFile(LOG_PATH).findViolators();
        assertFalse(violators.contains("Curtis"));
    }

    @Test
    public void shouldReturnMultipleViolatorsSortedAlphabetically() {
        String raw = String.join("\n",
            "Zara 09:00",
            "Zara 09:30",
            "Zara 09:50",
            "Bob 10:00",
            "Bob 10:30",
            "Bob 10:45"
        );
        assertEquals(Arrays.asList("Bob", "Zara"),
                BadgeSystem.fromText(raw).findViolators());
    }

    @Test
    public void exactlyThreeSwipesSpanningExactlySixtyMinutesShouldFlag() {
        // 09:00 ... 10:00 is 60 min apart — inclusive window includes both ends.
        String raw = String.join("\n",
            "Eve 09:00",
            "Eve 09:30",
            "Eve 10:00"
        );
        assertEquals(Collections.singletonList("Eve"),
                BadgeSystem.fromText(raw).findViolators());
    }

    @Test
    public void threeSwipesSpanningSixtyOneMinutesShouldNotFlag() {
        // 09:00 ... 10:01 is 61 min apart — too wide for any single 1-hour window.
        String raw = String.join("\n",
            "Mallory 09:00",
            "Mallory 09:30",
            "Mallory 10:01"
        );
        assertEquals(Collections.emptyList(), BadgeSystem.fromText(raw).findViolators());
    }
}
