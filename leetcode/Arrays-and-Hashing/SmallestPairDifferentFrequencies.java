import java.util.*;

/**
 * Smallest Pair With Different Frequencies (Easy)
 *
 * Find the smallest pair [x, y] where x < y and freq(x) != freq(y).
 * Smallest x first, then smallest y.
 *
 * Approach: Frequency map + sorted distinct pairs scan
 * 1. Count frequencies with HashMap
 * 2. Sort array → iterate over distinct pairs in order
 * 3. First valid pair found = answer (guaranteed smallest x, then smallest y)
 *
 * Time: O(n log n) for sorting. Space: O(n) for frequency map.
 *
 * Bugs hit in first attempt:
 * - Used Map.add() instead of Map.put() (add doesn't exist on HashMap)
 * - Typo: nums.lenth → nums.length
 * - i never incremented when j reached end → infinite loop
 * - Missing return [-1, -1] for no valid pair case
 */
class Solution {
    public int[] minDistinctFreqPair(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1])
                continue; // skip duplicate x
            for (int j = i + 1; j < nums.length; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1])
                    continue; // skip duplicate y
                if (!freq.get(nums[i]).equals(freq.get(nums[j]))) {
                    return new int[] { nums[i], nums[j] };
                }
            }
        }

        return new int[] { -1, -1 };
    }
}
