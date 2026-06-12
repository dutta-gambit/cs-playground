package com.karat.ancestry;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Part 1.
 *
 * Implement FamilyTree#findIndividualsWithZeroOrOneParent() so it returns:
 *   result.get(0) -> all individuals with ZERO parents, ascending
 *   result.get(1) -> all individuals with EXACTLY ONE parent, ascending
 *
 * Individuals with 2+ parents must NOT appear in either list.
 *
 * Bundled tree (src/main/resources/pairs.log):
 *   parents of 2 = {1, 3}    -> 2 has TWO parents          -> in neither list
 *   parents of 4 = {2, 5}    -> 4 has TWO parents          -> in neither list
 *   parents of 5 = {7}                                     -> result.get(1)
 *   parents of 6 = {5}                                     -> result.get(1)
 *   parents of 8 = {7}                                     -> result.get(1)
 *   parents of 9 = {8}                                     -> result.get(1)
 *   individuals 1, 3, 7 never appear as a child            -> result.get(0)
 *
 * Expected: [[1, 3, 7], [5, 6, 8, 9]]
 */
public class Part1ZeroAndOneParentTest {

    private static final String LOG_PATH = "src/main/resources/pairs.log";

    @Test
    public void shouldReturnAllOrphansInSampleData() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        List<List<Integer>> result = tree.findIndividualsWithZeroOrOneParent();
        assertEquals(Arrays.asList(1, 3, 7), result.get(0));
    }

    @Test
    public void shouldReturnAllSingleParentIndividualsInSampleData() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        List<List<Integer>> result = tree.findIndividualsWithZeroOrOneParent();
        assertEquals(Arrays.asList(5, 6, 8, 9), result.get(1));
    }

    @Test
    public void individualsWithTwoParentsShouldAppearInNeitherList() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        List<List<Integer>> result = tree.findIndividualsWithZeroOrOneParent();
        // 2 has parents {1, 3}; 4 has parents {2, 5}
        assertFalse("2 has two parents, must not be in zero-parent list", result.get(0).contains(2));
        assertFalse("2 has two parents, must not be in one-parent list", result.get(1).contains(2));
        assertFalse("4 has two parents, must not be in zero-parent list", result.get(0).contains(4));
        assertFalse("4 has two parents, must not be in one-parent list", result.get(1).contains(4));
    }

    @Test
    public void bothListsShouldBeSortedAscending() throws IOException {
        FamilyTree tree = FamilyTree.fromFile(LOG_PATH);
        List<List<Integer>> result = tree.findIndividualsWithZeroOrOneParent();

        for (List<Integer> list : result) {
            for (int i = 1; i < list.size(); i++) {
                assertTrue("Result must be sorted ascending; got " + list,
                        list.get(i - 1) <= list.get(i));
            }
        }
    }

    @Test
    public void rootOnlyIndividualShouldAppearInZeroParentList() {
        // A graph with one parent and one child:
        //   10 -> 11
        // 10 has 0 parents; 11 has 1 parent.
        FamilyTree tree = FamilyTree.fromPairs(new int[][]{{10, 11}});
        List<List<Integer>> result = tree.findIndividualsWithZeroOrOneParent();
        assertEquals(Arrays.asList(10), result.get(0));
        assertEquals(Arrays.asList(11), result.get(1));
    }

    @Test
    public void everyoneWithTwoParentsCollapsesToEmptyResult() {
        // 1 -> 3, 2 -> 3 — only individual is 3 (two parents), 1 and 2 have zero parents.
        FamilyTree tree = FamilyTree.fromPairs(new int[][]{{1, 3}, {2, 3}});
        List<List<Integer>> result = tree.findIndividualsWithZeroOrOneParent();
        assertEquals(Arrays.asList(1, 2), result.get(0));
        assertTrue("3 has two parents, single-parent list must not contain it",
                result.get(1).isEmpty());
    }
}
