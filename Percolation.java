import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Union-Find data structure
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF fullUF;

    // virtual top and bottom
    private final int vTop, vBottom;

    // grid and open indicators

    private boolean[][] grid;

    // class vars
    private final int n;
    private int trials = 0;

    // constructor
    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;

        // declare union-find
        uf = new WeightedQuickUnionUF(n * n + 2);
        fullUF = new WeightedQuickUnionUF(n * n + 2);

        vTop = 0;
        vBottom = n * n + 1;
        grid = new boolean[n][n]; // false by default
    }

    // opens site
    public void open(int row, int col) {

        if (valid(row, col)) {
            // ADJUSTED TO MAKE MATRIX 1-INDEXED
            final int adjustedRow = row - 1;
            final int adjustedCol = col - 1;

            // check if site already opened
            if (!grid[adjustedRow][adjustedCol]) {
                // +1 trial
                trials++;

                grid[adjustedRow][adjustedCol] = true;

                // check if top row
                if (row == 1) {
                    uf.union(vTop, getIndex(adjustedRow, adjustedCol));
                    fullUF.union(vTop, getIndex(adjustedRow, adjustedCol));
                }

                // checks if bottom row
                if (row == n) {
                    uf.union(vBottom, getIndex(adjustedRow, adjustedCol));
                }

                // checks adjacent sites if they're open
                checkAdjacent(adjustedRow, adjustedCol);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int numberOfOpenSites() {
        return trials;
    }

    public boolean isOpen(int row, int col) {
        // row = 1, col = 1 (0, 0)

        if (valid(row, col)) {
            return grid[row - 1][col - 1];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean isFull(int row, int col) {

        if (valid(row, col)) {
            // row = 1, col = 1 ADJUST
            /*
            final int aRow, aCol;
            aRow = row - 1; // row = 0
            aCol = col - 1; // col = 0

            if (aRow < 0 || aRow >= n || aCol < 0 || aCol >= n) {
                throw new IllegalArgumentException();
            } else {
                return (uf.find(vTop) == uf.find(getIndex(aRow, aCol)));
            } */
            return (fullUF.find(vTop) == fullUF.find(getIndex(row - 1, col - 1)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean percolates() {
        return (uf.find(vTop) == uf.find(vBottom));
    }

    private int getIndex(int row, int col) {
        // return index at site
        return n * row + col + 1;
    }

    // fills adjacent sites if full
    private void checkAdjacent(int row, int col) {

        // check top
        if (row - 1 >= 0) {
            if (grid[row - 1][col]) {
                uf.union(getIndex(row, col), getIndex(row - 1, col));
                fullUF.union(getIndex(row, col), getIndex(row - 1, col));
            }
        }

        // check bottom
        if (row + 1 < n) {
            if (grid[row + 1][col]) {
                uf.union(getIndex(row, col), getIndex(row + 1, col));
                fullUF.union(getIndex(row, col), getIndex(row + 1, col));
            }
        }

        // check left
        if (col - 1 >= 0) {
            if (grid[row][col - 1]) {
                uf.union(getIndex(row, col), getIndex(row, col - 1));
                fullUF.union(getIndex(row, col), getIndex(row, col - 1));
            }
        }

        // check right
        if (col + 1 < n) {
            if (grid[row][col + 1]) {
                uf.union(getIndex(row, col), getIndex(row, col + 1));
                fullUF.union(getIndex(row, col), getIndex(row, col + 1));
            }
        }
    }

    private boolean valid(int row, int col) {
        // row = 1, col = 1 (0, 0)
        // row = n, col = n (n-1, n-1)

        return (row > 0 && row <= n && col > 0 && col <= n);
    }

    public static void main(String[] args) {
        // StdOut.println("What is n?");
        int n = Integer.parseInt(args[0]);

        Percolation perc = new Percolation(n);

        do {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            if (!perc.isOpen(row, col)) {
                perc.open(row, col);
            }
        } while (!perc.percolates());

        if (perc.percolates()) {
            StdOut.println("Success! Amount of trials: " + perc.numberOfOpenSites());
        }
    }
}
