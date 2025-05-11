import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private static final double EPSILON = 1e-9;

    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points

        // Throw exception if argument is null
        if (points == null) {
            throw new IllegalArgumentException();
        }

        // Throw exception if an entry in the array argument is null
        for (int i = 0; i < points.length; i++) {

            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        // Throw exception if two entries in the array are equal
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {

                if (j != i && points[j].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // get a copy of points array
        Point[] otherPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            otherPoints[i] = points[i];
        }

        // initialize lineSegments array
        lineSegments = new LineSegment[0];

        for (int i = 0; i < points.length; i++) { // N
            Point p = points[i];

            // sort other points according to the slopes they make with p
            sort(otherPoints, p.slopeOrder()); // N LOG N

            // Check if any 3 (or more) adjacent points in the sorted order have equal
            // slopes with respect to p. If so, these points, together with p, are
            // collinear.

            int numColPts = 1;
            for (int k = 1; k < otherPoints.length; k++) {
                if (safeEquals(p.slopeTo(otherPoints[k]), p.slopeTo(otherPoints[k - 1]))) {
                    numColPts++;
                } else {
                    if (numColPts >= 3) {
                        // get the collinear points
                        Point[] colPoints = new Point[numColPts + 1];
                        int cpIndex = 0;
                        colPoints[cpIndex++] = p;
                        for (int m = 0; m < numColPts; m++) {
                            colPoints[cpIndex++] = otherPoints[k - m - 1];
                        }
                        // add the col points to line segments array if p is smallest point
                        addLsgToLsgs(colPoints, p);
                    }
                    numColPts = 1;
                }
            }

            // collinear points at the end
            if (numColPts >= 3) {
                // get the collinear points
                Point[] colPoints = new Point[numColPts + 1];
                int cpIndex = 0;
                colPoints[cpIndex++] = p;
                for (int m = 0; m < numColPts; m++) {
                    colPoints[cpIndex++] = otherPoints[(otherPoints.length - 1) - m];
                }
                // add the col points to line segments array if p is smallest point
                addLsgToLsgs(colPoints, p);
            }
        }

    }

    private static boolean safeEquals(double a, double b) {
        // Handles infinities and exact matches
        if (a == b) {
            return true;
        }
        // Handles rounding errors
        return Math.abs(a - b) < EPSILON;
    }

    private void addLsgToLsgs(Point[] pts, Point p) {
        // find min and max
        int min = 0, max = 0;
        for (int i = 0; i < pts.length; i++) {
            if (pts[i].compareTo(pts[min]) < 0) {
                min = i;
            }
            if (pts[i].compareTo(pts[max]) > 0) {
                max = i;
            }
        }
        if (p == pts[min]) {
            LineSegment lsg = new LineSegment(pts[min], pts[max]);
            int len = lineSegments.length;
            LineSegment[] copy = getLineSegmentsCopy(len + 1);
            copy[len] = lsg;
            lineSegments = copy;
        }
    }

    private LineSegment[] getLineSegmentsCopy(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < lineSegments.length; i++) {
            copy[i] = lineSegments[i];
        }
        return copy;
    }

    private static void sort(Point[] a, Comparator<Point> c) {
        Point[] aux = new Point[a.length];
        sort(a, aux, 0, a.length - 1, c);
    }

    private static void sort(Point[] a, Point[] aux, int lo, int hi, Comparator<Point> c) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, c);
        sort(a, aux, mid + 1, hi, c);
        merge(a, aux, lo, mid, hi, c);
    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Comparator<Point> c) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (less(c, aux[j], aux[i])) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
    }

    private static boolean less(Comparator<Point> c, Point v, Point w) {
        return c.compare(v, w) < 0;
    }

    private static void exch(Point[] a, int i, int j) {
        Point swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    public int numberOfSegments() { // the number of line segments
        return lineSegments.length;
    }

    public LineSegment[] segments() { // the line segments
        LineSegment[] copy = getLineSegmentsCopy(lineSegments.length);
        return copy;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
