import java.util.*;

/**
 * LeetCode 349 - Intersection of Two Arrays
 * https://leetcode.com/problems/intersection-of-two-arrays/
 *
 * Return unique elements common to both arrays.
 *
 * Approach: HashMap frequency map — build from nums1, scan nums2
 * - Mark visited with -1 to avoid duplicates in result
 * - Could also use HashSet (simpler since only existence matters)
 *
 * Bug hit: Used nums1[i] instead of nums2[i] when marking visited
 * in second loop — wrong array reference with shared loop variable name.
 *
 * Time: O(n + m) | Space: O(n)
 */
class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {
        List<Integer> res = new ArrayList<>();
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int i = 0; i < nums1.length; i++) {
            frequencyMap.put(nums1[i], frequencyMap.getOrDefault(nums1[i], 0) + 1);
        }

        for (int i = 0; i < nums2.length; i++) {
            if (frequencyMap.containsKey(nums2[i])) {
                Integer value = frequencyMap.get(nums2[i]);
                if (value != -1) {
                    res.add(nums2[i]);
                    frequencyMap.put(nums2[i], -1); // mark visited
                }
            }
        }

        int[] resultant = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            resultant[i] = res.get(i);
        }
        return resultant;
    }
}
