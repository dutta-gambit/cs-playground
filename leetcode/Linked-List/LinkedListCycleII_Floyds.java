/**
 * LeetCode 142 - Linked List Cycle II (Floyd's Algorithm — O(1) space)
 * https://leetcode.com/problems/linked-list-cycle-ii/
 *
 * Approach: Floyd's Cycle Detection — Phase 1 + Phase 2
 * - Phase 1: slow (1 step) and fast (2 steps) find meeting point inside cycle
 * - Phase 2: one pointer at head, one at meeting point, both move 1 step → meet at cycle start
 *
 * Why Phase 2 works:
 * - F = distance from head to cycle start
 * - When they meet: 2(F+a) = F+a+nC → F = nC-a = (C-a) + (n-1)C
 * - C-a = distance from meeting point to cycle start (going forward)
 * - So walking F steps from meeting point = walking F steps from head = both arrive at cycle start
 * - TODO: revisit Floyd's Phase 2 proof until fully confident
 *
 * Time:  O(n) — Phase 1: at most F + C steps, Phase 2: at most F steps
 * Space: O(1) — only pointers, no extra data structures
 *
 * Key learnings:
 * - Meeting point from Phase 1 is just where slow sits when slow == fast
 * - Phase 2 is the elegant part: reuse the meeting point without any storage
 * - The HashMap approach (LinkedListCycleII.java) is O(n) space — this is the upgrade
 * - Once inside a cycle, nothing can escape — every node's next points to another cycle node
 * - Gap between slow and fast is always 0 to C-1 (positions are modular)
 */
class Solution {
    // ListNode is provided by LeetCode: { int val; ListNode next; }
    public ListNode detectCycle(ListNode head) {

        ListNode slow = head;
        ListNode fast = head;
        ListNode meetingPoint = null;
        Boolean isCyclic = false;

        while (fast != null && fast.next != null) {

            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                meetingPoint = slow;
                isCyclic = true;
                break;
            }
        }

        if (!isCyclic) {
            return null;
        }

        ListNode currHead = head;

        while (currHead != meetingPoint) {
            currHead = currHead.next;
            meetingPoint = meetingPoint.next;
        }

        return currHead;
    }
}
