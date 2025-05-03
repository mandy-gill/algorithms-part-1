import java.util.Comparator;

public class FastCollinearPoints {

    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points

        // Throw exception if argument is null
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            // Throw exception if an entry in the arrray argument is null
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }

            for (int j = 0; j < points.length; j++) {
                // Throw exception if two entries in the array are equal
                if (j != i && points[j] == points[i]) {
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

            int numColPts = 0;
            for (int k = 1; k < otherPoints.length; k++) {
                if (p.slopeTo(otherPoints[k]) == p.slopeTo(otherPoints[k - 1])) {
                    numColPts++;
                } else {
                    if (numColPts >= 3) {
                        // get the collinear points
                        Point[] colPoints = new Point[numColPts + 1];
                        int cpIndex = 0;
                        colPoints[cpIndex++] = p;
                        for (int m = 0; m < numColPts; m++) {
                            colPoints[cpIndex++] = points[k - m - 1];
                        }
                        // add the col points to line segments array
                        addLsgToLsgs(colPoints);
                    }
                    numColPts = 0;
                }
            }
        }

    }

    private void addLsgToLsgs(Point[] pts) {
        int min = 0, max = 0;
        for (int i = 1; i < pts.length; i++) {
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
}
