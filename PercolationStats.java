import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double STD_VAR = 1.96;
    private final double[] amountOfTrials;

    public PercolationStats(int n, int trials) {

        if (n > 0 && trials > 0) {
            amountOfTrials = new double[trials];

            for (int i = 0; i < trials; i++) {

                // declare perc object
                Percolation perc = new Percolation(n);

                // run simulation
                do {
                    int row = StdRandom.uniform(1, n + 1);
                    int col = StdRandom.uniform(1, n + 1);
                    if (!perc.isOpen(row, col)) {
                        perc.open(row, col);
                    }
                } while (!perc.percolates());

                if (perc.percolates()) {
                    amountOfTrials[i] = perc.numberOfOpenSites();
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double mean() {
        return StdStats.mean(amountOfTrials);
    }

    public double stddev() {
        return StdStats.stddev(amountOfTrials);
    }

    public double confidenceLo() {
        return mean() - ((STD_VAR * stddev()) / Math.sqrt(this.amountOfTrials.length));
    }

    public double confidenceHi() {
        return mean() + ((STD_VAR * stddev()) / Math.sqrt(this.amountOfTrials.length));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats s = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + s.mean());
        StdOut.println("stddev                  = " + s.stddev());
        StdOut.println("95% confidence interval = " + s.confidenceLo()
                + ", " + s.confidenceHi());
    }
}
