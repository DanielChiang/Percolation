/*----------------------------------------------------------------
*  Author:        Daniel Chiang
*  Written:       1/15/2017
*  Last updated:  1/15/2017
*
*  Compilation:   javac -cp $CLASSPATH Percolation.java
*  Execution:     java Percolation
*  Dependencies:  WeightedQuickUnionUF.java
*  
*
*  A practical application of union-find problem: Percolation 
*
*----------------------------------------------------------------*/


import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int rowSize                 = 0;
    private int gridSize                = 0;
    private WeightedQuickUnionUF uf     = null;
    private boolean[] hasOpened         = null;
    private boolean[] isBottomConnected = null;
    private int sizeOfOpened            = 0;
    private boolean hasPercolated       = false;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new java.lang.IllegalArgumentException();
        }

        rowSize  = n;
        gridSize = n * n;

        // 1 additional room for dummy head
        int len           = gridSize + 1;
        uf                = new WeightedQuickUnionUF(len);
        hasOpened         = new boolean[len];
        isBottomConnected = new boolean[len];

        initialPercolate();
    }

    public void open(int row, int col) {
        int index = xyTo1D(row, col);
        validate(index);

        if (hasOpened[index]) return;

        hasOpened[index] = true;
        ++sizeOfOpened;

        int root;
        int top = index - rowSize;
        if (row != 1 && hasOpened[top]) {
            root = uf.find(top);
            isBottomConnected[index] |= isBottomConnected[root];
            uf.union(index, top);
        }

        int bottom = index + rowSize;
        if (row != rowSize && hasOpened[bottom]) {
            root = uf.find(bottom);
            isBottomConnected[index] |= isBottomConnected[root];
            uf.union(index, index + rowSize);
        }

        int left = index - 1;
        if (col % rowSize != 1 && hasOpened[left]) {
            root = uf.find(left);
            isBottomConnected[index] |= isBottomConnected[root];
            uf.union(index, left);
        }

        int right = index + 1;
        if (col % rowSize != 0 && hasOpened[right]) {
            root = uf.find(right);
            isBottomConnected[index] |= isBottomConnected[root];
            uf.union(index, right);
        }

        root = uf.find(index);
        isBottomConnected[root] |= isBottomConnected[index];
        
        // Check percolation here to avoid backwashing
        if (isBottomConnected[index] && uf.connected(0, index)) {
            hasPercolated = true;
        }
        
    }

    public boolean isOpen(int row, int col) {
        int index = xyTo1D(row, col);
        validate(index);

        return hasOpened[index];
    }

    public boolean isFull(int row, int col) {
        int index = xyTo1D(row, col);
        validate(index);

        return isOpen(row, col) && uf.connected(0, index);
    }
    public int numberOfOpenSites() {
        return sizeOfOpened;
    }

    public boolean percolates() {
        return hasPercolated;
}

    private void validate(int n) {
        if (n > gridSize + 1) throw new java.lang.IndexOutOfBoundsException();
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * rowSize + (col - 1) + 1;
    }

    private void initialPercolate() {
        int len = rowSize  + 1;
        int end = gridSize + 1;

        for (int i = 1; i < len; ++i) {
            uf.union(0, i);
            isBottomConnected[end - i] = true;
            // uf.union(end, end - i);
        }
    }

    public static void main(String[] args) {
    }
}