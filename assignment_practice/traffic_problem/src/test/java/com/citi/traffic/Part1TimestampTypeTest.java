package com.citi.traffic;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Part 1: The Pre-Existing Data Type Bug.
 *
 * The skeleton currently stores the timestamp token (e.g. "90750.191")
 * as a String. Because every downstream calculation in Part 3 depends on
 * subtracting two timestamps to derive a vehicle's speed, this must be
 * stored as a numeric type.
 *
 * Fix LogEntry so the `timestamp` field is `double` (or `Double`) and
 * the parsed value is a true floating-point number.
 */
public class Part1TimestampTypeTest {

    @Test
    public void timestampFieldShouldBeDeclaredAsADoublePrecisionType() throws Exception {
        Field tsField = LogEntry.class.getDeclaredField("timestamp");
        String typeName = tsField.getType().getName();

        boolean isNumeric = typeName.equals("double") || typeName.equals("java.lang.Double");

        assertTrue(
            "LogEntry#timestamp must be a numeric type (double or Double) so that " +
            "arithmetic between two entries is possible. Found: " + typeName,
            isNumeric);
    }

    @Test
    public void timestampGetterShouldReturnADoublePrecisionType() throws Exception {
        Method getter = LogEntry.class.getMethod("getTimestamp");
        String returnTypeName = getter.getReturnType().getName();

        boolean isNumeric =
            returnTypeName.equals("double") || returnTypeName.equals("java.lang.Double");

        assertTrue(
            "LogEntry#getTimestamp() must return a numeric type (double or Double). " +
            "Found: " + returnTypeName,
            isNumeric);
    }

    @Test
    public void parsedTimestampShouldEqualTheRawNumericValue() throws Exception {
        LogEntry entry = new LogEntry("90750.191 JOX304 250E ENTRY");

        Method getter = LogEntry.class.getMethod("getTimestamp");
        Object raw = getter.invoke(entry);

        if (!(raw instanceof Number)) {
            fail("Expected getTimestamp() to return a Number, got: " +
                 (raw == null ? "null" : raw.getClass().getName()));
        }

        double actual = ((Number) raw).doubleValue();
        assertEquals(90750.191, actual, 1e-6);
    }
}
