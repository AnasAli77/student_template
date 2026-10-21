import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static class Point {
        int x, y, in;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextInt()) {
            int n = sc.nextInt();
            Point[] points = new Point[n];
            Point[] pointsY = new Point[n];

            for (int i = 0; i < n; i++) {
                int x = sc.nextInt();
                int y = sc.nextInt();
                points[i] = new Point(x, y);
                pointsY[i] = new Point(x, y);
            }

            Arrays.sort(points, (p1, p2) -> Integer.compare(p1.x, p2.x));
            Arrays.sort(pointsY, (p1, p2) -> Integer.compare(p1.y, p2.y));
            for (int i = 0; i < n; i++) {
                points[i].in = i;
            }
            ArrayList<Point> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                list.add(pointsY[i]);
            }
            double minPerimeter = getSmallestPerimeter(points, 0, n - 1, list);

            System.out.printf("%.3f\n", minPerimeter);
        }
    }

    private static double getSmallestPerimeter(Point[] points, int l, int r, ArrayList<Point> curY) {
        // base case
        int n = r - l + 1;
        if (n < 3) {
            return Double.MAX_VALUE;
        }
        if (n == 3) {
            return getPerimeter(points[l], points[l + 1], points[l + 2]);
        }

        int mid = l + (r - l) / 2;
        ArrayList<Point> y1 = new ArrayList<>(mid - l + 1);
        ArrayList<Point> y2 = new ArrayList<>(r - mid);
        for (Point i : curY) {
            if (i.in <= mid)
                y1.add(i);
            else
                y2.add(i);
        }

        double perimeter1 = getSmallestPerimeter(points, l, mid, y1);
        double perimeter2 = getSmallestPerimeter(points, mid + 1, r, y2);
        double perimeter = Math.min(perimeter1, perimeter2);

        // combine
        Point[] check = new Point[curY.size()];
        int checkSize = 0;
        for (Point i : curY) {
            if (Math.abs(i.x - points[mid].x) <= perimeter / 2.0)
                check[checkSize++] = i;
        }
        for (int i = 0; i < checkSize; i++) {
            for (int j = i + 1; j < checkSize && (check[j].y - check[i].y) < (perimeter / 2.0); j++) {
                for (int k = j + 1; k < checkSize && (check[k].y - check[i].y) < perimeter / 2.0; k++) {
                    double currentPerimeter = getPerimeter(check[i], check[j], check[k]);
                    perimeter = Math.min(perimeter, currentPerimeter);
                }
            }
        }
        return perimeter;
    }

    private static double getDistance(Point p1, Point p2) {
        long dx = (long) p1.x - p2.x;
        long dy = (long) p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double getPerimeter(Point p1, Point p2, Point p3) {
        return getDistance(p1, p2) + getDistance(p2, p3) + getDistance(p3, p1);
    }
}