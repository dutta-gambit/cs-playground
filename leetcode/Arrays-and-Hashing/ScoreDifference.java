/**
 * LeetCode 3847 - Find the Score Difference in a Game
 *
 * Two players alternate. Swaps happen when:
 * 1. nums[i] is odd → swap
 * 2. Every 6th game (indices 5, 11, 17...) → swap
 * If both conditions met → they cancel out (XOR logic)
 *
 * Approach: Track active player, apply XOR toggle logic
 * Time: O(n) | Space: O(1)
 */
class Solution {
    public int scoreDifference(int[] nums) {
        int firstPlayerScore = 0;
        int secondPlayerScore = 0;
        boolean isFirstPlayerActive = true;

        for (int i = 0; i < nums.length; i++) {
            if (shouldToggle(nums[i], i)) {
                isFirstPlayerActive = !isFirstPlayerActive;
            }
            if (isFirstPlayerActive) {
                firstPlayerScore += nums[i];
            } else {
                secondPlayerScore += nums[i];
            }
        }
        return firstPlayerScore - secondPlayerScore;
    }

    private boolean shouldToggle(int value, int i) {
        boolean isSixth = (i + 1) % 6 == 0;
        boolean isOdd = value % 2 != 0;
        // Both true → cancel out (no toggle). XOR: toggle only if exactly one is true.
        if (isSixth && isOdd) return false;
        if (isSixth || isOdd) return true;
        return false;
    }
}
