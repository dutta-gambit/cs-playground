/**
 * LeetCode 81 - Search in Rotated Sorted Array II (with DUPLICATES)
 * https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
 *
 * Problem: Same as LC 33 but values may REPEAT. Return whether target exists
 * (boolean). Duplicates break the worst-case O(log n) guarantee.
 *
 * What duplicates change — ONE thing: the tie nums[mid] == nums[high].
 *   With distinct values, comparing nums[mid] vs nums[high] is a perfect
 *   cliff locator (a STRICT < or > can never straddle the cliff, because
 *   every high-run element >= every low-run element). The strict branches
 *   stay 100% correct here too. But nums[mid] == nums[high] is now
 *   AMBIGUOUS: the equal endpoints could be two copies with a cliff hidden
 *   between them, e.g. [1,1,1,0,1] — mid and high are both 1, yet [mid..high]
 *   is NOT sorted. Trusting "<=" here would discard the half that holds the
 *   answer (false negative).
 *
 * The fix — peel the ambiguous end:
 *   when nums[mid] == nums[high], do high-- and retry.
 *   Safe because we already returned on nums[mid] == target, so
 *   nums[high] (== nums[mid]) is NOT the target — dropping that one index
 *   loses no answer, and [low..high-1] is still a valid rotated slice.
 *   Cost: an adversary like [1,1,1,...,1] forces one-at-a-time peeling ->
 *   O(n) worst case. The duplicates literally erase the signal binary
 *   search needs; there is no way around it. (Same medicine as LC 154's
 *   high-- for the duplicate minimum.)
 *
 * Note: the strict-< and strict-> branches are IDENTICAL to LC 33. Distinct
 * arrays never hit the tie branch, so this one method is correct for both
 * LC 33 and LC 81 — only the leading `== nums[high]` guard is new.
 *
 * Time: O(log n) average, O(n) worst case (all-equal) | Space: O(1)
 */
class Solution {
    public boolean search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) return true;

            if (nums[mid] == nums[high]) {                       // tie -> which half is sorted is unknowable
                high--;                                          // safe: nums[high] == nums[mid] != target
            } else if (nums[mid] < nums[high]) {                 // right half [mid..high] sorted
                if (nums[mid] < target && target <= nums[high])  low = mid + 1;
                else                                             high = mid - 1;
            } else {                                             // nums[mid] > nums[high] -> left half [low..mid] sorted
                if (nums[low] <= target && target < nums[mid])   high = mid - 1;
                else                                             low = mid + 1;
            }
        }
        return false;
    }
}
