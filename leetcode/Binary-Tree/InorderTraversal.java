/**
 * LeetCode 94 - Binary Tree Inorder Traversal
 * https://leetcode.com/problems/binary-tree-inorder-traversal/
 *
 * Order: Left → Root → Right
 *
 * Approach: Recursive DFS with a helper that carries the result list.
 * - Public method seeds the result list and delegates to the helper.
 * - Helper recurses left, appends the current value, recurses right.
 *
 * Time:  O(n) — each node is visited exactly once.
 * Space: O(h) — recursion stack; h = tree height. O(n) worst case (skewed),
 *        O(log n) for a balanced tree.
 *
 * Key learnings:
 * - The helper MUST recurse into itself (`impl(...)`) and pass `result` along.
 *   Recursing into the public method instead creates a fresh list every call
 *   and discards everything below the root — a very common bug.
 * - For inorder on a BST, the output is sorted ascending.
 */
class InorderTraversal {

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        impl(root, result);
        return result;
    }

    private void impl(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        impl(root.left, result);
        result.add(root.val);
        impl(root.right, result);
    }
}
