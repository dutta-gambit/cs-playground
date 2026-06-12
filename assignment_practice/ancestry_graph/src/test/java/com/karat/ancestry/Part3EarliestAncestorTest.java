package com.karat.ancestry;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Part 3.
 *
 * Implement FamilyTree#earliestAncestor(individual). Definition:
 *   - "Earliest" means farthest distance UP the family tree.
 *   - If multiple ancestors share the same max distance, return ANY of them.
 *   - If the individual has no parents at all, return -1.
 *
 * Distances on the bundled tree:
 *   9 -> 8 (d=1) -> 7 (d=2).                              earliestAncestor(9) = 7
 *   6 -> 5 (d=1) -> 7 (d=2).                              earliestAncestor(6) = 7
 *   5 -> 7 (d=1).                                         earliestAncestor(5) = 7
 *   8 -> 7 (d=1).                                         earliestAncestor(8) = 7
 *   4 -> {2, 5} (d=1) -> {1, 3, 7} (d=2).                 earliestAncestor(4) in {1, 3, 7}
 *   2 -> {1, 3} (d=1).                                    earliestAncestor(2) in {1, 3}
 *   1, 3, 7 have no parents.                              earliestAncestor(*) = -1
 */
public class Part3EarliestAncestorTest {

    private static final String LOG_PATH = "src/main/resources/pairs.log";

    @Test
    public void earliestAncestorOfNineShouldBeSeven() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertEquals(7, tree.earliestAncestor(9));
    }

    @Test
    public void earliestAncestorOfSixShouldBeSeven() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertEquals(7, tree.earliestAncestor(6));
    }

    @Test
    public void earliestAncestorOfFiveShouldBeSeven() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertEquals(7, tree.earliestAncestor(5));
    }

    @Test
    public void earliestAncestorOfFourShouldBeOneOfTheTopLevelRoots() throws IOException {
        // 4's deepest ancestors are 1, 3 (via 2) and 7 (via 5) — all at depth 2.
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        int result = tree.earliestAncestor(4);
        List<Integer> acceptable = Arrays.asList(1, 3, 7);
        assertTrue("Expected one of " + acceptable + "; got " + result, acceptable.contains(result));
    }

    @Test
    public void earliestAncestorOfTwoShouldBeAParent() throws IOException {
        // 2's parents 1 and 3 have no further parents — both are at depth 1.
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        int result = tree.earliestAncestor(2);
        List<Integer> acceptable = Arrays.asList(1, 3);
        assertTrue("Expected 1 or 3; got " + result, acceptable.contains(result));
    }

    @Test
    public void rootIndividualsShouldReturnNegativeOne() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertEquals("1 has no parents", -1, tree.earliestAncestor(1));
        assertEquals("3 has no parents", -1, tree.earliestAncestor(3));
        assertEquals("7 has no parents", -1, tree.earliestAncestor(7));
    }

    @Test
    public void deeperChainShouldBubbleUpAllTheWay() {
        // 100 -> 200 -> 300 -> 400.   earliestAncestor(400) = 100.
        FamilyTree tree = FamilyTree.fromPairs(new int[][]{
            {100, 200}, {200, 300}, {300, 400}
        });
        assertEquals(100, tree.earliestAncestor(400));
    }

    @Test
    public void ancestorAtGreaterDepthShouldBeatOneCloser() {
        // Tree:
        //   10 -> 11 -> 12
        //         15 -> 12    (12 has parents {11, 15})
        // Depth from 12:
        //   d=1 : {11, 15}
        //   d=2 : {10}        ← winner
        FamilyTree tree = FamilyTree.fromPairs(new int[][]{
            {10, 11}, {11, 12}, {15, 12}
        });
        assertEquals(10, tree.earliestAncestor(12));
    }
}
