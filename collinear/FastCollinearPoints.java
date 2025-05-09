import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

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

        // initialize lineSegments array
        lineSegments = new LineSegment[0];

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            // get a new array of all points except p
            Point[] otherPoints = new Point[points.length - 1];
            int opIndex = 0;
            for (int j = 0; j < points.length; j++) {
                if (j != i) {
                    otherPoints[opIndex++] = points[j];
                }
            }

            // sort other points according to the slopes they make with p
            sort(otherPoints, p.slopeOrder());

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
                        if (isPSmallest(colPoints, p)) {
                            addLsgToLsgs(colPoints);
                        }
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
                if (isPSmallest(colPoints, p)) {
                    addLsgToLsgs(colPoints);
                }
            }
        }

    }

    private boolean isPSmallest(Point[] pts, Point p) {
        for (int i = 0; i < pts.length; i++) {
            if (p.compareTo(pts[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean safeEquals(double a, double b) {
        // Handles infinities and exact matches
        if (a == b) {
            return true;
        }
        // Handles rounding errors
        return Math.abs(a - b) < 1e-9;
    }

    private void addLsgToLsgs(Point[] pts) {
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
        LineSegment lsg = new LineSegment(pts[min], pts[max]);
        int len = lineSegments.length;
        LineSegment[] copy = getLineSegmentsCopy(len + 1);
        copy[len] = lsg;
        lineSegments = copy;
    }

    private LineSegment[] getLineSegmentsCopy(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < lineSegments.length; i++) {
            copy[i] = lineSegments[i];
        }
        return copy;
    }

    private static void sort(Point[] a, Comparator<Point> comparator) {
        int n = a.length;
        for (int i = 0; i < n; i++)
            for (int j = i; j > 0 && less(comparator, a[j], a[j - 1]); j--)
                exch(a, j, j - 1);
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
