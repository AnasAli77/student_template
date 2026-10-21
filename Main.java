
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Locale;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st = new StringTokenizer("");

    static String next() {
        while (!st.hasMoreTokens()) {
            try {
                String line = br.readLine();
                if (line == null)
                    return null;
                st = new StringTokenizer(line);
            } catch (IOException e) {
                return null;
            }
        }
        return st.nextToken();
    }

    static int nextInt() {
        return Integer.parseInt(next());
    }

    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // --- ALGORITHM METHODS MOVED DIRECTLY INTO MAIN ---

    static double euclideanDistances(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    static double perimeter(Point p1, Point p2, Point p3) {
        return euclideanDistances(p1, p2) +
                euclideanDistances(p1, p3) +
                euclideanDistances(p2, p3);
    }

    static double getMinPerimeterTriangle(Point[] points) {
        Arrays.sort(points, Comparator.comparingInt((Point p) -> p.x).thenComparingInt((Point p) -> p.y));
        Point[] temp = new Point[points.length];
        return getMinPerimeterTriangle(points, temp, 0, points.length);
    }

    static double getMinPerimeterTriangle(Point[] points, Point[] temp, int l, int r) {
        if (r - l <= 3) {
            Arrays.sort(points, l, r, Comparator.comparingInt((Point p) -> p.y).thenComparingInt((Point p) -> p.x));
            if (r - l < 3)
                return Double.POSITIVE_INFINITY;
            return perimeter(points[l], points[l + 1], points[l + 2]);
        }

        int mid = (r + l) / 2;
        int midx = points[mid].x;
        double lp = getMinPerimeterTriangle(points, temp, l, mid);
        double rp = getMinPerimeterTriangle(points, temp, mid, r);
        double d = Math.min(lp, rp);

        // merge l----mid, mid----r by y.
        int i = l, j = mid, k = l;
        while (i < mid && j < r) {
            if (points[i].y <= points[j].y)
                temp[k++] = points[i++];
            else
                temp[k++] = points[j++];
        }
        while (i < mid)
            temp[k++] = points[i++];
        while (j < r)
            temp[k++] = points[j++];

        int stripCount = 0;
        for (int t = l; t < r; ++t) {
            points[t] = temp[t];
            if (Math.abs(points[t].x - midx) < d / 2.0)
                temp[stripCount++] = points[t];
        }

        for (int a = 0; a < stripCount; ++a) {
            for (int b = a + 1; b < stripCount; ++b) {
                if (temp[b].y - temp[a].y >= d / 2.0)
                    break;
                double disAB = euclideanDistances(temp[a], temp[b]);
                if (disAB >= d / 2.0)
                    continue;
                for (int c = b + 1; c < stripCount; ++c) {
                    if (temp[c].y - temp[a].y >= d / 2.0)
                        break;
                    double disBC = euclideanDistances(temp[b], temp[c]);
                    double disAC = euclideanDistances(temp[a], temp[c]);
                    d = Math.min(d, disAB + disBC + disAC);
                }
            }
        }
        return d;
    }

    // --- MAIN EXECUTION ---
    public static void main(String[] args) {

        String token = next();
        if (token == null)
            return;

        int numTestCases = Integer.parseInt(token);
        StringBuilder out = new StringBuilder();

        for (int t = 0; t < numTestCases; t++) {

            int n = nextInt();
            Point[] points = new Point[n];

            for (int i = 0; i < n; i++) {
                points[i] = new Point(nextInt(), nextInt());
            }

            // Call the static method directly
            double minPerimeter = getMinPerimeterTriangle(points);
            out.append(String.format(Locale.US, "%.3f\n", minPerimeter));
        }

        System.out.print(out);
    }
}