package HW;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SceneObject extends SceneObjectAbstract {

    private ArrayList<TriangleAbstract> baseTriangles;
    private VectorAbstract modelCenter;

    public SceneObject(String stlfilename) {
        try {
            List<Triangle> parsed = STLParser.parseSTLFile(Paths.get(stlfilename));

            triangles = new ArrayList<>();
            baseTriangles = new ArrayList<>();
            modelCenter = computeCenter(parsed);

            Color faceColor = new Color(0, 0, 1);

            for (int i = 0; i < parsed.size(); i++) {

                Triangle t = parsed.get(i);

                for (VectorAbstract v : t.getVertices()) {
                    v.setColor(faceColor);
                }

                triangles.add(cloneTriangle(t));
                baseTriangles.add(cloneTriangle(t));
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rotate(VectorAbstract axis, double theta) {

        ArrayList<TriangleAbstract> newTris = new ArrayList<>();

        VectorAbstract center = getCenter();
        double radians = Math.toRadians(theta);

        for (TriangleAbstract t : triangles) {
            newTris.add(t.rotateAxis(axis, center, radians, t));
        }

        triangles = newTris;
    }

    @Override
    public void translate(VectorAbstract trans) {
        ArrayList<TriangleAbstract> newTris = new ArrayList<>();

        for (TriangleAbstract t : triangles) {
            newTris.add(t.translate(trans, t));
        }

        triangles = newTris;
    }

    @Override
    public void scale(VectorAbstract scale) {
        VectorAbstract center = getCenter();

        ArrayList<TriangleAbstract> newTris = new ArrayList<>();

        for (TriangleAbstract t : triangles) {
            newTris.add(t.scale(scale, center, t));
        }

        triangles = newTris;
    }

    @Override
    public void render(int[][][] framebuffer, double[][] zbuffer, boolean shownormals, ShaderAbstract.FILLSTYLE fill, VectorAbstract viewpoint, Light light) {

        Shader shader = new Shader();

        VectorAbstract objectCenter = getCenter();

        for (TriangleAbstract t : triangles) {

            for (VectorAbstract v : t.getVertices()) {

                VectorAbstract normal = v.sub(objectCenter).unit();

                v.setNormal(normal);

            }
        }

        VectorAbstract center = getCenter();

        for (TriangleAbstract t : triangles) {

            VectorAbstract[] verts = t.getVertices();

            VectorAbstract p0 = project(verts[0], center);

            VectorAbstract p1 = project(verts[1], center);

            VectorAbstract p2 = project(verts[2], center);

            Triangle projected = new Triangle(p0, p1, p2);

            if (fill == ShaderAbstract.FILLSTYLE.FLAT) {
                shader.solidFill(projected, framebuffer, zbuffer, light);
            }
            else if (fill == ShaderAbstract.FILLSTYLE.GOURAUD) {
                shader.shadeFill(projected, framebuffer, zbuffer, light);
            }
            else if (fill == ShaderAbstract.FILLSTYLE.PHONG) {
                shader.phongFill(projected, framebuffer, zbuffer, light);
            }
            else {
                projected.render(framebuffer, shownormals, fill, viewpoint);
            }
        }
    }

    @Override
    public VectorAbstract getCenter() {
        double x = 0, y = 0, z = 0;
        int count = 0;

        for (TriangleAbstract t : triangles) {
            for (VectorAbstract v : t.getVertices()) {
                x += v.getX();
                y += v.getY();
                z += v.getZ();
                count++;
            }
        }

        return new Vector(x / count, y / count, z / count);
    }

    @Override
    public VectorAbstract[] getExtents() {

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;

        for (TriangleAbstract t : triangles) {
            for (VectorAbstract v : t.getVertices()) {
                minX = Math.min(minX, v.getX());
                minY = Math.min(minY, v.getY());
                minZ = Math.min(minZ, v.getZ());

                maxX = Math.max(maxX, v.getX());
                maxY = Math.max(maxY, v.getY());
                maxZ = Math.max(maxZ, v.getZ());
            }
        }

        return new Vector[]{new Vector(minX, minY, minZ), new Vector(maxX, maxY, maxZ)};
    }

    @Override
    public void reset() {
        triangles = new ArrayList<>(baseTriangles);
    }

    private TriangleAbstract cloneTriangle(TriangleAbstract t) {

        VectorAbstract[] v = t.getVertices();

        Vector v0 = new Vector(v[0].getX(), v[0].getY(), v[0].getZ(), v[0].getColor());

        Vector v1 = new Vector(v[1].getX(), v[1].getY(), v[1].getZ(), v[1].getColor());

        Vector v2 = new Vector(v[2].getX(), v[2].getY(), v[2].getZ(), v[2].getColor());

        return new Triangle(v0, v1, v2);
    }

    private VectorAbstract computeCenter(List<Triangle> tris) {
        double x = 0, y = 0, z = 0;
        int count = 0;

        for (TriangleAbstract t : tris) {
            for (VectorAbstract v : t.getVertices()) {
                x += v.getX();
                y += v.getY();
                z += v.getZ();
                count++;
            }
        }

        return new Vector(x / count, y / count, z / count);
    }

    private VectorAbstract project(VectorAbstract v, VectorAbstract center) {

        double cameraDistance = 800.0;
        double scale = cameraDistance / (cameraDistance - v.getZ());

        scale = Math.max(0.1, Math.min(scale, 5.0));

        double projectedX = center.getX() + (v.getX() - center.getX()) * scale;
        double projectedY = center.getY() + (v.getY() - center.getY()) * scale;

        Vector projected = new Vector(projectedX, projectedY, v.getZ(), v.getColor());

        projected.setNormal(v.getNormal());

        return projected;
    }

}