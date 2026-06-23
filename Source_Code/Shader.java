package HW;

public class Shader extends ShaderAbstract {

    @Override
    public void solidFill(TriangleAbstract tri, int[][][] framebuffer, double[][] zbuffer, Light light) {

        VectorAbstract[] v = tri.getVertices();
        VectorAbstract normal = tri.getNormal().unit();
        VectorAbstract center = tri.getCenter();

        double intensity = computeIntensity(normal, center, light);

        int x0 = (int) v[0].getX();
        int y0 = (int) v[0].getY();

        int x1 = (int) v[1].getX();
        int y1 = (int) v[1].getY();

        int x2 = (int) v[2].getX();
        int y2 = (int) v[2].getY();

        Color c = (Color) v[0].getColor();

        int r = (int)(c.getR() * intensity * 255);
        int g = (int)(c.getG() * intensity * 255);
        int b = (int)(c.getB() * intensity * 255);

        int minX = Math.max(0, Math.min(x0, Math.min(x1, x2)));
        int maxX = Math.min(framebuffer[0][0].length - 1, Math.max(x0, Math.max(x1, x2)));
        int minY = Math.max(0, Math.min(y0, Math.min(y1, y2)));
        int maxY = Math.min(framebuffer[0].length - 1, Math.max(y0, Math.max(y1, y2)));

        // Precompute denominator (for barycentric)
        double denom = ((y1 - y2)*(x0 - x2) + (x2 - x1)*(y0 - y2));

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {

                if (insideTriangle(x, y, x0, y0, x1, y1, x2, y2)) {

                    // Barycentric coordinates
                    double w1 = ((y1 - y2)*(x - x2) + (x2 - x1)*(y - y2)) / denom;

                    double w2 = ((y2 - y0)*(x - x2) + (x0 - x2)*(y - y2)) / denom;

                    double w3 = 1 - w1 - w2;

                    // Interpolate z
                    double z = w1 * v[0].getZ() + w2 * v[1].getZ() + w3 * v[2].getZ();

                    // Depth test
                    if (z < zbuffer[y][x]) {

                        zbuffer[y][x] = z;

                        framebuffer[0][y][x] = r;
                        framebuffer[1][y][x] = g;
                        framebuffer[2][y][x] = b;
                    }
                }
            }
        }
    }

    @Override
    public void shadeFill(TriangleAbstract tri, int[][][] framebuffer, double[][] zbuffer, Light light) {

        VectorAbstract[] v = tri.getVertices();

        int x0 = (int) v[0].getX();
        int y0 = (int) v[0].getY();

        int x1 = (int) v[1].getX();
        int y1 = (int) v[1].getY();

        int x2 = (int) v[2].getX();
        int y2 = (int) v[2].getY();

        // Step 1: Declare vertex intensities
        double i0 = computeIntensity(v[0].getNormal(), v[0], light);
        double i1 = computeIntensity(v[1].getNormal(), v[1], light);
        double i2 = computeIntensity(v[2].getNormal(), v[2], light);

        Color c = (Color) v[0].getColor();

        int minX = Math.max(0, Math.min(x0, Math.min(x1, x2)));
        int maxX = Math.min(framebuffer[0][0].length - 1, Math.max(x0, Math.max(x1, x2)));
        int minY = Math.max(0, Math.min(y0, Math.min(y1, y2)));
        int maxY = Math.min(framebuffer[0].length - 1, Math.max(y0, Math.max(y1, y2)));

        // Precompute denominator (for barycentric)
        double denom = ((y1 - y2)*(x0 - x2) + (x2 - x1)*(y0 - y2));

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {

                if (insideTriangle(x, y, x0, y0, x1, y1, x2, y2)) {

                    // Step 2: Barycentric weights
                    double w1 = ((y1 - y2)*(x - x2) + (x2 - x1)*(y - y2)) / denom;
                    double w2 = ((y2 - y0)*(x - x2) + (x0 - x2)*(y - y2)) / denom;
                    double w3 = 1 - w1 - w2;

                    double z = w1 * v[0].getZ() + w2 * v[1].getZ() + w3 * v[2].getZ();

                    // Step 3: Interpolate intensity
                    double intensity = w1 * i0 + w2 * i1 + w3 * i2;

                    // Clamp
                    intensity = Math.max(0, Math.min(1, intensity));

                    // Step 4: Apply color
                    if (z < zbuffer[y][x]) {

                        zbuffer[y][x] = z;

                        framebuffer[0][y][x] = (int)(c.getR() * intensity * 255);
                        framebuffer[1][y][x] = (int)(c.getG() * intensity * 255);
                        framebuffer[2][y][x] = (int)(c.getB() * intensity * 255);
                    }
                }
            }
        }
    }

    @Override
    public void phongFill(TriangleAbstract tri, int[][][] framebuffer, double[][] zbuffer, Light light) {

        VectorAbstract[] v = tri.getVertices();

        int x0 = (int) v[0].getX();
        int y0 = (int) v[0].getY();

        int x1 = (int) v[1].getX();
        int y1 = (int) v[1].getY();

        int x2 = (int) v[2].getX();
        int y2 = (int) v[2].getY();

        Color c = (Color) v[0].getColor();

        int minX = Math.max(0, Math.min(x0, Math.min(x1, x2)));
        int maxX = Math.min(framebuffer[0][0].length - 1, Math.max(x0, Math.max(x1, x2)));

        int minY = Math.max(0, Math.min(y0, Math.min(y1, y2)));
        int maxY = Math.min(framebuffer[0].length - 1, Math.max(y0, Math.max(y1, y2)));

        double denom = ((y1 - y2)*(x0 - x2) + (x2 - x1)*(y0 - y2));

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {

                if (insideTriangle(x, y, x0, y0, x1, y1, x2, y2)) {

                    // Barycentric coordinates
                    double w1 = ((y1 - y2)*(x - x2) + (x2 - x1)*(y - y2)) / denom;
                    double w2 = ((y2 - y0)*(x - x2) + (x0 - x2)*(y - y2)) / denom;
                    double w3 = 1 - w1 - w2;

                    double z = w1 * v[0].getZ() + w2 * v[1].getZ() + w3 * v[2].getZ();

                    // Interpolate normals
                    VectorAbstract normal = v[0].getNormal().mult(w1).add(v[1].getNormal().mult(w2)).add(v[2].getNormal().mult(w3)).unit();

                    // Approximate pixel position
                    VectorAbstract point = v[0].mult(w1).add(v[1].mult(w2)).add(v[2].mult(w3));

                    // Determines lighting per pixel
                    double intensity = computeIntensity(normal, point, light);

                    intensity = Math.max(0, Math.min(1, intensity));

                    if (z < zbuffer[y][x]) {

                        zbuffer[y][x] = z;

                        framebuffer[0][y][x] = (int)(c.getR() * intensity * 255);
                        framebuffer[1][y][x] = (int)(c.getG() * intensity * 255);
                        framebuffer[2][y][x] = (int)(c.getB() * intensity * 255);
                    }
                }
            }
        }
    }

    private boolean insideTriangle(int px, int py, int x0, int y0, int x1, int y1, int x2, int y2) {

        int d1 = sign(px, py, x0, y0, x1, y1);
        int d2 = sign(px, py, x1, y1, x2, y2);
        int d3 = sign(px, py, x2, y2, x0, y0);

        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNeg && hasPos);
    }

    private int sign(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
    }

    private double computeIntensity(VectorAbstract normal, VectorAbstract point, Light light) {

        VectorAbstract N = normal.unit();
        VectorAbstract L = light.position.sub(point).unit();
        VectorAbstract V = new Vector(0,0,1,null).sub(point).unit();

        double ambient = 0.25;

        double diffuse = Math.max(0, N.dot(L));

        // Reflection vector
        VectorAbstract R = N.mult(2 * N.dot(L)).sub(L).unit();

        double specular = Math.pow(Math.max(0, R.dot(V)), 32);
        double intensity = ambient + 0.7 * diffuse + 0.4 * specular;

        return Math.min(1, intensity);
    }
}