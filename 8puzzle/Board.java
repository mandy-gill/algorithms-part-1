import edu.princeton.cs.algs4.Stack;

public class Board {

    private int[][] tiles;
    private int n; // stores the dimension of board

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String toReturn = "" + n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                toReturn += this.tiles[i][j];
                if (j != n - 1) {
                    toReturn += " ";
                }
            }
            toReturn += "\n";
        }
        return toReturn;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hdist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != n - 1 && j != n - 1) {
                    if (tiles[i][j] != ((i * n) + j + 1)) {
                        hdist++;
                    }
                } else {
                    if (tiles[i][j] != 0) {
                        hdist++;
                    }
                }
            }
        }
        return hdist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int mdist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = tiles[i][j];
                if (x != 0) {
                    int gi = x / n;
                    int gj = x - (x / n) * n;
                    mdist += Math.abs(gi - i) + Math.abs(gj - j);
                } else {
                    mdist += Math.abs((n - 1) - i) + Math.abs((n - 1) - j);
                }
            }
        }
        return mdist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != n - 1 && j != n - 1) {
                    if (tiles[i][j] != ((i * n) + j + 1)) {
                        return false;
                    }
                } else {
                    if (tiles[i][j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // does this board equal y?
    // Two boards are equal if they are have the same size and their corresponding
    // tiles are in the same positions
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (n != that.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    // HOW TO IMPLEMENT STACK FOR THIS???
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        int row = 0, col = 0;
        // get row and col indices of 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        // add left neighbour
        if (row - 1 >= 0) {
            Board nb = new Board(tiles);
            int temp = nb.tiles[row - 1][col];
            nb.tiles[row - 1][col] = 0;
            nb.tiles[row][col] = temp;
            stack.push(nb);
        }
        // add right neighbour
        if (row + 1 <= n) {
            Board nb = new Board(tiles);
            int temp = nb.tiles[row + 1][col];
            nb.tiles[row + 1][col] = 0;
            nb.tiles[row][col] = temp;
            stack.push(nb);
        }
        // add bottom neighbour
        if (col + 1 <= n) {
            Board nb = new Board(tiles);
            int temp = nb.tiles[row][col + 1];
            nb.tiles[row][col + 1] = 0;
            nb.tiles[row][col] = temp;
            stack.push(nb);
        }
        // add top neighbour
        if (col - 1 >= 0) {
            Board nb = new Board(tiles);
            int temp = nb.tiles[row][col - 1];
            nb.tiles[row][col - 1] = 0;
            nb.tiles[row][col] = temp;
            stack.push(nb);
        }
        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int fi = 0, fj = 0, si = 0, sj = 0;
        boolean firstFound = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    if (!firstFound) {
                        fi = i;
                        fj = j;
                        firstFound = true;
                    } else {
                        si = i;
                        sj = j;
                        break;
                    }
                }
            }
        }
        Board nb = new Board(tiles);
        int temp = nb.tiles[fi][fj];
        nb.tiles[fi][fj] = nb.tiles[si][sj];
        nb.tiles[si][sj] = temp;
        return nb;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
    }

}
