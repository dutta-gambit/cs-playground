package com.citi.traffic;

/**
 * Represents one observation recorded by a toll-booth sensor on the highway.
 *
 * Raw log line schema (space-delimited):
 *   <Timestamp> <License_Plate> <Location_and_Direction> <Booth_Type>
 *
 * Example:
 *   90750.191 JOX304 250E ENTRY
 *
 * Booth types: ENTRY, EXIT, MAINROAD.
 */
public class LogEntry {

    private Double timestamp;
    private String licensePlate;
    private int location;
    private String direction;
    private String boothType;

    public LogEntry(String rawLine) {
        String[] tokens = rawLine.trim().split("\\s+");
        if (tokens.length != 4) {
            throw new IllegalArgumentException("Malformed log line: " + rawLine);
        }

        this.timestamp = Double.parseDouble(tokens[0]);
        this.licensePlate = tokens[1];

        String locDir = tokens[2];
        this.location = Integer.parseInt(locDir.substring(0, locDir.length() - 1));
        this.direction = locDir.substring(locDir.length() - 1);

        this.boothType = tokens[3];
    }

    public Double getTimestamp() {
        return timestamp;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getLocation() {
        return location;
    }

    public String getDirection() {
        return direction;
    }

    public String getBoothType() {
        return boothType;
    }

    @Override
    public String toString() {
        return timestamp + " " + licensePlate + " " + location + direction + " " + boothType;
    }
}
