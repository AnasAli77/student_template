import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        FastScanner fs = new FastScanner();
        String token = fs.next();
        if (token == null) return;

        int numTestCases = Integer.parseInt(token);
        minPerimeterTriangle solver = new minPerimeterTriangle();
        StringBuilder out = new StringBuilder();

        for (int t = 0; t < numTestCases; t++) {
            int n = fs.nextInt();
            Point[] points = new Point[n];

            for (int i = 0; i < n; i++) {
                points[i] = new Point(fs.nextInt(), fs.nextInt());
            }

            double minPerimeter = solver.getMinPerimeterTriangle(points);
            out.append(String.format(Locale.US, "%.3f\n", minPerimeter));
        }

        System.out.print(out);
    }
}


class FastScanner {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer("");

    String next() {
        while (!st.hasMoreTokens()) {
            try {
                String line = br.readLine();
                if (line == null) return null;
                st = new StringTokenizer(line);
            } catch (IOException e) {
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() {
        return Integer.parseInt(next());
    }
}