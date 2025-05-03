public class BruteCollinearPoints {

    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points

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
            for (int j = 0; j < points.length; j++) {
                for (int k = 0; k < points.length; k++) {
                    for (int m = 0; m < points.length; m++) {

                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[m];

                        // check if slope between p and q, p and r, and p and s is equal
                        if ((p.slopeTo(q) == p.slopeTo(r)) && (p.slopeTo(q) == p.slopeTo(s))) {
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
}