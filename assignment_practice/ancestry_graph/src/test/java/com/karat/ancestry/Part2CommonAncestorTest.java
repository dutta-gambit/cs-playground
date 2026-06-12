package com.karat.ancestry;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Part 2.
 *
 * Implement FamilyTree#hasCommonAncestor(a, b) so it returns true iff
 * individuals a and b share at least one ancestor.
 *
 * Reminder: an individual is NOT considered an ancestor of themselves.
 * So hasCommonAncestor(1, 2) where 1 -> 2 returns FALSE because 1's
 * ancestor set is empty.
 */
public class Part2CommonAncestorTest {

    private static final String LOG_PATH = "src/main/resources/pairs.log";

    @Test
    public void halfSiblingsThroughDifferentMothersStillShareAncestors() throws IOException {
        // 4 and 6 share both 5 (4's parent via one branch, 6's parent) and 7 (5's parent).
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertTrue("4 and 6 share ancestors 5 and 7", tree.hasCommonAncestor(4, 6));
    }

    @Test
    public void cousinsAcrossBranchesShouldShareAncestor() throws IOException {
        // 4's lineage: 2, 5, 1, 3, 7.   9's lineage: 8, 7.   Common: 7.
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertTrue("4 and 9 share ancestor 7", tree.hasCommonAncestor(4, 9));
    }

    @Test
    public void siblingsWithCommonGrandparentShouldShareAncestor() throws IOException {
        // 6's lineage: 5, 7.   9's lineage: 8, 7.   Common: 7.
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertTrue("6 and 9 share ancestor 7", tree.hasCommonAncestor(6, 9));
    }

    @Test
    public void twoRootIndividualsHaveNoCommonAncestor() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertFalse("1 and 3 are both roots; no ancestors at all",
                tree.hasCommonAncestor(1, 3));
        assertFalse("1 and 7 are both roots; no ancestors at all",
                tree.hasCommonAncestor(1, 7));
    }

    @Test
    public void parentAndChildPairAloneDoNotShareAnAncestor() {
        // 1 -> 2.  Ancestors of 1: {}.  Ancestors of 2: {1}.  Intersection: empty.
        FamilyTree tree = FamilyTree.fromPairs(new int[][]{{1, 2}});
        assertFalse("1 is 2's parent, not their common ancestor",
                tree.hasCommonAncestor(1, 2));
    }

    @Test
    public void parentAndChildShareAncestorIfParentHasItsOwnParents() throws IOException {
        // 2 and 4: ancestors of 2 = {1, 3}, ancestors of 4 = {2, 5, 1, 3, 7}.
        // Common: {1, 3}.  Even though 2 is also 4's ancestor, the result is true
        // because they share a grandparent of 4.
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        assertTrue("2 and 4 share grandparents 1 and 3",
                tree.hasCommonAncestor(2, 4));
    }

    @Test
    public void disconnectedSubtreesShouldNotShareAncestor() {
        // Subtree A: 10 -> 11.   Subtree B: 20 -> 21.   No overlap.
        FamilyTree tree = FamilyTree.fromPairs(new int[][]{{10, 11}, {20, 21}});
        assertFalse(tree.hasCommonAncestor(11, 21));
    }
}
