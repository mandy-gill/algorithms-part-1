import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private static final double EPSILON = 1e-9;

    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points

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

        // create a copy of points
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsCopy[i] = points[i];
        }

        // sort the copy of points
        for (int i = 0; i < pointsCopy.length; i++) {
            for (int j = i; j > 0 && pointsCopy[j].compareTo(pointsCopy[j - 1]) < 0; j--) {
                exch(pointsCopy, j, j - 1);
            }
        }

        for (int i = 0; i < pointsCopy.length - 3; i++) {
            for (int j = i + 1; j < pointsCopy.length - 2; j++) {
                for (int k = j + 1; k < pointsCopy.length - 1; k++) {
                    for (int m = k + 1; m < pointsCopy.length; m++) {

                        Point p = pointsCopy[i];
                        Point q = pointsCopy[j];
                        Point r = pointsCopy[k];
                        Point s = pointsCopy[m];

                        // check if points are collinear
                        if ((safeEquals(p.slopeTo(q), p.slopeTo(r))) && (safeEquals(p.slopeTo(q), p.slopeTo(s)))) {

                            // construct a new line segment from p to (q,r,s)
                            LineSegment lsg = new LineSegment(p, s);

                            int len = lineSegments.length;

                            // create a copy of lineSegments with length = lineSegments.length + 1
                            LineSegment[] copy = getLineSegmentsCopy(len + 1);

                            // add lineSegment to copy
                            copy[len] = lsg;

                            // point lineSegments to copy
                            lineSegments = copy;
                        }
                    }
                }
            }
        }
    }

    private static void exch(Point[] a, int i, int j) {
        Point swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean safeEquals(double a, double b) {
        // Handles infinities and exact matches
        if (a == b) {
            return true;
        }
        // Handles rounding errors
        return Math.abs(a - b) < EPSILON;
    }

    private LineSegment[] getLineSegmentsCopy(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < lineSegments.length; i++) {
            copy[i] = lineSegments[i];
        }
        return copy;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}