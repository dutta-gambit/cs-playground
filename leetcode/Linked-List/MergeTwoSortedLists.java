/**
 * LeetCode 21 - Merge Two Sorted Lists
 * https://leetcode.com/problems/merge-two-sorted-lists/
 *
 * Problem:
 * You are given the heads of two sorted linked lists list1 and list2.
 * Merge the two lists into one sorted list by splicing together the nodes
 * of the first two lists, and return the head of the merged list.
 *
 * Example:
 *   Input:  list1 = [1,2,4], list2 = [1,3,4]
 *   Output: [1,1,2,3,4,4]
 *
 * Approach: Dummy node + walker, splice nodes in-place
 * - dummy is the anchor (returned as dummy.next); curr is the walker that moves
 * - Each iteration: pick the smaller head, attach it via curr.next, advance the
 *   chosen input pointer, then advance curr so the next attach doesn't clobber
 * - When one list is exhausted, splice the leftover tail in one assignment
 *   (the remainder is already a fully linked sorted chain — no node-by-node copy)
 *
 * Time:  O(n + m) — each node visited once
 * Space: O(1) — no new nodes allocated; we re-link existing ones
 *
 * Key learnings:
 * - The two pointer moves per iteration are DIFFERENT operations:
 *     curr.next = chosen  → mutates curr's `next` field (writes a slot;
 *                           curr itself does NOT move)
 *     curr = curr.next    → reassigns the curr variable (the walker
 *                           steps forward)
 *   Forgetting `curr = curr.next` makes every iteration overwrite the
 *   same slot, collapsing the output to just the last-written node.
 * - No special `==` branch needed. Use `<=` and equal values fall into
 *   the list1 bucket naturally; the next iteration picks up list2's
 *   equal node. The branch in this solution works but is redundant.
 * - The leftover tail does NOT need a while-loop. `curr.next = list1`
 *   (or list2) attaches the whole remaining sorted chain at once,
 *   because both inputs were sorted to begin with.
 * - Same anchor/walker rule as the dummy-node pattern: NEVER do
 *   `dummy = ...`. Only mutate `dummy.next`. Return `dummy.next` at end.
 */
class MergeTwoSortedLists {
    // ListNode is provided by LeetCode: { int val; ListNode next; }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(-101);
        ListNode curr = dummy;

        while (list1 != null && list2 != null) {

            if (list1.val < list2.val) {
                curr.next = list1;
                list1 = list1.next;
            } else if (list1.val == list2.val) {
                curr.next = list1;
                list1 = list1.next;
                curr = curr.next;
                curr.next = list2;
                list2 = list2.next;
            } else {
                curr.next = list2;
                list2 = list2.next;
            }
            curr = curr.next;
        }

        if (list1 != null) {
            curr.next = list1;
            curr = curr.next;
        } else {
            curr.next = list2;
            curr = curr.next;
        }

        return dummy.next;
    }
}
