package com.karat.badge;

/**
 * One badge swipe by one employee on a single day.
 *
 * Example raw line:
 *   Paul 13:45
 *
 * The raw time string is preserved as-is for display. Downstream window
 * arithmetic needs MINUTES-SINCE-MIDNIGHT as an int — that's what
 * {@link #getMinutesSinceMidnight()} should return.
 */
public class SwipeRecord {

    private final String name;
    private final String time;

    public SwipeRecord(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    /**
     * Returns the swipe time as minutes elapsed since 00:00 on the same day.
     *
     * Examples:
     *   "00:00" -> 0
     *   "12:00" -> 720
     *   "13:45" -> 825      (13 * 60 + 45)
     *   "13:55" -> 835
     *   "23:59" -> 1439
     *
     * Part 1 TODO: implement the parsing. The skeleton always returns 0, which
     * breaks every downstream window calculation in Parts 2 and 3.
     */
    public int getMinutesSinceMidnight() {
        int timeSinceMidNight = 0;
        String time = getTime();
        String[] breakTime = time.split(":");
        timeSinceMidNight= Integer.parseInt(breakTime[0]) * 60 + Integer.parseInt(breakTime[1]);
        return timeSinceMidNight;
    }
}
