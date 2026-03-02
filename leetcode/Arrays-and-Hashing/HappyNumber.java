import java.util.*;

/**
 * LeetCode 202 - Happy Number
 * https://leetcode.com/problems/happy-number/
 *
 * Repeatedly replace n with sum of squares of its digits.
 * Return true if it reaches 1, false if it enters a cycle.
 *
 * Approach: HashSet cycle detection + digit extraction
 * - Inner loop: extract digits with % 10, / 10, sum squares
 * - Outer loop: repeat until sum == 1 (happy) or sum seen before (cycle)
 * - Set tracks complete sums (not partial digit sums!)
 *
 * Bugs hit:
 * - Set check inside inner loop (checked partial sums, not complete sum)
 * - Missing n = sum to continue with new number
 * - Missing sum == 1 check
 *
 * Time: O(log n) per iteration | Space: O(k) where k = unique sums before cycle
 */
class Solution {
    public boolean isHappy(int n) {
        Set<Integer> seen = new HashSet<>();

        while (true) {
            int sum = 0;
            while (n > 0) {
                int digit = n % 10;
                sum += digit * digit;
                n /= 10;
            }
            if (sum == 1)
                return true;
            if (seen.contains(sum))
                return false;
            seen.add(sum);
            n = sum;
        }
    }
}
