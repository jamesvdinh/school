import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final boolean[][] grid;
    private final int size;
    private int numOpen;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF gravityUf;
    private final int topVirtual;
    private final int bottomVirtual;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("invalid grid size, must be greater than 0");
        }

        this.grid = new boolean[N][N];
        this.size = N;
        this.numOpen = 0;
        this.uf = new WeightedQuickUnionUF(N * N + 2); // size of grid + virtual spots
        this.gravityUf = new WeightedQuickUnionUF(N * N + 2);
        this.topVirtual = N * N;
        this.bottomVirtual = N * N + 1;
    }

    public void open(int row, int col) {
        if (!checkValid(row, col) || grid[row][col]) {
            return;
        }
        grid[row][col] = true;
        numOpen++;

        // connect to topVirtual if first row
        if (row == 0) {
            uf.union(topVirtual, index(row, col));
            gravityUf.union(topVirtual, index(row, col));
        }
        // connect to bottomVirtual if last row
        if (row == this.size - 1) {
            uf.union(bottomVirtual, index(row, col));
        }
        connectAdjacent(row, col, row - 1, col); // down
        connectAdjacent(row, col, row + 1, col); // up
        connectAdjacent(row, col, row, col - 1); // left
        connectAdjacent(row, col, row, col + 1); // right
    }

    private int index(int row, int col) {
        return row * this.size + col;
    }

    private void connectAdjacent(int row, int col, int adjRow, int adjCol) {
        if (checkValid(adjRow, adjCol) && isOpen(adjRow, adjCol)) {
            uf.union(index(row, col), index(adjRow, adjCol));
            gravityUf.union(index(row, col), index(adjRow, adjCol));
        }
    }

    private boolean checkValid(int row, int col) {
        return row >= 0 && row < this.size && col >= 0 && col < this.size;
    }

    public boolean isOpen(int row, int col) {
        return checkValid(row, col) && grid[row][col];
    }

    public boolean isFull(int row, int col) {
        return checkValid(row, col) && gravityUf.find(index(row, col)) == gravityUf.find(topVirtual);
    }

    public int numberOfOpenSites() {
        return this.numOpen;
    }

    public boolean percolates() {
        return uf.find(bottomVirtual) == uf.find(topVirtual);
    }
}
