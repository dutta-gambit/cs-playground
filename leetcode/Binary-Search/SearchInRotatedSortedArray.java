/**
 * LeetCode 33 - Search in Rotated Sorted Array
 * https://leetcode.com/problems/search-in-rotated-sorted-array/
 *
 * Problem: A sorted array of DISTINCT ints is rotated at an unknown pivot.
 * Return the index of target, or -1. Must run in O(log n).
 *
 * Template: EXACT MATCH, so the closed-interval package —
 *   while (low <= high), a live `if (nums[mid] == target) return mid`,
 *   both pointers step over mid (mid+1 / mid-1), and `return -1` when the
 *   window empties. (Contrast the boundary/convergence package used by
 *   find-min: while(low<high), bare high=mid, return low.)
 *
 * Mental model — "one half is always sorted":
 *   A rotated sorted array has exactly one cliff, so at any mid the array
 *   splits into one CLEAN (cliff-free, fully sorted) half and one half that
 *   still hides the cliff. You can only range-check the clean half, because
 *   only there do you have BOTH its endpoints to bound the target.
 *
 * Cliff locator (anchored on nums[high], the same anchor as find-min):
 *   - nums[mid] <= nums[high]  -> mid & high share a run -> RIGHT half
 *                                 [mid..high] is the clean, sorted one.
 *   - nums[mid] >  nums[high]  -> a cliff sits between them -> LEFT half
 *                                 [low..mid] is the clean, sorted one.
 *   (Why airtight: if mid and high shared a run, mid<=high would force
 *    nums[mid] <= nums[high]; so a strict nums[mid] > nums[high] can only
 *    mean the cliff is to mid's right.)
 *
 * Decide, then move (target already != nums[mid] thanks to the early return):
 *   - right half clean: target in (nums[mid], nums[high]] ? go right : go left
 *   - left  half clean: target in [nums[low], nums[mid]) ? go left  : go right
 * Every check bounds target with TWO endpoints of a PROVABLY sorted half —
 * a single-endpoint comparison cannot localize a target in a rotated array.
 *
 * Time: O(log n) | Space: O(1)
 */
class Solution {
    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) return mid;

            if (nums[mid] <= nums[high]) {                       // right half [mid..high] sorted
                if (nums[mid] < target && target <= nums[high]) low = mid + 1;
                else                                            high = mid - 1;
            } else {                                             // left half [low..mid] sorted
                if (nums[low] <= target && target < nums[mid])  high = mid - 1;
                else                                            low = mid + 1;
            }
        }
        return -1;
    }
}
