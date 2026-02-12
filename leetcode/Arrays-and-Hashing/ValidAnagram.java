import java.util.Map;
import java.util.HashMap;

/**
 * LeetCode 242 - Valid Anagram
 * https://leetcode.com/problems/valid-anagram/
 *
 * Approach 1: HashMap frequency count
 * - Count chars in s (increment), iterate t (decrement), check map empty
 * Time:  O(n)
 * Space: O(n) — but at most 26 entries for lowercase English
 *
 * Approach 2 (Optimal): int[26] array — O(1) space, no autoboxing
 * - count[ch - 'a']++ for s, count[ch - 'a']-- for t, check all zeros
 *
 * Key learnings:
 * - Map (not Set) because we need frequency, not just existence
 * - charAt(i) - 'a' → numeric promotion: char widens to int (unicode math)
 * - Bounded character set (26 letters) → use int[] instead of HashMap
 * - getOrDefault() avoids null checks on Map
 */

// --- Approach 1: HashMap ---
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        Map<Character, Integer> charCount = new HashMap<>();
        char[] a = s.toCharArray();
        char[] b = t.toCharArray();

        for (int i = 0; i < a.length; i++) {
            charCount.put(a[i], charCount.getOrDefault(a[i], 0) + 1);
        }
        for (int i = 0; i < b.length; i++) {
            if (!charCount.containsKey(b[i])) {
                return false;
            }
            charCount.put(b[i], charCount.get(b[i]) - 1);
            if (charCount.get(b[i]) == 0) {
                charCount.remove(b[i]);
            }
        }
        return true; // map guaranteed empty if same length + all matched
    }
}

// --- Approach 2: int[26] array (Optimal) ---
// class Solution {
//     public boolean isAnagram(String s, String t) {
//         if (s.length() != t.length()) return false;
//
//         int[] count = new int[26];
//         for (int i = 0; i < s.length(); i++) {
//             count[s.charAt(i) - 'a']++;
//             count[t.charAt(i) - 'a']--;
//         }
//         for (int c : count) {
//             if (c != 0) return false;
//         }
//         return true;
//     }
// }
