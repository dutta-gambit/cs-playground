package com.karat.ancestry;

/**
 * Single (parent, child) edge in the family-tree graph.
 *
 * Example raw line:
 *   1 2     // 1 is the parent of 2
 */
public class ParentChildPair {

    private final int parent;
    private final int child;

    public ParentChildPair(int parent, int child) {
        this.parent = parent;
        this.child = child;
    }

    public int getParent() {
        return parent;
    }

    public int getChild() {
        return child;
    }
}
