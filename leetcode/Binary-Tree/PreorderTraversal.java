/**
 * LeetCode 144 - Binary Tree Preorder Traversal
 * https://leetcode.com/problems/binary-tree-preorder-traversal/
 *
 * Order: Root → Left → Right
 *
 * Approach: Recursive DFS with a helper that carries the result list.
 * - Append the current value FIRST, then recurse left, then recurse right.
 *
 * Time:  O(n) — each node is visited exactly once.
 * Space: O(h) — recursion stack; h = tree height.
 *
 * Key learnings:
 * - Preorder is the natural order for "serialize / clone a tree" — you
 *   always have a parent reference before its children.
 * - Iterative version uses an explicit stack: push root, then loop
 *   { pop, visit, push right, push left } so left is processed first.
 */
class PreorderTraversal {

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        impl(root, result);
        return result;
    }

    private void impl(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        result.add(root.val);
        impl(root.left, result);
        impl(root.right, result);
    }
}
