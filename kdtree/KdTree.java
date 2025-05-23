import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    // private final String even = "EVEN";
    // private final String odd = "ODD";

    private RectHV rootRect;

    private Node root;

    // construct an empty set of points
    public KdTree() {
        rootRect = new RectHV(0, 0, 1, 1);
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
            root = new Node(p, rootRect, 1, 0);
            return;
        }
        int cmp = p.compareTo(root.p);
        if (cmp < 0) {
            root = insert(root, root.rect, root.level, p, true);
        } else if (cmp > 0) {
            root = insert(root, root.rect, root.level, p, false);
        }
    }

    private Node insert(Node x, RectHV rect, int level, Point2D p, boolean small) {
        if (x == null) {
            int nodeLevel = level + 1;
            RectHV nodeRect;
            // even level
            if (nodeLevel % 2 == 0) {
                if (small) {
                    nodeRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax() - p.x(), rect.ymax());
                } else {
                    nodeRect = new RectHV(rect.xmin() + p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                }
            }
            // odd level
            else {
                if (small) {
                    nodeRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), rect.ymax() - p.y());
                } else {
                    nodeRect = new RectHV(rect.xmin(), rect.ymin() + p.y(), rect.xmax(), rect.ymax());
                }
            }
            return new Node(p, nodeRect, 1, nodeLevel);
        }

        int cmp = p.compareTo(x.p);

        if (cmp < 0) {
            x.lb = insert(x.lb, x.rect, x.level, p, true);
        } else if (cmp > 0) {
            x.rt = insert(x.rt, x.rect, x.level, p, false);
        } else {
            // nothing
        }
        x.count = 1 + size(x.lb) + size(x.rt);
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) {
            return false;
        }
        int cmp = p.compareTo(x.p);
        if (cmp < 0) {
            return contains(x.lb, p);
        } else if (cmp > 0) {
            return contains(x.rt, p);
        } else {
            return true;
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
        return nearest(root, p, root.p);
    }

    private Point2D nearest(Node x, Point2D p, Point2D nPt) {
        // distance b/w closest point so far and p
        double dist1 = p.distanceSquaredTo(nPt);

        // distance b/w rect correspoding to node and p
        double dist2 = x.rect.distanceSquaredTo(p);

        if (dist1 > dist2) {
            double distLB = x.lb.rect.distanceSquaredTo(p);
            double distRT = x.rt.rect.distanceSquaredTo(p);
            nPt = x.p;
            if (distLB < distRT) {
                nPt = nearest(x.lb, p, nPt);
            } else {
                nPt = nearest(x.rt, p, nPt);
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
        private int level;

        public Node(Point2D p, RectHV rect, int count, int level) {
            this.p = p;
            this.rect = rect;
            this.count = count;
            this.level = level;
        }
    }

    // unit testing of the methods (optional)

    public static void main(String[] args) {
    }

}
