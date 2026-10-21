package submissions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

public class MinPerimeterTriangleTest {

    private static final double EPSILON = 1e-6;
    private final minPerimeterTriangle solver = new minPerimeterTriangle();

    // Helper to convert 2D int array into Point[]
    private Point[] toPoints(int[][] coords) {
        Point[] pts = new Point[coords.length];
        for (int i = 0; i < coords.length; i++) {
            pts[i] = new Point(coords[i][0], coords[i][1]);
        }
        return pts;
    }

    // O(n^3) Brute-Force Oracle for ground-truth verification
    private double bruteForceMinPerimeter(Point[] pts) {
        int n = pts.length;
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double d1 = Math.hypot(pts[i].x - pts[j].x, pts[i].y - pts[j].y);
                for (int k = j + 1; k < n; k++) {
                    double d2 = Math.hypot(pts[j].x - pts[k].x, pts[j].y - pts[k].y);
                    double d3 = Math.hypot(pts[i].x - pts[k].x, pts[i].y - pts[k].y);
                    best = Math.min(best, d1 + d2 + d3);
                }
            }
        }
        return best;
    }

    @Test
    public void testStandardCase() {
        // Triangle (0,0)-(3,0)-(1,1) has sides 3, sqrt(5), sqrt(2) -> 6.6502815
        Point[] pts = toPoints(new int[][]{
                {0, 0}, {3, 0}, {0, 4}, {1, 1}
        });
        double expected = 3.0 + Math.sqrt(5) + Math.sqrt(2);
        assertEquals(expected, solver.getMinPerimeterTriangle(pts), EPSILON);
    }

    @Test
    public void testCollinearPoints() {
        // Collinear points on y = 5: (1,5), (2,5), (4,5) -> perimeter = 2 * (4 - 1) = 6.0
        Point[] pts = toPoints(new int[][]{
                {1, 5}, {2, 5}, {4, 5}, {100, 100}
        });
        assertEquals(6.0, solver.getMinPerimeterTriangle(pts), EPSILON);
    }

    @Test
    public void testCrossDividingLineStrip() {
        // 6 points where the optimal triangle (4,2)-(5,2)-(5,3) straddles x_mid = 4.5
        Point[] pts = toPoints(new int[][]{
                {0, 0}, {0, 5}, {4, 2}, {5, 2}, {5, 3}, {10, 0}
        });
        double expected = 1.0 + 1.0 + Math.sqrt(2); // ~3.41421356
        assertEquals(expected, solver.getMinPerimeterTriangle(pts), EPSILON);
    }

    @Test
    public void testAllSameXCoordinate() {
        // All points on vertical line x = 7
        Point[] pts = toPoints(new int[][]{
                {7, 100}, {7, 10}, {7, 13}, {7, 11}, {7, 50}, {7, 200}
        });
        // Closest triple is (7,10), (7,11), (7,13) -> perimeter = 2 * (13 - 10) = 6.0
        assertEquals(6.0, solver.getMinPerimeterTriangle(pts), EPSILON);
    }

    @Test
    public void testBaseCasesSmallN() {
        // Exactly 3 points: 3-4-5 right triangle -> perimeter = 12.0
        Point[] threePts = toPoints(new int[][]{
                {0, 0}, {3, 0}, {0, 4}
        });
        assertEquals(12.0, solver.getMinPerimeterTriangle(threePts), EPSILON);

        // Fewer than 3 points -> POSITIVE_INFINITY
        Point[] twoPts = toPoints(new int[][]{
                {0, 0}, {3, 0}
        });
        assertEquals(Double.POSITIVE_INFINITY, solver.getMinPerimeterTriangle(twoPts));
    }

    @Test
    public void testFourPointsSplittingIntoTwoAndTwo() {
        // Tests n=4 splitting into left (n=2 -> INF) and right (n=2 -> INF),
        // forcing the answer to be found purely in the strip merge step!
        Point[] pts = toPoints(new int[][]{
                {0, 0}, {0, 2}, {2, 0}, {10, 10}
        });
        double expected = 2.0 + 2.0 + Math.sqrt(8); // ~6.828427
        assertEquals(expected, solver.getMinPerimeterTriangle(pts), EPSILON);
    }

    @Test
    public void testLargeCoordinatesNoOverflow() {
        // Coordinates up to 10^8 where (dx*dx) in int would overflow if not using Math.pow / double
        Point[] pts = toPoints(new int[][]{
                {-100_000_000, -100_000_000},
                {100_000_000, 100_000_000},
                {50_000_000, 50_000_000},
                {50_000_003, 50_000_000},
                {50_000_000, 50_000_004}
        });
        assertEquals(12.0, solver.getMinPerimeterTriangle(pts), EPSILON);
    }

    @Test
    public void testRandomizedAgainstBruteForce() {
        Random rand = new Random(42);
        for (int trial = 0; trial < 100; trial++) {
            int n = 4 + rand.nextInt(70); // N between 4 and 73
            Point[] pts1 = new Point[n];
            Point[] pts2 = new Point[n];

            // Generate distinct random points
            for (int i = 0; i < n; i++) {
                int x = rand.nextInt(1000) - 500;
                int y = rand.nextInt(1000) - 500;
                // Ensure distinctness
                boolean duplicate = false;
                for (int j = 0; j < i; j++) {
                    if (pts1[j].x == x && pts1[j].y == y) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    i--;
                    continue;
                }
                pts1[i] = new Point(x, y);
                pts2[i] = new Point(x, y);
            }

            double expected = bruteForceMinPerimeter(pts1);
            double actual = solver.getMinPerimeterTriangle(pts2);

            assertEquals(expected, actual, EPSILON, "Failed on random trial " + trial + " with N=" + n);
        }
    }

    @Test
    public void testLargeInputPerformance() {
        int n = 100_000;
        Random rand = new Random(12345);
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(rand.nextInt(10_000_000), rand.nextInt(10_000_000));
        }

        long startMs = System.currentTimeMillis();
        double result = solver.getMinPerimeterTriangle(pts);
        long elapsedMs = System.currentTimeMillis() - startMs;

        assertTrue(result > 0 && Double.isFinite(result));
        assertTrue(elapsedMs < 2000, "Took too long: " + elapsedMs + " ms (Expected < 2000 ms for N=100,000)");
    }
}