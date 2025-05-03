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

        lineSegments = new LineSegment[0];

        Point p = points[0];

        sort(points, p.slopeOrder());

        Point[] colPoints = new Point[points.length];
        int x = 0;
        colPoints[x++] = p;

        for (int i = 1; i < points.length; i++) {
            if (p.slopeTo(points[i]) - p.slopeTo(points[i - 1]) < 1e-9) {
                colPoints[x++] = points[i];
            } else {
                if (colPoints.length > 4) {
                    LineSegment lineSegment = new LineSegment(p, colPoints[colPoints.length - 1]);
                    LineSegment[] lineSegmentsCopy = new LineSegment[lineSegments.length + 1];
                    for (int j = 0; j < lineSegmentsCopy.length; j++) {
                        lineSegmentsCopy[j] = lineSegments[j];
                    }
                    lineSegmentsCopy[lineSegmentsCopy.length - 1] = lineSegment;
                    lineSegments = lineSegmentsCopy;
                }
            }
        }

    }

    private static void sort(Point[] a, Comparator<Point> comparator) {
        int N = a.length;
        for (int i = 0; i < N; i++)
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
        return lineSegments;
    }
}
