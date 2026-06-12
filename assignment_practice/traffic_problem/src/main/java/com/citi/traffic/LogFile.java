package com.citi.traffic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In-memory representation of a toll-booth log file.
 *
 * Backed by an ordered list of {@link LogEntry} objects parsed from the raw
 * log text. The class exposes two analytical operations:
 *
 *   - {@link #countJourneys()}      (Part 2)
 *   - {@link #catchSpeeders()}      (Part 3)
 */
public class LogFile {

    // 10 km / 130 km/h * 3600 s/h  ->  any segment faster than this trips Rule A
    private static final double SINGLE_SEGMENT_THRESHOLD_SECONDS = (10.0 / 130.0) * 3600.0;

    // 10 km / 120 km/h * 3600 s/h  ->  two such segments in one journey trip Rule B
    private static final double MULTI_SEGMENT_THRESHOLD_SECONDS  = (10.0 / 120.0) * 3600.0;

    private final List<LogEntry> entries;

    public LogFile(List<LogEntry> entries) {
        this.entries = entries;
    }

    public static LogFile fromFile(String path) throws IOException {
        List<LogEntry> parsed = Files.lines(Paths.get(path))
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                .map(LogEntry::new)
                .collect(Collectors.toList());
        return new LogFile(parsed);
    }

    public static LogFile fromText(String rawText) {
        List<LogEntry> parsed = new ArrayList<>();
        for (String line : rawText.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            parsed.add(new LogEntry(trimmed));
        }
        return new LogFile(parsed);
    }
    public List<LogEntry> getEntries() {
        return entries;
    }
    public int countJourneys() {
        int numberOfCompleteJourney = 0;
        for (LogEntry entry : getEntries()) {
            if (entry.getBoothType().equals("ENTRY")) {
                numberOfCompleteJourney++;
            }
        }
        return numberOfCompleteJourney;
    }
    public Map<String, Integer> catchSpeeders() {
        List<LogEntry> sortedEntries = new ArrayList<>(getEntries());
        sortedEntries.sort(Comparator.comparingDouble(LogEntry::getTimestamp));

        Map<String, List<LogEntry>> entriesByPlate = new HashMap<>();
        for (LogEntry entry : sortedEntries) {
            entriesByPlate
                .computeIfAbsent(entry.getLicensePlate(), k -> new ArrayList<>())
                .add(entry);
        }
        Map<String, Integer> violatingJourneysByPlate = new HashMap<>();
        for (Map.Entry<String, List<LogEntry>> plateEntries : entriesByPlate.entrySet()) {
            String plate = plateEntries.getKey();
            List<LogEntry> currentJourney = new ArrayList<>();
            int violations = 0;
            for (LogEntry entry : plateEntries.getValue()) {
                String boothType = entry.getBoothType();
                if ("ENTRY".equals(boothType)) {
                    currentJourney = new ArrayList<>();
                    currentJourney.add(entry);
                } else if ("MAINROAD".equals(boothType)) {
                    currentJourney.add(entry);
                } else if ("EXIT".equals(boothType)) {
                    currentJourney.add(entry);
                    if (isViolatingJourney(currentJourney)) {
                        violations++;
                    }
                }
            }
            if (violations > 0) {
                violatingJourneysByPlate.put(plate, violations);
            }
        }
        return violatingJourneysByPlate;
    }

    private static boolean isViolatingJourney(List<LogEntry> journey) {
        int segmentsAt120OrAbove = 0;
        for (int i = 1; i < journey.size(); i++) {
            double dt = journey.get(i).getTimestamp() - journey.get(i - 1).getTimestamp();
            if (dt <= SINGLE_SEGMENT_THRESHOLD_SECONDS) {
                return true;
            }
            if (dt <= MULTI_SEGMENT_THRESHOLD_SECONDS) {
                segmentsAt120OrAbove++;
                if (segmentsAt120OrAbove >= 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
