import java.util.*;

/**
 * LeetCode - Merge Adjacent Equal Elements (Medium)
 *
 * Repeatedly merge the leftmost adjacent equal pair (sum them) until
 * no adjacent equal pairs remain.
 *
 * Approach 1 (Naive): Simulation — scan, merge leftmost pair, rebuild list, repeat
 * - Time: O(n²) — n passes, each O(n)
 * - TLE on chain reaction inputs like [1,1,2,4,8,...,65536]
 *
 * Approach 2 (Optimal): Stack — build result left-to-right, chain-react on push
 * - Time: O(n) — each element pushed/popped at most once
 * - Space: O(n)
 *
 * Key learnings:
 * - "Adjacent collapse" problems = Stack pattern
 * - Chain reactions handled by while loop checking top of stack after each merge
 * - ArrayList as stack: get(size-1), remove(size-1), add()
 * - == is safe when one side is primitive (auto-unboxing), dangerous when both are objects
 */

// --- Approach 1: Simulation (O(n²) — TLE) ---
// class Solution {
//     public List<Long> mergeAdjacent(int[] nums) {
//         List<Long> intermediate = new ArrayList<>();
//         for (int i : nums) intermediate.add(Long.valueOf(i));
//
//         while (isAdjacentPairPresent(intermediate)) {
//             intermediate = mergeClosest(intermediate);
//         }
//         return intermediate;
//     }
//
//     private List<Long> mergeClosest(List<Long> list) {
//         List<Long> result = new ArrayList<>();
//         boolean merged = false;
//         int i = 0;
//         while (i < list.size()) {
//             if (!merged && i + 1 < list.size() && list.get(i).equals(list.get(i + 1))) {
//                 result.add(list.get(i) + list.get(i + 1));
//                 i += 2;
//                 merged = true;
//             } else {
//                 result.add(list.get(i));
//                 i++;
//             }
//         }
//         return result;
//     }
//
//     private boolean isAdjacentPairPresent(List<Long> list) {
//         for (int i = 0; i < list.size() - 1; i++) {
//             if (list.get(i).equals(list.get(i + 1))) return true;  // .equals() not ==
//         }
//         return false;
//     }
// }

// --- Approach 2: Stack (Optimal O(n)) ---
class Solution {
    public List<Long> mergeAdjacent(int[] nums) {
        List<Long> result = new ArrayList<>();  // ArrayList as stack

        for (int num : nums) {
            long current = num;
            // Chain reaction: keep merging with top if equal
            while (!result.isEmpty() && result.get(result.size() - 1) == current) {
                current += result.remove(result.size() - 1);  // pop + merge
            }
            result.add(current);  // push
        }

        return result;
    }
}
