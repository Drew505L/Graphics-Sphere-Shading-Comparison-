package HW;

import java.util.ArrayList;
import java.util.Collections;

public class ScanlineFill {
    public static void fillTriangle(int[][][] fb, Triangle tri) {
        VectorAbstract[] v = tri.getVertices();

        int x0 = (int)v[0].getX(); int y0 = (int)v[0].getY();
        int x1 = (int)v[1].getX(); int y1 = (int)v[1].getY();
        int x2 = (int)v[2].getX(); int y2 = (int)v[2].getY();

        ColorAbstract c = v[0].getColor();

        int minY = Math.max(0, Math.min(y0, Math.min(y1, y2)));
        int maxY = Math.min(511, Math.max(y0, Math.max(y1, y2)));

        for (int y = minY; y <= maxY; y++) {

            ArrayList<Integer> intersections = new ArrayList<>();

            intersect(y, x0, y0, x1, y1, intersections);
            intersect(y, x1, y1, x2, y2, intersections);
            intersect(y, x2, y2, x0, y0, intersections);

            if (intersections.size() >= 2) {

                Collections.sort(intersections);

                int xStart = intersections.get(0);
                int xEnd = intersections.get(1);

                xStart = Math.max(0, xStart);
                xEnd = Math.min(511, xEnd);

                for (int x = xStart; x <= xEnd; x++) {
                    fb[0][y][x] = (int)(c.getR() * 255);
                    fb[1][y][x] = (int)(c.getG() * 255);
                    fb[2][y][x] = (int)(c.getB() * 255);
                }
            }
        }
    }

    private static void intersect(int y, int x0, int y0, int x1, int y1, ArrayList<Integer> list) {

        if (y0 == y1) return;

        if ((y >= Math.min(y0, y1)) && (y < Math.max(y0, y1))) {

            int x = x0 + (y - y0) * (x1 - x0) / (y1 - y0);
            list.add(x);
        }
    }
}