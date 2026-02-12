import java.util.Map;
import java.util.HashMap;

/**
 * LeetCode 1 - Two Sum
 * https://leetcode.com/problems/two-sum/
 *
 * Approach: Single-pass HashMap (value → index)
 * - For each num, check if complement (target - num) exists in map
 * - If found, return both indices. If not, store current value → index
 * - Check BEFORE insert → prevents self-matching
 *
 * Time:  O(n) — single pass
 * Space: O(n) — HashMap stores up to n entries
 *
 * Key learnings:
 * - Map<value, index> pattern: "where did I see this value?"
 * - Single-pass: check before insert prevents self-matching
 * - complement = target - nums[i] is the core insight
 * - Different from Set problems: need to return indices, not just existence
 */
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> mapOfVals = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (mapOfVals.containsKey(target - nums[i])) {
                return new int[]{mapOfVals.get(target - nums[i]), i};
            }
            mapOfVals.put(nums[i], i);
        }

        return new int[]{}; // problem guarantees exactly one solution
    }
}
