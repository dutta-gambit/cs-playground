/**
 * LeetCode 145 - Binary Tree Postorder Traversal
 * https://leetcode.com/problems/binary-tree-postorder-traversal/
 *
 * Order: Left → Right → Root
 *
 * Approach: Recursive DFS with a helper that carries the result list.
 * - Recurse left, recurse right, THEN append the current value.
 *
 * Time:  O(n) — each node is visited exactly once.
 * Space: O(h) — recursion stack; h = tree height.
 *
 * Key learnings:
 * - Postorder is the natural order for "delete / free a tree" or any
 *   bottom-up aggregation (subtree sums, heights, diameter) — children's
 *   results are ready before the parent processes them.
 * - Iterative trick: do a modified preorder (root, right, left) on a stack,
 *   then reverse the result — that gives left, right, root.
 */
class PostorderTraversal {

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        impl(root, result);
        return result;
    }

    private void impl(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        impl(root.left, result);
        impl(root.right, result);
        result.add(root.val);
    }
}
