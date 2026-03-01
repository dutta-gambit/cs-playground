import java.util.*;

/**
 * Trim Trailing Vowels (Easy) — Weekly Contest
 *
 * Remove all trailing vowels from a string.
 *
 * Approach 1 (Submitted): Reverse scan with boolean flag +
 * StringBuilder.reverse()
 * - Works but verbose: ArrayList for vowels, redundant if-blocks, reverse at
 * end
 *
 * Approach 2 (Optimal): Pointer from end — find last non-vowel, substring.
 * - No StringBuilder, no reverse. substring(0, 0) handles all-vowels edge case.
 *
 * Takeaway: "Remove trailing X" → pointer from the end, not "iterate and
 * rebuild."
 *
 * Time: O(n) | Space: O(1) (excluding output)
 */

// --- Approach 1: Submitted in contest ---
// class Solution {
// public String trimTrailingVowels(String s) {
// List<Character> vowels = new ArrayList<>(Arrays.asList('a','e','i','o','u'));
// StringBuilder sb = new StringBuilder();
// Boolean vowelFound = true;
// for (int i = s.length() - 1; i >= 0; i--) {
// if (vowels.contains(s.charAt(i)) && vowelFound) {
// vowelFound = true;
// }
// if (!vowels.contains(s.charAt(i))) {
// vowelFound = false;
// }
// if (!vowelFound) {
// sb.append(s.charAt(i));
// vowelFound = false;
// }
// }
// if (sb.isEmpty()) {
// return "";
// }
// return sb.reverse().toString();
// }
// }

// --- Approach 2: Optimal ---
class Solution {
    public String trimTrailingVowels(String s) {
        String vowels = "aeiou";
        int i = s.length() - 1;
        while (i >= 0 && vowels.indexOf(s.charAt(i)) != -1) {
            i--;
        }
        return s.substring(0, i + 1);
    }
}
