import java.util.*;

/**
 * LeetCode 49 - Group Anagrams
 * https://leetcode.com/problems/group-anagrams/
 *
 * Approach 1: Sort characters as key → O(n · k log k) time
 * Approach 2: Count array as key → O(n · k) time (optimal)
 *
 * Space: O(n · k) for both approaches
 *
 * Key learnings:
 * - computeIfAbsent() → "get or create" pattern for grouping
 * - Map stores references → mutating a list inside map doesn't need re-put
 * - List.add() returns boolean, not the List itself
 * - '#' separator prevents count collision (e.g., [1,12] vs [11,2])
 * - map.values() gives all grouped lists directly
 */

// --- Approach 1: Sort as key ---
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> mapOfAnagrams = new HashMap<>();

        for (String str : strs) {
            char[] ch = str.toCharArray();
            Arrays.sort(ch);
            String sortedStr = new String(ch);
            mapOfAnagrams.computeIfAbsent(sortedStr, k -> new ArrayList<>()).add(str);
        }

        return new ArrayList<>(mapOfAnagrams.values());
    }
}

// --- Approach 2: Count array as key (optimal) ---
// class Solution {
//     public List<List<String>> groupAnagrams(String[] strs) {
//         Map<String, List<String>> map = new HashMap<>();
//
//         for (String str : strs) {
//             int[] count = new int[26];
//             for (char c : str.toCharArray()) {
//                 count[c - 'a']++;
//             }
//             StringBuilder sb = new StringBuilder();
//             for (int i = 0; i < 26; i++) {
//                 sb.append(count[i]).append('#');
//             }
//             map.computeIfAbsent(sb.toString(), k -> new ArrayList<>()).add(str);
//         }
//
//         return new ArrayList<>(map.values());
//     }
// }
