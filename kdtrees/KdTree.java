import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private final RectHV rootRect;
    private final int rootLevel;

    private Node root;

    // construct an empty set of points
    public KdTree() {
        rootRect = new RectHV(0, 0, 1, 1);
        rootLevel = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            root = new Node(p, rootRect, 1);
            return;
        }
        root = insert(root, p, rootLevel, 0, 1, 0, 1);
    }

    private Node insert(Node x, Point2D p, int level, double rxmin, double rxmax, double rymin, double rymax) {
        if (x == null) {
            return new Node(p, new RectHV(rxmin, rymin, rxmax, rymax), 1);
        }

        if (p.equals(x.p)) {
            return x;
        }

        int cmp;
        double nodeX = x.p.x();
        double nodeY = x.p.y();
        if (level % 2 == 0) {
            cmp = Double.compare(p.x(), nodeX);
        } else {
            cmp = Double.compare(p.y(), nodeY);
        }
        if (cmp < 0) {
            // less, even level
            if (level % 2 == 0) {
                rxmax = nodeX;
            }
            // less, odd level
            else {
                rymax = nodeY;
            }
            x.lb = insert(x.lb, p, level + 1, rxmin, rxmax, rymin, rymax);
        } else {
            // more, even level
            if (level % 2 == 0) {
                rxmin = nodeX;
            }
            // more, odd level
            else {
                rymin = nodeY;
            }
            x.rt = insert(x.rt, p, level + 1, rxmin, rxmax, rymin, rymax);
        }
        x.count = 1 + size(x.lb) + size(x.rt);
        return x;

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p, 0);
    }

    private boolean contains(Node x, Point2D p, int level) {
        if (x == null) {
            return false;
        }
        if (p.equals(x.p)) {
            return true;
        }
        int cmp;
        if (level % 2 == 0) {
            cmp = Double.compare(p.x(), x.p.x());
        } else {
            cmp = Double.compare(p.y(), x.p.y());
        }

        if (cmp < 0) {
            return contains(x.lb, p, level + 1);
        } else {
            return contains(x.rt, p, level + 1);
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) {
            return;
        }
        draw(x.lb);
        x.p.draw();
        draw(x.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q);
        return q;
    }

    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (x == null) {
            return;
        }
        if (rect.intersects(x.rect)) {
            if (rect.contains(x.p)) {
                q.enqueue(x.p);
            }
            range(x.lb, rect, q);
            range(x.rt, rect, q);
        }
        return;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        return nearest(root, p, root.p);
    }

    private Point2D nearest(Node x, Point2D p, Point2D nPt) {
        if (x == null) {
            return nPt;
        }
        // distance b/w closest point so far and p
        double dist1 = nPt.distanceSquaredTo(p);

        // distance b/w rect correspoding to node and p
        double dist2 = x.rect.distanceSquaredTo(p);

        if (dist2 < dist1) {
            double dist3 = x.p.distanceSquaredTo(p);
            if (dist3 < dist1) {
                nPt = x.p;
            }
            if (x.lb == null) {
                return nearest(x.rt, p, nPt);
            }
            if (x.rt == null) {
                return nearest(x.lb, p, nPt);
            }
            double distLB = x.lb.rect.distanceSquaredTo(p);
            double distRT = x.rt.rect.distanceSquaredTo(p);
            if (distLB < distRT) {
                nPt = nearest(x.lb, p, nPt);
                nPt = nearest(x.rt, p, nPt);
            } else {
                nPt = nearest(x.rt, p, nPt);
                nPt = nearest(x.lb, p, nPt);
            }
        }
        return nPt;
    }

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private int count;

        public Node(Point2D p, RectHV rect, int count) {
            this.p = p;
            this.rect = rect;
            this.count = count;
        }
    }

    // unit testing of the methods (optional)

    public static void main(String[] args) {

    }

}
