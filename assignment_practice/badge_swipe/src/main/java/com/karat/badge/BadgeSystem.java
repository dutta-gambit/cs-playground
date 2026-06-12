package com.karat.badge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * In-memory representation of a day's badge-swipe log.
 *
 * Two analytical operations:
 *   - {@link #findViolators()}            (Part 2)
 *   - {@link #bestViolationWindow()}      (Part 3)
 *
 * "Violation" definition: an employee badged in three or more times within
 * ANY rolling one-hour window. The window is inclusive at both ends — a swipe
 * at t and another at t + 60 minutes are both inside the same window.
 */
public class BadgeSystem {

    private final List<SwipeRecord> records;

    public BadgeSystem(List<SwipeRecord> records) {
        this.records = records;
    }

    public static BadgeSystem fromFile(String path) throws IOException {
        List<SwipeRecord> parsed = Files.lines(Paths.get(path))
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                .map(BadgeSystem::parseLine)
                .collect(Collectors.toList());
        return new BadgeSystem(parsed);
    }

    public static BadgeSystem fromText(String rawText) {
        List<SwipeRecord> parsed = new ArrayList<>();
        for (String line : rawText.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            parsed.add(parseLine(trimmed));
        }
        return new BadgeSystem(parsed);
    }

    private static SwipeRecord parseLine(String line) {
        String[] tokens = line.split("\\s+");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Malformed swipe line: " + line);
        }
        return new SwipeRecord(tokens[0], tokens[1]);
    }

    public List<SwipeRecord> getRecords() {
        return records;
    }

    /**
     * Part 2.
     *
     * Returns the names of employees who badged in three or more times within
     * any single rolling one-hour window. Output is sorted alphabetically.
     *
     * An employee with zero qualifying windows MUST NOT appear in the list.
     *
     * TODO: implement.
     */

    public List<String> findViolators() {

        List<SwipeRecord> rSwipeRecords = getRecords();

        List<String> result = new ArrayList<>();
        Map<String, List<Integer>> groupByPerson = new TreeMap<>();

        for(SwipeRecord record : rSwipeRecords){
            groupByPerson.computeIfAbsent(record.getName(), k -> new ArrayList()).add(record.getMinutesSinceMidnight());
        }

      for(Map.Entry<String, List<Integer>> entry : groupByPerson.entrySet()){
            String personName = entry.getKey();
            List<Integer> numberOfBadged = entry.getValue();
            numberOfBadged.sort(Comparator.naturalOrder());
            if(numberOfBadged.size() > 2){
                for(int i = 2 ; i < numberOfBadged.size(); i++){
                    if(numberOfBadged.get(i) - numberOfBadged.get(i-2) <= 60){
                        if(!result.contains(personName)){
                            result.add(personName);
                        }
                    }

                }
            }
        }

        return result;
    }

    /**
     * Part 3.
     *
     * Returns the BEST one-hour window per violator — the window holding the
     * maximum number of swipes for that employee. Ties broken by earliest
     * window start.
     *
     * The value list contains the raw "HH:MM" timestamps of every swipe inside
     * that winning window, in chronological order.
     *
     * Non-violators (fewer than 3 swipes in every 1-hour window) MUST NOT
     * appear in the returned map.
     *
     * TODO: implement.
     */
    public Map<String, List<String>> bestViolationWindow() {
          List<SwipeRecord> records = getRecords();
          Map<String, List<String>> result = new HashMap<>();
          Map<String, List<SwipeRecord>> groupBySwap = new HashMap<>();
          
          for(SwipeRecord record : records){
            groupBySwap.computeIfAbsent(record.getName(), k -> new ArrayList<>()).add(record);
          }
        for(Map.Entry<String, List<SwipeRecord>> entry : groupBySwap.entrySet()){
            String personName = entry.getKey();

            List<SwipeRecord> rSwipeRecords = entry.getValue();
            rSwipeRecords.sort(Comparator.comparingInt(SwipeRecord::getMinutesSinceMidnight));

            int bestStart = -1;
            int bestEnd = -1;
            int bestCount = 0;

            
            for(int i = 0; i < rSwipeRecords.size(); i++){

                int startMinute = rSwipeRecords.get(i).getMinutesSinceMidnight();
                int count = 0;

                for(int j = i; j < rSwipeRecords.size(); j++){

                    if(rSwipeRecords.get(j).getMinutesSinceMidnight() - startMinute <= 60){
                        count++;
                    }
                    else{
                        break;
                    }

                }
                if(count> bestCount){
                    bestCount = count;
                    bestStart = i;
                    bestEnd = i + count - 1;
                }

            }
            if(bestCount >= 3){
                List<String> timeStrings = new ArrayList<>();
                for(int k = bestStart; k <= bestEnd; k++){
                    timeStrings.add(rSwipeRecords.get(k).getTime());
                }
                result.put(personName, timeStrings);
            } 
        }
        return result;
    }
}
