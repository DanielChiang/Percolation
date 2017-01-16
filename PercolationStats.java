/*----------------------------------------------------------------
*  Author:        Daniel Chiang
*  Written:       1/15/2017
*  Last updated:  1/15/2017
*
*  Compilation:   javac -cp $CLASSPATH PercolationStats.java
*  Execution:     java PercolationStats
*  Dependencies:  Percolation.java, StdRandom.java, 
*                 StdStats.java, StdOut.java  
*
*
*  Statistical analysis for percolation program
*
*----------------------------------------------------------------*/


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;


public class PercolationStats {
    private int size;
    private int trails;
    private double[] means = null;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int t) {
        if (n < 1 || t < 1) {
            throw new java.lang.IllegalArgumentException();
        }

        size   = n * n;
        trails = t;
        means  = new double[t];

        for (int i = 0; i < t; ++i) {
            Percolation pc = new Percolation(n);
            do {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                pc.open(row, col);
            } while (!pc.percolates());

            means[i] = (double) pc.numberOfOpenSites() / size;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(means);
    }

    // sample standard deviation of percolation threshold                    
    public double stddev() {
        return StdStats.stddev(means);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(trails));
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(trails));
    }

    public static void main(String[] args) {

        int n      = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trails);

        StdOut.printf("%-24s= %.10f\n", "mean",  ps.mean());
        StdOut.printf("%-24s= %.10f\n", "stddev", ps.stddev());
        StdOut.printf("%-24s= %.10f, %.10f\n",
            "95% confidence interval", ps.confidenceLo(), ps.confidenceHi());
    }
}