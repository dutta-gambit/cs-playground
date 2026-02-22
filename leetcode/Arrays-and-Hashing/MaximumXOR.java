/**
 * LeetCode 3849 - Maximum Bitwise XOR After Rearrangement
 *
 * Given binary strings s and t, rearrange t to maximize s XOR t.
 *
 * Approach: Greedy — maximize 1s in XOR from left to right (MSB first)
 * - For each bit in s, place the opposite bit from t (if available)
 * - s[i]='1' → place '0' from t, s[i]='0' → place '1' from t
 * - Count available 0s and 1s in t, greedily assign from left
 *
 * Key: XOR gives 1 when bits differ. Leftmost bits have highest value (2^n),
 * so greedily making them different is always optimal.
 *
 * Time: O(n) | Space: O(n)
 */
class Solution {
    public String maximumXor(String s, String t) {
        char[] arrS = s.toCharArray();
        char[] arrT = t.toCharArray();

        int countOneInT = 0, countZeroInT = 0;
        for (char c : arrT) {
            if (c == '0') countZeroInT++;
            else countOneInT++;
        }

        int j = 0;
        for (int i = 0; i < arrS.length; i++) {
            if (arrS[i] == '1') {
                // Want '0' to make XOR = 1
                if (countZeroInT > 0) { arrT[j] = '0'; countZeroInT--; }
                else { arrT[j] = '1'; countOneInT--; }
            } else {
                // Want '1' to make XOR = 1
                if (countOneInT > 0) { arrT[j] = '1'; countOneInT--; }
                else { arrT[j] = '0'; countZeroInT--; }
            }
            j++;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrS.length; i++) {
            sb.append(arrT[i] ^ arrS[i]);
        }
        return sb.toString();
    }
}
