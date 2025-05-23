import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private SET<Point2D> root;

    // construct an empty set of points
    public PointSET() {
        root = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return root.isEmpty();
    }

    // number of points in the set
    public int size() {
        return root.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return root.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : root) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<Point2D> q = new Queue<Point2D>();

        for (Point2D p : root) {
            if (rect.contains(p)) {
                q.enqueue(p);
            }
        }

        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nPt = null;
        double nDist = Double.POSITIVE_INFINITY;

        for (Point2D pt : root) {
            if (p.distanceSquaredTo(pt) < nDist) {
                nPt = pt;
                nDist = p.distanceSquaredTo(nPt);
            }
        }

        return nPt;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}