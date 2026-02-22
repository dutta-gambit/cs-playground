import java.util.*;

/**
 * LeetCode 1282 - Group the People Given the Group Size They Belong To
 * https://leetcode.com/problems/group-the-people-given-the-group-size-they-belong-to/
 *
 * Approach: HashMap grouping by group size, then chunk each list into sublists of that size
 * - Map: groupSize → list of person indices
 * - For each group size, split the list into chunks of that size
 *
 * Time: O(n) | Space: O(n)
 */
class Solution {
    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        List<List<Integer>> result = new ArrayList<>();
        Map<Integer, List<Integer>> groupMap = new HashMap<>();

        for (int i = 0; i < groupSizes.length; i++) {
            groupMap.computeIfAbsent(groupSizes[i], k -> new ArrayList<>()).add(i);
        }

        for (Map.Entry<Integer, List<Integer>> entry : groupMap.entrySet()) {
            List<Integer> people = entry.getValue();
            int size = entry.getKey();
            int j = 0;
            while (j < people.size()) {
                result.add(new ArrayList<>(people.subList(j, j + size)));
                j += size;
            }
        }
        return result;
    }
}
