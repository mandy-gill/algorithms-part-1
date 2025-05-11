import java.util.Iterator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private MinPQ<SearchNode> pq;
    private SearchNode initialSN;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        pq = new MinPQ<>();
        SearchNode sn = new SearchNode(initial, 0, null);
        this.initialSN = sn;
        pq.insert(sn);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (solution() == null) {
            return false;
        }
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return initialSN.hp;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> boards = new Stack<>();
        while (!pq.isEmpty()) {
            SearchNode sn = pq.delMin();
            boards.push(sn.board);
            if (!sn.board.isGoal()) {
                Iterable<Board> stack = sn.board.neighbors();
                Iterator<Board> iter = stack.iterator();
                while (iter.hasNext()) {
                    Board b = iter.next();
                    pq.insert(new SearchNode(b, sn.numMoves + 1, sn));
                }
                sn = pq.delMin();
                boards.push(sn.board);
            } else {
                return boards;
            }
        }
        return null;
    }

    private class SearchNode {
        Board board;
        int numMoves;
        SearchNode prev;
        int hp;

        public SearchNode(Board board, int numMoves, SearchNode prev) {
            this.board = board;
            this.numMoves = numMoves;
            this.prev = prev;
            this.hp = board.hamming() + numMoves;
        }

        public int compareTo(SearchNode sn) {
            if (this.hp < sn.hp) {
                return -1;
            } else if (this.hp > sn.hp) {
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
