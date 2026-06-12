package com.karat.ancestry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * In-memory representation of a multi-generation family tree.
 *
 * Three analytical operations:
 *   - {@link #findIndividualsWithZeroOrOneParent()}   (Part 1)
 *   - {@link #hasCommonAncestor(int, int)}            (Part 2)
 *   - {@link #earliestAncestor(int)}                  (Part 3)
 */
public class FamilyTree {

    private final List<ParentChildPair> pairs;

    public FamilyTree(List<ParentChildPair> pairs) {
        this.pairs = pairs;
    }

    public static FamilyTree fromFile(String path) throws IOException {
        List<ParentChildPair> parsed = Files.lines(Paths.get(path))
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                .map(FamilyTree::parseLine)
                .collect(Collectors.toList());
        return new FamilyTree(parsed);
    }

    public static FamilyTree fromText(String rawText) {
        List<ParentChildPair> parsed = new ArrayList<>();
        for (String line : rawText.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            parsed.add(parseLine(trimmed));
        }
        return new FamilyTree(parsed);
    }

    public static FamilyTree fromPairs(int[][] arr) {
        List<ParentChildPair> parsed = new ArrayList<>();
        for (int[] p : arr) {
            if (p.length != 2) {
                throw new IllegalArgumentException("Each pair must be [parent, child], got length " + p.length);
            }
            parsed.add(new ParentChildPair(p[0], p[1]));
        }
        return new FamilyTree(parsed);
    }

    private static ParentChildPair parseLine(String line) {
        String[] tokens = line.split("\\s+");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Malformed pair line: " + line);
        }
        return new ParentChildPair(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }

    public List<ParentChildPair> getPairs() {
        return pairs;
    }

    /**
     * Part 1.
     *
     * Returns two ascending lists wrapped in a single outer list:
     *   - result.get(0): individuals with ZERO known parents
     *   - result.get(1): individuals with EXACTLY ONE known parent
     *
     * Individuals with two or more parents must appear in NEITHER list.
     *
     * TODO: implement.
     */
    public List<List<Integer>> findIndividualsWithZeroOrOneParent() {
        Set<Integer> everyone = new HashSet<>();
        Map<Integer, Integer> parentCount = new HashMap<>();

        for (ParentChildPair pair : pairs) {
            everyone.add(pair.getParent());
            everyone.add(pair.getChild());
            parentCount.merge(pair.getChild(), 1, Integer::sum);
        }

        List<Integer> zeroParents = new ArrayList<>();
        List<Integer> oneParent   = new ArrayList<>();

        for (int individual : everyone) {
            int count = parentCount.getOrDefault(individual, 0);
            if (count == 0) {
                zeroParents.add(individual);
            } else if (count == 1) {
                oneParent.add(individual);
            }
        }

        Collections.sort(zeroParents);
        Collections.sort(oneParent);

        List<List<Integer>> result = new ArrayList<>();
        result.add(zeroParents);
        result.add(oneParent);
        return result;
    }

    /**
     * Part 2.
     *
     * Returns true if the two individuals share at least one common ancestor.
     *
     * An individual is NOT considered an ancestor of themselves: if A is an
     * ancestor of B but neither has any further parents in common, the result
     * is false (because A's ancestor set is empty).
     *
     * TODO: implement.
     */
    public boolean hasCommonAncestor(int a, int b) {
        Map<Integer, List<Integer>> childToParents = buildChildToParents();
        Set<Integer> ancestorsA = ancestorsOf(a, childToParents);
        Set<Integer> ancestorsB = ancestorsOf(b, childToParents);
        return !Collections.disjoint(ancestorsA, ancestorsB);
    }

    private Map<Integer, List<Integer>> buildChildToParents() {
        Map<Integer, List<Integer>> index = new HashMap<>();
        for (ParentChildPair pair : pairs) {
            index.computeIfAbsent(pair.getChild(), k -> new ArrayList<>())
                 .add(pair.getParent());
        }
        return index;
    }

    private static Set<Integer> ancestorsOf(int individual,
                                            Map<Integer, List<Integer>> childToParents) {
        Set<Integer> ancestors = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(individual);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int parent : childToParents.getOrDefault(current, Collections.emptyList())) {
                if (ancestors.add(parent)) {       // returns true iff newly added
                    queue.offer(parent);
                }
            }
        }
        return ancestors;
    }

    /**
     * Part 3.
     *
     * Returns the EARLIEST known ancestor of the individual — defined as the
     * ancestor at the FARTHEST distance up the tree from the individual.
     *
     * On ties (multiple ancestors share the same max distance), any of them
     * is acceptable.
     *
     * Returns -1 if the individual has no parents.
     *
     * TODO: implement.
     */
    public int earliestAncestor(int individual) {
        Map<Integer, List<Integer>> childToParents = buildChildToParents();

        Set<Integer> visited = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(individual);

        List<Integer> lastLevel = new ArrayList<>();

        while (!queue.isEmpty()) {
            int levelSize = queue.size();                 // freeze count for THIS wave
            List<Integer> currentLevel = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                int current = queue.poll();
                for (int parent : childToParents.getOrDefault(current, Collections.emptyList())) {
                    if (visited.add(parent)) {            // dedup + cycle guard
                        currentLevel.add(parent);
                        queue.offer(parent);
                    }
                }
            }

            if (!currentLevel.isEmpty()) {
                lastLevel = currentLevel;                 // overwrite — only keep the deepest wave
            }
        }

        return lastLevel.isEmpty() ? -1 : lastLevel.get(0);
    }
}
