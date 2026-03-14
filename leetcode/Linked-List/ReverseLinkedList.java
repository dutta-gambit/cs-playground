/**
 * LeetCode 206 - Reverse Linked List
 * https://leetcode.com/problems/reverse-linked-list/
 *
 * Approach: Iterative three-pointer march (prev, curr, next)
 * - Walk through the list, reversing each node's next pointer
 * - Save curr.next before breaking the link
 * - After loop, prev is the new head (curr is null)
 *
 * Time:  O(n) — single pass
 * Space: O(1) — only three pointers
 *
 * Key learnings:
 * - The prev/curr/next pattern is foundational — reused in many linked list problems
 * - Always save curr.next BEFORE modifying curr.next (otherwise you lose the chain)
 * - prev starts at null — this naturally makes the old head point to null (new tail)
 * - Return prev, not curr (curr is null when loop ends)
 */
class ReverseLinkedList {

    public ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }
}
