/**
 * LeetCode 3217 - Delete Nodes From Linked List Present in Array
 * https://leetcode.com/problems/delete-nodes-from-linked-list-present-in-array/
 *
 * Problem:
 * Given an array of integers nums and the head of a linked list, return the
 * head of the modified linked list after removing all nodes whose value
 * exists in nums.
 *
 * Example:
 *   Input:  nums = [1,2,3], head = [1,2,3,4,5]
 *   Output: [4,5]
 *
 * Approach: HashSet lookup + dummy node + curr/prev walk
 * - Dump nums into a HashSet → O(1) membership checks while walking the list
 * - Use a dummy sentinel before head so the real head is "just another node"
 * - Walk curr through the list; prev tracks the last kept node
 * - On match (curr.val in set): unlink curr by setting prev.next = curr.next
 *   (prev stays put, so consecutive deletions still chain correctly)
 * - On no match: advance prev to curr, then move curr forward
 * - Return dummy.next (head may itself have been removed)
 *
 * Time:  O(n + m) — n = nums length (build set), m = list length (one pass)
 * Space: O(n) — the HashSet
 *
 * Key learnings:
 * - This is "Remove Linked List Elements" (LC 203) generalised from one
 *   target value to a SET of target values — same skeleton, just swap
 *   `curr.val != val` for `!numSet.contains(curr.val)`
 * - The `if (!set.contains(i)) set.add(i)` check is redundant — HashSet.add
 *   already no-ops on duplicates. `for (int i : nums) numSet.add(i);` is
 *   equivalent and cleaner.
 * - HashSet trades O(n) space for O(1) lookup; without it, each list node
 *   would do an O(n) scan of nums → O(n*m) overall. Worth it for any
 *   non-trivial nums size.
 * - Same dummy-node anchor rule applies: NEVER reassign `dummy = ...`,
 *   only mutate `dummy.next`. Return `dummy.next` at the end.
 * - prev does NOT advance on deletion — critical when several consecutive
 *   nodes all match (e.g. list [1,2,3] with nums = [1,2,3])
 */
class DeleteNodesFromLinkedListPresentInArray {
    // ListNode is provided by LeetCode: { int val; ListNode next; }

    public ListNode modifiedList(int[] nums, ListNode head) {

        Set<Integer> numSet = new HashSet<>();

        for (int i : nums) {
            if (!numSet.contains(i)) {
                numSet.add(i);
            }
        }

        ListNode dummy = new ListNode(-1, head);
        ListNode curr = dummy;
        ListNode prev = null;

        while (curr != null) {
            if (!numSet.contains(curr.val)) {
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
