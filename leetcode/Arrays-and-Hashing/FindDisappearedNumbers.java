import java.util.*;

/**
 * LeetCode 448 - Find All Numbers Disappeared in an Array
 * https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
 *
 * Given array of n integers where nums[i] is in [1, n], return all numbers in [1, n] that are missing.
 *
 * Approach 1: HashSet — O(n) time, O(n) space
 *   - Add all nums to set, check which 1..n are missing
 *
 * Approach 2: Negation trick — O(n) time, O(1) extra space
 *   - Values are in [1, n], so each value maps to a valid index (value - 1)
 *   - Negate nums[index] to mark "this value exists"
 *   - Any index still positive = that number is missing
 *   - Math.abs() prevents issues from previously negated values
 *   - if (nums[index] > 0) prevents double-negating for duplicates
 */

// --- Approach 1: HashSet (O(n) space) ---
// class Solution {
//     public List<Integer> findDisappearedNumbers(int[] nums) {
//         List<Integer> result = new ArrayList<>();
//         Set<Integer> hashSet = new HashSet<>();
//         for (int i : nums) hashSet.add(i);
//         for (int i = 1; i <= nums.length; i++) {
//             if (!hashSet.contains(i)) result.add(i);
//         }
//         return result;
//     }
// }

// --- Approach 2: Negation trick (O(1) extra space) ---
class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> missingItems = new ArrayList<>();

        // Pass 1: mark each value's index by negating
        for (int i = 0; i < nums.length; i++) {
            int index = Math.abs(nums[i]) - 1;   // value → index (-1 for 0-based)
            if (nums[index] > 0) {
                nums[index] = -(nums[index]);     // negate to mark "exists"
            }
        }

        // Pass 2: positive indices = missing numbers
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                missingItems.add(i + 1);          // index → value (+1 for 1-based)
            }
        }
        return missingItems;
    }
}
