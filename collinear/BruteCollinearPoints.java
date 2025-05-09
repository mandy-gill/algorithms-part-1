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

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int m = k + 1; m < points.length; m++) {

                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[m];

                        // check if points are collinear
                        if ((safeEquals(p.slopeTo(q), p.slopeTo(r))) && (safeEquals(p.slopeTo(q), p.slopeTo(s)))) {

                            int min = 0, max = 0;
                            Point[] pts = { p, q, r, s };
                            for (int n = 0; n < pts.length; n++) {
                                if (pts[n].compareTo(pts[min]) < 0) {
                                    min = n;
                                }
                                if (pts[n].compareTo(pts[max]) > 0) {
                                    max = n;
                                }
                            }

                            // construct a new line segment from p to (q,r,s)
                            LineSegment lsg = new LineSegment(pts[min], pts[max]);

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