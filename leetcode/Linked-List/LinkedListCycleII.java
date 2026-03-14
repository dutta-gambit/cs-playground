/**
 * LeetCode 142 - Linked List Cycle II
 * https://leetcode.com/problems/linked-list-cycle-ii/
 *
 * Approach 1 (implemented): HashMap — store visited nodes, first repeat is cycle start
 * - Walk the list, store each node in a map with its index
 * - First node already in the map = cycle start
 * - If we reach null, no cycle
 *
 * Time:  O(n) — single pass
 * Space: O(n) — HashMap stores all visited nodes
 *
 * Approach 2 (optimal): Floyd's Algorithm Phase 2
 * - Phase 1: slow/fast find meeting point inside cycle
 * - Phase 2: one pointer at head, one at meeting point, both move 1 step → meet at cycle start
 * - Math: distance from head to cycle start == distance from meeting point to cycle start
 * - Time: O(n), Space: O(1)
 *
 * Key learnings:
 * - Once map.containsKey(prev) is true, prev itself IS the cycle start — can return directly
 * - HashMap approach is simpler to reason about, Floyd's Phase 2 is the O(1) space follow-up
 * - Google/Meta will ask the O(1) space version as a follow-up
 */
import java.util.Map;
import java.util.HashMap;

class Solution {
    // ListNode is provided by LeetCode: { int val; ListNode next; }
    public ListNode detectCycle(ListNode head) {

        ListNode prev = head;
        Map<ListNode, Integer> map = new HashMap<>();
        int count = 0;
        int index = -1;

        while (prev != null) {
            if (map.containsKey(prev)) {
                index = map.get(prev);
                break;
            }
            map.put(prev, count);
            count = count + 1;
            prev = prev.next;
        }

        if (index == -1) {
            return null;
        }

        ListNode newNode = head;

        for (int i = 0; i < index; i++) {
            newNode = newNode.next;
        }

        return newNode;
    }
}
