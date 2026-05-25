/**
 * LeetCode 61 - Rotate List
 * https://leetcode.com/problems/rotate-list/
 *
 * Problem:
 * Given the head of a linked list, rotate the list to the right by k places.
 *
 * Example:
 *   Input:  head = [1,2,3,4,5], k = 2
 *   Output: [4,5,1,2,3]
 *
 * Approach: Length + modulo + single cut/splice (O(n), one pass after length)
 * - Walk once to find length L (start curr at dummy.next → 1-to-1 with real nodes)
 * - Reduce k: k %= L  (rotating by L is a no-op, so anything ≥ L is wasted work)
 * - If k == 0 after reduction, the list is already in final position → return head
 * - The new head will be the node at position (L - k) from the front (1-indexed)
 * - Walk a finder pointer to the node BEFORE the new head: start at dummy, take
 *   (L - k) steps. Starting at dummy gives the "one free hop" that lands us at
 *   the predecessor instead of the target itself
 * - Three rewires complete the rotation:
 *     1. save newHead = predecessor.next
 *     2. predecessor.next = null  (cut)
 *     3. walk to the tail of newHead's chain, then tail.next = head (splice)
 *
 * Time:  O(n) — one pass for length, one pass to predecessor, one pass to tail
 * Space: O(1) — only pointer locals
 *
 * Key learnings:
 * - O(n*k) (rotate-by-1 k times) TLEs on LeetCode because k can be up to 2×10⁹.
 *   k %= length collapses any huge k into the range [0, length-1]. This is not
 *   "an optimization" — it's required for correctness within the constraints.
 * - Three edge cases that crash naive code:
 *     a. head == null            → guard with length == 0 check (or up-front return)
 *     b. single-node list        → length == 1 makes k %= 1 == 0 → early return
 *     c. k a multiple of length  → k becomes 0 → return head (no rotation needed)
 *   Without the `if (k == 0) return head;` guard, the second-piece splice walks
 *   on a null pointer and NPEs.
 * - Walking principle: "K steps from dummy" lands at the Kth real node
 *   (1-indexed); "K steps from head" lands at the (K+1)th. Start at dummy when
 *   you need the PREDECESSOR for surgery (cutting, splicing), start at head
 *   when you're processing every node (counting, printing).
 * - Tail-finding loop must guard with `while (tail.next != null)`, not
 *   `while (tail != null)`. The former stops AT the last node; the latter walks
 *   past it and leaves tail == null, NPEing the next dereference.
 * - dummy as anchor, never as walker: don't reassign the variable `dummy`
 *   itself — only mutate dummy.next. (This solution stores the new head in a
 *   separately-named variable to keep dummy's role unambiguous.)
 */
class RotateList {
    // ListNode is provided by LeetCode: { int val; ListNode next; }

    public ListNode rotateRight(ListNode head, int k) {

        ListNode dummy = new ListNode(-101, head);

        int length = 0;
        ListNode curr = dummy.next;
        while (curr != null) {
            length++;
            curr = curr.next;
        }

        if (length == 0) return null;

        k = k % length;
        if (k == 0) return head;

        int stepsToPredecessor = length - k;
        curr = dummy;
        while (stepsToPredecessor != 0) {
            curr = curr.next;
            stepsToPredecessor--;
        }

        ListNode newHead = curr.next;
        curr.next = null;

        ListNode tail = newHead;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = head;

        return newHead;
    }
}
