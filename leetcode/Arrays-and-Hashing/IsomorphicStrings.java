import java.util.*;

/**
 * LeetCode 205 - Isomorphic Strings
 * https://leetcode.com/problems/isomorphic-strings/
 *
 * Two strings are isomorphic if characters can be mapped 1-to-1
 * (same position, consistent mapping both directions).
 *
 * Wrong approach tried first: Frequency counting
 * - "abab" and "cddc" have same frequencies but are NOT isomorphic
 * - Frequency ignores positional mapping — isomorphic is about ORDER, not
 * counts
 *
 * Correct approach: Two mapping arrays (s→t and t→s)
 * - int[256] for ASCII, initialized to -1
 * - Walk position by position:
 * - If neither char is mapped → create both mappings
 * - If existing mappings don't match in BOTH directions → return false
 * - Two maps needed because mapping must be bijective (1-to-1 AND onto)
 * e.g., "ab" → "aa" fails: a→a, b→a (two chars map to same target)
 *
 * Time: O(n) | Space: O(1) — fixed 256-size arrays
 */
class Solution {
    public boolean isIsomorphic(String s, String t) {
        int[] mappingDictStoT = new int[256];
        Arrays.fill(mappingDictStoT, -1);

        int[] mappingDictTtoS = new int[256];
        Arrays.fill(mappingDictTtoS, -1);

        for (int i = 0; i < s.length(); ++i) {
            char c1 = s.charAt(i);
            char c2 = t.charAt(i);

            if (mappingDictStoT[c1] == -1 && mappingDictTtoS[c2] == -1) {
                mappingDictStoT[c1] = c2;
                mappingDictTtoS[c2] = c1;
            } else if (!(mappingDictStoT[c1] == c2 && mappingDictTtoS[c2] == c1)) {
                return false;
            }
        }

        return true;
    }
}
