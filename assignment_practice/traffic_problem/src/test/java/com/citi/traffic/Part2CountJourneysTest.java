package com.citi.traffic;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Part 2: State Tracking and Journey Enumeration.
 *
 * Implement LogFile#countJourneys() so it returns the total number of
 * complete journeys present in the log.
 *
 * A complete journey is:  ENTRY  ->  zero or more MAINROAD  ->  EXIT.
 *
 * The log is guaranteed to be perfectly formed (no dangling sessions).
 *
 * The bundled tollbooth.log contains six complete journeys.
 */
public class Part2CountJourneysTest {

    private static final String LOG_PATH = "src/main/resources/tollbooth.log";

    @Test
    public void totalNumberOfCompleteJourneysShouldBeSix() throws IOException {
        LogFile logFile = LogFile.fromFile(LOG_PATH);

        assertEquals(6, logFile.countJourneys());
    }

    @Test
    public void inlineLogWithThreeEntriesShouldReportThreeJourneys() {
        String raw = String.join("\n",
            "100.000 AAA111 250E ENTRY",
            "200.000 AAA111 260E EXIT",
            "300.000 BBB222 100E ENTRY",
            "400.000 BBB222 110E MAINROAD",
            "500.000 BBB222 120E EXIT",
            "600.000 CCC333 300E ENTRY",
            "700.000 CCC333 310E EXIT"
        );

        LogFile logFile = LogFile.fromText(raw);

        assertEquals(3, logFile.countJourneys());
    }

    @Test
    public void emptyLogShouldReportZeroJourneys() {
        LogFile logFile = LogFile.fromText("");

        assertEquals(0, logFile.countJourneys());
    }
}
