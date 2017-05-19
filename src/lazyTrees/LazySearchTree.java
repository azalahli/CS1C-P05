package lazyTrees;


import java.util.*;

/**
 * Implementation of a Lazy Binary search tree
 * File functions mostly copied from FHsearch_tree
 * @author Myron Pow 5/17/17
 */
public class LazySearchTree<E extends Comparable< ? super E > >
        implements Cloneable {
    protected int mSize;
    protected int mSizeHard;
    protected LazySTNode mRoot;

    /**
     * Creates empty LazyTree
     */
    public LazySearchTree() {
        clear();
    }

    /**
     * Checks to see if the tree is empty
     * @return boolean value 0 = has nodes 1 = empty
     */
    public boolean empty() {
        return (mSize == 0);
    }

    /**
     * Accessor for soft size
     * @return int of soft nodes
     */
    public int size() {
        return mSize;
    }

    /**
     *Clears tree to default values
     */
    public void clear() {
        mSize = 0;
        mSizeHard = 0;
        mRoot = null;
    }

    public int showHeight() {
        return findHeight(mRoot, -1);
    }

    /**
     * Accessor for LazyTree size, incl "deleted"
     * @return total number of nodes
     */
    public int sizeHard() {
        return mSizeHard;
    }

    public E findMin() {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMin(mRoot).data;
    }

    public E findMax() {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMax(mRoot).data;
    }

    public E find(E x) {
        LazySTNode resultNode;
        resultNode = find(mRoot, x);
        if (resultNode == null)
            throw new NoSuchElementException();
        return resultNode.data;
    }

    public boolean contains(E x) {
        return find(mRoot, x) != null;
    }

    public boolean insert(E x) {
        int oldSize = mSize;
        mRoot = insert(mRoot, x);
        return (mSize != oldSize);
    }

    public boolean remove(E x) {
        int oldSize = mSize;
        mRoot = remove(mRoot, x);
        return (mSize != oldSize);
    }

    public <F extends Traverser<? super E>>
    void traverseSoft(F func) {
        traverse(func, mRoot);
    }

    public <F extends Traverser<? super E>>
    void traverseHard(F func) {
        traverse(func, mRoot);
    }

    public Object clone() throws CloneNotSupportedException {
        LazySearchTree<E> newObject = (LazySearchTree<E>) super.clone();
        newObject.clear();  // can't point to other's data

        newObject.mRoot = cloneSubtree(mRoot);
        newObject.mSize = mSize;

        return newObject;
    }

    // private helper methods ----------------------------------------
    protected LazySTNode findMin(LazySTNode root) {
        if (root == null)
            return null;
        if (root.lftChild == null)
            return root;
        return findMin(root.lftChild);
    }

    protected LazySTNode findMax(LazySTNode root) {
        if (root == null)
            return null;
        if (root.rtChild == null)
            return root;
        return findMax(root.rtChild);
    }

    protected LazySTNode insert(LazySTNode root, E x) {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null) {
            mSize++;
            mSizeHard++;
            return new LazySTNode(x, null, null);
        }

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            root.lftChild = insert(root.lftChild, x);
        else if (compareResult > 0)
            root.rtChild = insert(root.rtChild, x);

        return root;
    }

    protected LazySTNode remove(LazySTNode root, E x) {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            root.lftChild = remove(root.lftChild, x);
        else if (compareResult > 0)
            root.rtChild = remove(root.rtChild, x);

            // found the node
        else if (root.lftChild != null && root.rtChild != null) {
            root.data = findMin(root.rtChild).data;
            root.rtChild = remove(root.rtChild, root.data);
        } else {
            root =
                    (root.lftChild != null) ? root.lftChild : root.rtChild;
            mSize--;
        }
        return root;
    }

    protected <F extends Traverser<? super E>>
    void traverse(F func, LazySTNode treeNode) {
        if (treeNode == null)
            return;

        traverse(func, treeNode.lftChild);
        func.visit(treeNode.data);
        traverse(func, treeNode.rtChild);
    }

    protected LazySTNode find(LazySTNode root, E x) {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            return find(root.lftChild, x);
        if (compareResult > 0)
            return find(root.rtChild, x);
        return root;   // found
    }

    protected LazySTNode cloneSubtree(LazySTNode root) {
        LazySTNode newNode;
        if (root == null)
            return null;

        // does not set myRoot which must be done by caller
        newNode = new LazySTNode
                (
                        root.data,
                        cloneSubtree(root.lftChild),
                        cloneSubtree(root.rtChild)
                );
        return newNode;
    }

    protected int findHeight(LazySTNode treeNode, int height) {
        int leftHeight, rightHeight;
        if (treeNode == null)
            return height;
        height++;
        leftHeight = findHeight(treeNode.lftChild, height);
        rightHeight = findHeight(treeNode.rtChild, height);
        return (leftHeight > rightHeight) ? leftHeight : rightHeight;
    }


    /**
     * LazyTree node class
     */
    private class LazySTNode{
        // use public access so the tree or other classes can access members
        protected LazySTNode lftChild, rtChild;
        protected E data;
        protected LazySTNode myRoot;  // needed to test for certain error
        protected boolean deleted;

        protected LazySTNode(E d, LazySTNode lft, LazySTNode rt) {
            lftChild = lft;
            rtChild = rt;
            data = d;
            deleted = false;
        }

        public LazySTNode() {
            this(null, null, null);
        }

        // function stubs -- for use only with AVL Trees when we extend
        public int getHeight() {
            return 0;
        }

        boolean setHeight(int height) {
            return true;
        }
    }
}
