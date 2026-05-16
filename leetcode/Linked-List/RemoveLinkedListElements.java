/**
 * LeetCode 203 - Remove Linked List Elements
 * https://leetcode.com/problems/remove-linked-list-elements/
 *
 * Problem:
 * Given the head of a linked list and an integer val, remove all the nodes
 * of the linked list that have Node.val == val, and return the new head.
 *
 * Example:
 *   Input:  head = [1,2,6,3,4,5,6], val = 6
 *   Output: [1,2,3,4,5]
 *
 * Approach: Dummy node + curr/prev walk
 * - Use a dummy sentinel before head so the real head is "just another node"
 * - Walk curr through the list; prev tracks the last kept node
 * - On match: unlink curr by setting prev.next = curr.next (prev stays put)
 * - On no match: advance prev to curr, then move curr forward
 * - Return dummy.next (the head may itself have been removed)
 *
 * Time:  O(n) — single pass
 * Space: O(1) — only dummy/curr/prev pointers
 *
 * Key learnings:
 * - The dummy node pattern shines when the HEAD itself might be deleted —
 *   without it, head removal becomes a special case littered with if-checks
 * - Anchor (dummy) vs walker (curr): dummy never moves, curr does.
 *   NEVER do `dummy = head` — that reassigns the anchor away and you lose
 *   your return reference. Only mutate `dummy.next`.
 * - When deleting, DO NOT advance prev — prev must stay as the last kept
 *   node so the next deletion still unlinks correctly (e.g. consecutive
 *   target values like [6,6,6] would break otherwise)
 * - Return dummy.next, not head — head may have been one of the removed nodes
 */
class RemoveLinkedListElements {
    // ListNode is provided by LeetCode: { int val; ListNode next; }

    public ListNode removeElements(ListNode head, int val) {

        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode curr = dummy;
        ListNode prev = null;

        while (curr != null) {
            if (curr.val != val) {
                prev = curr;
                curr = curr.next;
            } else {
                curr = curr.next;
                prev.next = curr;
            }
        }

        return dummy.next;
    }
}
