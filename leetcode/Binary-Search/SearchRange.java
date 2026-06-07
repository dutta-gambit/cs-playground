/**
 * LeetCode 34 - Find First and Last Position of Element in Sorted Array
 * (a.k.a. InterviewBit "Search for a Range")
 * https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 *
 * Problem: sorted array with duplicates; return [firstIndex, lastIndex] of
 * target, or [-1, -1] if absent. Must run in O(log n).
 *
 * Approach: two boundary searches (the bisect pair).
 *   first = bisectLeft(target)        -> leftmost i with nums[i] >= target
 *   last  = bisectRight(target) - 1   -> rightmost i with nums[i] == target
 * If target is absent, bisectLeft lands where it WOULD be inserted, so a
 * single presence check decides [-1, -1].
 *
 * Key correctness trap — BOUNDARY OUT-OF-BOUNDS:
 *   A bisect returns an index in the CLOSED range [0, length].
 *     bisectLeft  can return `length` when target > every element.
 *     bisectRight can return `length` when target >= every element
 *                 (including when target IS the last element: [1], 1 -> 1).
 *   So the boundary helpers must NEVER dereference nums[result] — they return
 *   the raw index. (Putting a `nums[low] == target ? low : -1` check inside a
 *   helper throws AIOOBE exactly at low == length, e.g. [1], target 1.)
 *   Do the presence check in ONE place (here) and guard the index against
 *   length BEFORE touching the array:
 *     WRONG: if (nums.length == 0 || nums[first] != target)  // misses target>all
 *     RIGHT: if (first == nums.length || nums[first] != target)
 *   The first form still crashes on e.g. [1,2,3], target 5 (first == 3 == length,
 *   length != 0, so nums[3] throws). Compare the index to length, not the array.
 *
 * Time: O(log n) | Space: O(1)
 */
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int first = bisectLeft(nums, target);
        if (first == nums.length || nums[first] != target) {
            return new int[]{-1, -1};
        }
        int last = bisectRight(nums, target) - 1;
        return new int[]{first, last};
    }

    // leftmost index i with nums[i] >= target, in [0, length]
    private int bisectLeft(int[] nums, int target) {
        int low = 0, high = nums.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] < target) low = mid + 1;
            else high = mid;
        }
        return low;
    }

    // leftmost index i with nums[i] > target, in [0, length]
    private int bisectRight(int[] nums, int target) {
        int low = 0, high = nums.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] <= target) low = mid + 1;
            else high = mid;
        }
        return low;
    }
}
