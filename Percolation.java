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
    private int rowSize             = 0;
    private int gridSize            = 0;
    private WeightedQuickUnionUF uf = null;
    private boolean[] hasOpened     = null;
    private int sizeOfOpened        = 0;
    private boolean hasPercolated   = false;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if ((n != (int) n) || n < 1) {
            throw new java.lang.IllegalArgumentException();
        }

        rowSize   = n;
        gridSize  = n * n;

        // 1 additional room for dummy head
        int len   = gridSize + 1;
        uf        = new WeightedQuickUnionUF(len);
        hasOpened = new boolean[len];

        initialPercolate();
    }

    public void open(int row, int col) {
        int index = this.xyTo1D(row, col);
        this.validate(index);

        if (!this.hasOpened[index]) {
            this.hasOpened[index] = true;
            ++this.sizeOfOpened;

            // Top
            if (row != 1 && this.hasOpened[index - this.rowSize]) {
                this.uf.union(index, index - this.rowSize);
            }

            // Botton
            if (row != this.rowSize && this.hasOpened[index + this.rowSize]) {
                this.uf.union(index, index + this.rowSize);
            }

            // Left
            if (col % this.rowSize != 1 && this.hasOpened[index - 1]) {
                this.uf.union(index, index - 1);
            }

            // Right
            if (col % this.rowSize != 0 && this.hasOpened[index + 1]) {
                this.uf.union(index, index + 1);
            }

            // Check percolation here to avoid backwashing 
            if (row == this.rowSize && this.uf.connected(0, index)) {
                this.hasPercolated = true;
            }
        }
        
    }

    public boolean isOpen(int row, int col) {
        int index = this.xyTo1D(row, col);
        this.validate(index);

        return this.hasOpened[index];
    }

    public boolean isFull(int row, int col) {
        int index = this.xyTo1D(row, col);
        this.validate(index);

        return this.isOpen(row, col) && this.uf.connected(0, index);
    }
    public int numberOfOpenSites() {
        return this.sizeOfOpened;
    }

    public boolean percolates() {
        return this.hasPercolated;
}

    private void validate(int n) {
        if (n > this.gridSize + 1) throw new java.lang.IndexOutOfBoundsException();
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * this.rowSize + (col - 1) + 1;
    }

    private void initialPercolate() {
        int len = this.rowSize  + 1;
        // int end = this.gridSize + 1;

        for (int i = 1; i < len; ++i) {
            this.uf.union(0, i);
            // this.uf.union(end, end - i);
        }
    }

    public static void main(String[] args) {
    }
}