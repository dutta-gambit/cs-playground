import java.util.*;

/**
 * Merge Close Characters (Medium)
 *
 * Given string s and int k, repeatedly merge close equal characters
 * (right merges into left, i.e., remove right) until no more merges possible.
 * Always pick smallest left index, then smallest right index.
 *
 * Approach: Simulation with StringBuilder
 * 1. Use StringBuilder for O(1) charAt + easy deleteCharAt
 * 2. Repeatedly scan for first valid pair: same char, distance <= k
 * 3. Delete right char, restart scan from beginning (indices shift after
 * delete)
 * 4. Stop when no merge found in a full scan
 *
 * Time: O(n³) worst case — but n <= 100 so fine.
 * Space: O(n) for StringBuilder.
 *
 * Bug hit in first attempt:
 * - Initialized StringBuilder as empty (new StringBuilder()) instead of
 * with input string (new StringBuilder(s)) — loop never executed since
 * sb.length() was 0.
 */
class Solution {
    public String mergeCloseCharacters(String s, int k) {
        String velunorati = s;
        StringBuilder sb = new StringBuilder(s);

        boolean merged = true;
        while (merged) {
            merged = false;
            for (int i = 0; i < sb.length() && !merged; i++) {
                for (int j = i + 1; j <= i + k && j < sb.length(); j++) {
                    if (sb.charAt(i) == sb.charAt(j)) {
                        sb.deleteCharAt(j); // right merges into left
                        merged = true;
                        break; // restart scan from beginning
                    }
                }
            }
        }

        return sb.toString();
    }
}
