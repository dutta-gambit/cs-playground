package com.karat.badge;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Part 1.
 *
 * SwipeRecord#getMinutesSinceMidnight() must turn an "HH:MM" string into
 * minutes elapsed since 00:00. The skeleton returns 0 for everything, which
 * means Parts 2 and 3 will produce nonsense until this is fixed.
 *
 * Implement the parsing and these five tests turn green.
 */
public class Part1TimeParseTest {

    @Test
    public void midnightShouldBeZeroMinutes() {
        assertEquals(0, new SwipeRecord("Alice", "00:00").getMinutesSinceMidnight());
    }

    @Test
    public void onePastMidnightShouldBeOneMinute() {
        assertEquals(1, new SwipeRecord("Alice", "00:01").getMinutesSinceMidnight());
    }

    @Test
    public void noonShouldBeSevenHundredTwentyMinutes() {
        assertEquals(720, new SwipeRecord("Bob", "12:00").getMinutesSinceMidnight());
    }

    @Test
    public void afternoonSwipeShouldYieldExactMinutesSinceMidnight() {
        // 13:45 -> 13 * 60 + 45 = 825
        assertEquals(825, new SwipeRecord("Paul", "13:45").getMinutesSinceMidnight());
    }

    @Test
    public void lastMinuteOfTheDayShouldBeFourteenThirtyNine() {
        // 23:59 -> 23 * 60 + 59 = 1439
        assertEquals(1439, new SwipeRecord("Zed", "23:59").getMinutesSinceMidnight());
    }
}
