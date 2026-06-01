/**
 * GeeksforGeeks - Find Kth Rotation
 * (Algorithmic sibling of LeetCode 153 - Find Minimum in Rotated Sorted Array;
 *  here we return the INDEX of the minimum, which equals the rotation count k.)
 * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
 *
 * Problem: An increasing sorted array of DISTINCT ints is right-rotated k times.
 * Return k. Right-rotating moves the minimum to index k, so k = index of the minimum.
 *
 * Mental model — two lines:
 *   A rotated sorted array is two increasing runs. The left ("high") run sits
 *   ENTIRELY above the right ("low") run; the minimum is the foot of the cliff
 *   between them = the first element of the low run. (Rotation moves the large
 *   tail of the sorted array to the front, so the high run is the big values.)
 *
 * Approach — boundary / first-true binary search, anchored on arr[high]
 * (the right end is always on the low run, so it's a reliable reference):
 *   - arr[mid] > arr[high]  -> mid is on the HIGH run, cliff is to the right
 *                              -> low = mid + 1   (mid is provably not the min)
 *   - arr[mid] < arr[high]  -> mid is on the LOW run, min is at mid or left
 *                              -> high = mid      (KEEP mid; it might be the min)
 * Converge until low == high; that index is k. No exact-match check, and no
 * special case for the unrotated (k = 0) array — the loop invariant handles it.
 *
 * Equivalent framing: bisect_left on the predicate "arr[i] < arr[last]"
 * (false on the high run, true on the low run — flips exactly once, at the min).
 *
 * Time: O(log n) | Space: O(1)
 */
class Solution {
    public int findKRotation(int arr[]) {
        int low = 0;
        int high = arr.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] > arr[high]) {
                low = mid + 1;
            } else if (arr[mid] < arr[high]) {
                high = mid;
            }
        }

        return low;
    }
}
