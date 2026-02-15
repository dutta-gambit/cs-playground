/**
 * LeetCode 238 - Product of Array Except Self
 * https://leetcode.com/problems/product-of-array-except-self/
 *
 * Approach 1: Two arrays (prefix + suffix) → O(n) time, O(n) space
 * Approach 2: Single pass optimization → O(n) time, O(1) extra space
 *
 * Key insight: result[i] = product of everything LEFT of i × product of everything RIGHT of i
 * prefix[i] = product of nums[0..i-1] (everything BEFORE i, not including i)
 * suffix[i] = product of nums[i+1..n-1] (everything AFTER i, not including i)
 */

// --- Approach 1: Two arrays ---
// class Solution {
//     public int[] productExceptSelf(int[] nums) {
//         int[] prefixProduct = new int[nums.length];
//         int[] suffixProduct = new int[nums.length];
//
//         prefixProduct[0] = 1;
//         for (int i = 1; i < nums.length; i++) {
//             prefixProduct[i] = prefixProduct[i - 1] * nums[i - 1];
//         }
//
//         suffixProduct[nums.length - 1] = 1;
//         for (int i = nums.length - 2; i >= 0; i--) {
//             suffixProduct[i] = nums[i + 1] * suffixProduct[i + 1];
//         }
//
//         for (int i = 0; i < nums.length; i++) {
//             nums[i] = prefixProduct[i] * suffixProduct[i];
//         }
//         return nums;
//     }
// }

// --- Approach 2: O(1) extra space ---
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] res = new int[nums.length];

        // Pass 1: fill res with prefix products (left to right)
        res[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            res[i] = res[i - 1] * nums[i - 1];
        }

        // Pass 2: multiply in suffix products (right to left)
        int suffix = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            suffix *= nums[i + 1];
            res[i] *= suffix;
        }

        return res;
    }
}
