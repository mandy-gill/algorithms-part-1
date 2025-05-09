import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

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

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                for (int k = 0; k < points.length; k++) {
                    for (int m = 0; m < points.length; m++) {

                        // make sure all four points are distinct
                        if (j != i && (k != j && k != i) && (m != k && m != j && m != i)) {
                            Point p = points[i];
                            Point q = points[j];
                            Point r = points[k];
                            Point s = points[m];

                            // check if slope between p and q, p and r, and p and s is equal
                            if ((safeEquals(p.slopeTo(q), p.slopeTo(r))) && (safeEquals(p.slopeTo(q), p.slopeTo(s)))) {
                                boolean c1 = p.compareTo(q) < 0 && p.compareTo(r) < 0 && p.compareTo(s) < 0;
                                boolean c2 = q.compareTo(p) > 0 && q.compareTo(r) < 0 && q.compareTo(s) < 0;
                                boolean c3 = r.compareTo(p) > 0 && r.compareTo(q) > 0 && r.compareTo(s) < 0;
                                boolean c4 = s.compareTo(p) > 0 && s.compareTo(q) > 0 && s.compareTo(r) > 0;

                                // check if p, q, r, s are in order
                                if (c1 && c2 && c3 && c4) {

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
        }

    }

    private Point getSmallest(Point[] arr) {
        int min = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].compareTo(arr[min]) < 0) {
                min = i;
            }
        }
        return arr[min];
    }

    private Point getLargest(Point[] arr) {
        int min = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].compareTo(arr[min]) > 0) {
                min = i;
            }
        }
        return arr[min];
    }

    private static boolean safeEquals(double a, double b) {
        // Handles infinities and exact matches
        if (a == b) {
            return true;
        }
        // Handles rounding errors
        return Math.abs(a - b) < 1e-9;
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