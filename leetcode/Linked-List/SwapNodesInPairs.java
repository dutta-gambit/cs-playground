/**
 * LeetCode 24 - Swap Nodes in Pairs
 * https://leetcode.com/problems/swap-nodes-in-pairs/
 *
 * Problem:
 * Given a linked list, swap every two adjacent nodes and return its head.
 * You must solve the problem without modifying the values in the list's nodes
 * (i.e., only nodes themselves may be changed).
 *
 * Example:
 *   Input:  head = [1,2,3,4]
 *   Output: [2,1,4,3]
 *
 *   Input:  head = [1,2,3,4,5]
 *   Output: [2,1,4,3,5]   (trailing single node stays put)
 *
 * Approach: Dummy node + walker that sits BEFORE each pair
 * - curr starts at dummy (the "before" of the very first pair)
 * - Each iteration swaps the pair (first, second) = (curr.next, curr.next.next)
 *   with THREE rewires:
 *     (1) first.next  = second.next   — first now points past the pair
 *     (2) second.next = first         — second now leads the swapped pair
 *     (3) curr.next   = second        — boundary: predecessor points at new front
 * - Then walker advances: curr = first  (first is now the trailing node of the
 *   swapped pair, which is by definition the "before" of the NEXT pair)
 * - Loop condition `curr.next != null && curr.next.next != null` naturally
 *   handles odd-length lists: the lone trailing node is left in place
 *
 * Time:  O(n) — each node visited once
 * Space: O(1) — only pointer locals
 *
 * Key learnings:
 * - Every "rearrange a chunk" operation has the SAME three-part shape:
 *     (a) internal rewires — the pointers inside the chunk
 *     (b) BOUNDARY rewire — the predecessor's .next retargets to the new
 *         front of the rearranged chunk  ← most-forgotten step
 *     (c) walker advance — move "before" pointer to the new boundary so
 *         the next iteration is set up correctly
 *   Skip (b) and the chunk reorganizes in memory but stays disconnected
 *   from the list — visible only through dangling local variables.
 * - Order matters: (b) must happen BEFORE the walker advance. Once you've
 *   reassigned curr, `curr.next = second` would mutate the wrong node's
 *   .next field (the demoted "first" instead of the actual predecessor).
 * - `curr.next = second` is a GENERIC instruction — it mutates whoever
 *   curr currently points at. On iter 1, curr == dummy, so dummy.next
 *   gets updated (which is exactly when the head of the list changes).
 *   On iter 2+, curr is an interior node, so an interior .next gets
 *   updated. Same line, different target, because curr moves.
 * - dummy.next gets written exactly ONCE — the moment the head changes
 *   (when pair 1 is swapped). After that, curr has walked into the
 *   interior and dummy is left alone until the final return.
 * - The early-return guards for null head and single-node list are
 *   redundant once the loop condition checks both `curr.next != null`
 *   AND `curr.next.next != null`. Both cases naturally produce a no-op
 *   loop, and `return dummy.next` yields the right answer.
 * - Same anchor/walker rule as every other dummy-node problem:
 *   `dummy` is the anchor (never reassigned); `curr` is the walker
 *   (moves every iteration). Don't conflate the two.
 */
class SwapNodesInPairs {
    // ListNode is provided by LeetCode: { int val; ListNode next; }

    public ListNode swapPairs(ListNode head) {

        ListNode dummy = new ListNode(-101, head);
        ListNode curr = dummy;

        while (curr.next != null && curr.next.next != null) {
            ListNode first  = curr.next;
            ListNode second = curr.next.next;

            first.next  = second.next;   // (1) internal
            second.next = first;         // (2) internal
            curr.next   = second;        // (3) boundary — must precede walker advance
            curr = first;                // walker advance: first is now "before next pair"
        }

        return dummy.next;
    }
}
