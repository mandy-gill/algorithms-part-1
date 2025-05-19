import java.util.Iterator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private SearchNode initialSN;
    private Stack<Board> solutionBoards;
    private int minMoves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        initialSN = new SearchNode(initial, 0, null);

        // solve the board using the A* algorithm
        solutionBoards = null;
        minMoves = -1;

        SearchNode twinSN = new SearchNode(initialSN.board.twin(), 0, null);

        MinPQ<SearchNode> initialPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        initialPQ.insert(initialSN);
        twinPQ.insert(twinSN);

        while (!initialPQ.isEmpty() || !twinPQ.isEmpty()) {
            SearchNode snInitial = initialPQ.delMin();
            SearchNode snTwin = twinPQ.delMin();

            if (!snInitial.board.isGoal()) {
                Iterable<Board> stack = snInitial.board.neighbors();
                Iterator<Board> iter = stack.iterator();
                while (iter.hasNext()) {
                    Board b = iter.next();
                    if (snInitial.prev != null) {
                        if (!b.equals(snInitial.prev.board)) {
                            initialPQ.insert(new SearchNode(b, snInitial.numMoves + 1, snInitial));
                        }
                    } else {
                        initialPQ.insert(new SearchNode(b, snInitial.numMoves + 1, snInitial));
                    }

                }
            } else {
                Stack<Board> boards = new Stack<>();
                while (snInitial != null) {
                    boards.push(snInitial.board);
                    snInitial = snInitial.prev;
                }
                solutionBoards = boards;
                minMoves = solutionBoards.size() - 1;
                break;
            }

            if (!snTwin.board.isGoal()) {
                Iterable<Board> stack = snTwin.board.neighbors();
                Iterator<Board> iter = stack.iterator();
                while (iter.hasNext()) {
                    Board b = iter.next();
                    if (snTwin.prev != null) {
                        if (!b.equals(snTwin.prev.board)) {
                            twinPQ.insert(new SearchNode(b, snTwin.numMoves + 1, snInitial));
                        }
                    } else {
                        twinPQ.insert(new SearchNode(b, snTwin.numMoves + 1, snInitial));
                    }
                }
            } else {
                break;
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (solutionBoards == null) {
            return false;
        }
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return minMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutionBoards;
    }

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int numMoves;
        SearchNode prev;
        int manhattanPriority;

        public SearchNode(Board board, int numMoves, SearchNode prev) {
            this.board = board;
            this.numMoves = numMoves;
            this.prev = prev;
            this.manhattanPriority = board.manhattan() + numMoves;
        }

        public int compareTo(SearchNode that) {
            if (this.manhattanPriority < that.manhattanPriority) {
                return -1;
            } else if (this.manhattanPriority > that.manhattanPriority) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // test client (see below)
    public static void main(String[] args) {
    }
}
