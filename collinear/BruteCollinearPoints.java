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

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                for (int k = 0; k < points.length; k++) {
                    for (int l = 0; l < points.length; l++) {

                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[l];

                        if ((p.slopeTo(q) - p.slopeTo(r) < 1e-9) && (p.slopeTo(q) - p.slopeTo(s)) < 1e-9) {
                            LineSegment lineSegment = new LineSegment(p, s);
                            lineSegments = new LineSegment[1];
                            lineSegments[0] = lineSegment;
                        } else {
                            lineSegments = new LineSegment[0];
                        }

                    }
                }
            }
        }

    }

    public int numberOfSegments() { // the number of line segments
        return lineSegments.length;
    }

    public LineSegment[] segments() { // the line segments
        return lineSegments;
    }
}