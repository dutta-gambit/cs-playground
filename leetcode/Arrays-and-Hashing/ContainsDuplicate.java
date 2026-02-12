import java.util.Set;
import java.util.HashSet;

/**
 * LeetCode 217 - Contains Duplicate
 * https://leetcode.com/problems/contains-duplicate/
 *
 * Approach: HashSet for O(1) existence check
 * Time:  O(n) — single pass through array
 * Space: O(n) — HashSet stores up to n elements
 *
 * Key learnings:
 * - Set vs Map: Use Set when you only need "seen or not seen"
 * - Set.add() returns false if element already exists (combines check + insert)
 * - Alternative: Sort first (O(n log n) time, O(1) space) — check adjacent elements
 */
class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> setOfVals = new HashSet<>();

        for (int i : nums) {
            if (!setOfVals.add(i)) {
                return true;
            }
        }
        return false;
    }
}
