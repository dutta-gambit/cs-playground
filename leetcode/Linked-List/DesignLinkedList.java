/**
 * LeetCode 707 - Design Linked List
 * https://leetcode.com/problems/design-linked-list/
 *
 * Approach: Singly Linked List with dummy/sentinel node + size tracking
 * - Dummy node sits at position -1, real data starts at head.next
 * - head NEVER changes — always points to the dummy
 * - All index-based ops: walk `index` steps from dummy → land on node BEFORE target
 * - Then operate on prev.next (insert/delete/read)
 *
 * Time:  O(n) for get, addAtIndex, addAtTail, deleteAtIndex | O(1) for addAtHead
 * Space: O(1) per operation (no extra data structures)
 *
 * Key learnings:
 * - Dummy node eliminates ALL head special cases (empty list, insert at 0, delete at 0)
 * - The pattern: start at dummy, walk index steps, operate on prev.next
 * - addAtHead and addAtTail are just special cases of addAtIndex(0, val) and addAtIndex(size, val)
 * - Always update size on add/delete — boundary checks depend on it
 * - Direct field access (node.val, node.next) is standard for internal DSA — no getters needed
 * - With dummy: index 0 from dummy = dummy itself, so prev.next = first real node. No if-checks.
 */
class MyLinkedList {

    ListNode head;
    int size;

    public class ListNode {

        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    public MyLinkedList() {
        this.head = new ListNode(0); // dummy node — always exists
        this.size = 0;
    }

    public int get(int index) {

        if (index < 0 || index >= size) {
            return -1;
        }

        ListNode curr = head.next; // start at first REAL node, not dummy
        int count = 0;

        while (count != index && curr != null) {
            curr = curr.next;
            count++;
        }
        return curr.val;
    }

    public void addAtHead(int val) {
        addAtIndex(0, val);
    }

    public void addAtTail(int val) {
        addAtIndex(size, val);
    }

    public void addAtIndex(int index, int val) {

        if (index < 0 || index > size) {
            return;
        }

        ListNode prev = head; // start at dummy
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }

        ListNode newNode = new ListNode(val);
        newNode.next = prev.next;
        prev.next = newNode;
        size++;
    }

    public void deleteAtIndex(int index) {

        if (index < 0 || index >= size) {
            return;
        }

        ListNode prev = head; // start at dummy
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }

        prev.next = prev.next.next;
        size--;
    }
}
