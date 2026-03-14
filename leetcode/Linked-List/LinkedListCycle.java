/**
 * LeetCode 141 - Linked List Cycle
 * https://leetcode.com/problems/linked-list-cycle/
 *
 * Approach: Floyd's Cycle Detection (slow/fast pointers)
 * - slow moves 1 step, fast moves 2 steps
 * - If cycle exists, fast never hits null — they must meet inside the cycle
 * - If no cycle, fast reaches null
 *
 * Time:  O(n) — fast enters cycle and meets slow within one full cycle loop
 * Space: O(1) — only two pointers
 *
 * Key learnings:
 * - If there's a cycle, there is NO null — fast loops forever until they meet
 * - Fast closes gap by 1 each step → guaranteed to meet (not skip past)
 * - Check fast != null && fast.next != null (guard the two dereferences fast needs)
 * - Check slow == fast AFTER moving, not before (both start at head → false positive)
 * - Only need to guard fast, not slow — fast is always ahead
 * - 2x speed specifically works because gap shrinks by exactly 1 (3x could skip)
 */
class Solution {
    // ListNode is provided by LeetCode: { int val; ListNode next; }
    public boolean hasCycle(ListNode head) {

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }

        return false;
    }
}
