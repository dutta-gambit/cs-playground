/**
 * LeetCode 189 - Rotate Array
 * https://leetcode.com/problems/rotate-array/
 *
 * Rotate array right by k positions.
 *
 * Approach 1: Extra array — copy last k elements first, then remaining → O(n) time, O(n) space
 * Approach 2: Triple reverse — reverse all, reverse [0,k-1], reverse [k,n-1] → O(n) time, O(1) space
 * Approach 3: Cyclic replacement — follow the chain, each element → (i+k)%n → O(n) time, O(1) space
 *
 * Key insight: Array = [A | B], want [B | A]. Reverse all → [Bʳ|Aʳ], reverse each half → [B|A]
 */

// --- Approach 1: Extra array ---
// class Solution {
//     public void rotate(int[] nums, int k) {
//         int n = nums.length;
//         k = k % n;
//         int[] res = new int[n];
//         int z = 0;
//         for (int i = n - k; i < n; i++) res[z++] = nums[i];
//         for (int i = 0; i < n - k; i++) res[z++] = nums[i];
//         for (int i = 0; i < n; i++) nums[i] = res[i];
//     }
// }

// --- Approach 2: Triple reverse (interview-ready) ---
class Solution {
    public void rotate(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);  // reverse all
        reverse(nums, 0, k - 1);             // reverse first k
        reverse(nums, k, nums.length - 1);   // reverse rest
    }

    private void reverse(int[] nums, int i, int j) {
        while (i < j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
            i++; j--;
        }
    }
}

// --- Approach 3: Cyclic replacement ---
// class Solution {
//     public void rotate(int[] nums, int k) {
//         k = k % nums.length;
//         int count = 0;
//         for (int start = 0; count < nums.length; start++) {
//             int current = start;
//             int prev = nums[start];
//             do {
//                 int next = (current + k) % nums.length;
//                 int temp = nums[next];
//                 nums[next] = prev;
//                 prev = temp;
//                 current = next;
//                 count++;
//             } while (current != start);
//         }
//     }
// }
