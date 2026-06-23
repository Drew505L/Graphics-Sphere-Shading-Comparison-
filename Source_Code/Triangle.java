package HW;

public class Triangle extends TriangleAbstract {

    public Triangle(VectorAbstract v1, VectorAbstract v2, VectorAbstract v3) {
        vertices = new VectorAbstract[3];
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
    }

    @Override
    public VectorAbstract getCenter() {
        VectorAbstract sum = vertices[0].add(vertices[1]).add(vertices[2]);
        return sum.mult(1.0 / 3.0);
    }

    @Override
    public double getPerimeter() {
        double a = vertices[1].sub(vertices[0]).length();
        double b = vertices[2].sub(vertices[1]).length();
        double c = vertices[0].sub(vertices[2]).length();
        return a + b + c;
    }

    @Override
    public double getArea() {
        VectorAbstract ab = vertices[1].sub(vertices[0]);
        VectorAbstract ac = vertices[2].sub(vertices[0]);
        return 0.5 * ab.cross(ac).length();
    }

    @Override
    public VectorAbstract getNormal() {
        VectorAbstract ab = vertices[1].sub(vertices[0]);
        VectorAbstract ac = vertices[2].sub(vertices[0]);
        return ab.cross(ac);
    }

    @Override
    public void render(int[][][] framebuffer, boolean shownormal, ShaderAbstract.FILLSTYLE fill, VectorAbstract viewpoint) {

        ScanConvertLine line = new ScanConvertLine();

        int x0 = (int) vertices[0].getX();
        int y0 = (int) vertices[0].getY();

        int x1 = (int) vertices[1].getX();
        int y1 = (int) vertices[1].getY();

        int x2 = (int) vertices[2].getX();
        int y2 = (int) vertices[2].getY();

        ColorAbstract c0 = vertices[0].getColor();
        ColorAbstract c1 = vertices[1].getColor();
        ColorAbstract c2 = vertices[2].getColor();

        line.bresenham(x0, y0, x1, y1, c0, c1, framebuffer);
        line.bresenham(x1, y1, x2, y2, c1, c2, framebuffer);
        line.bresenham(x2, y2, x0, y0, c2, c0, framebuffer);

        if (shownormal) {

            VectorAbstract center = getCenter();

            VectorAbstract n = getNormal().unit();

            VectorAbstract normal2D = new Vector(n.getX(), n.getY(), 0);

            if (normal2D.length() == 0) {
                return;
            }

            normal2D = normal2D.unit().mult(16);

            VectorAbstract end = center.add(normal2D);

            line.bresenham(
                    (int) center.getX(),
                    (int) center.getY(),
                    (int) end.getX(),
                    (int) end.getY(),
                    new Color(1,1,1),
                    new Color(1,1,1),
                    framebuffer
            );
        }
    }

    private Matrix toMatrix(TriangleAbstract t) {

        double[][] m = new double[4][3];

        for(int i = 0; i < 3; i++) {
            VectorAbstract v = t.getVertices()[i];

            m[0][i] = v.getX();
            m[1][i] = v.getY();
            m[2][i] = v.getZ();
            m[3][i] = 1;
        }

        return new Matrix(m);
    }

    private Triangle fromMatrix(MatrixAbstract mat) {

        double[][] m = mat.getMatrix();

        VectorAbstract v0 = new Vector(
                m[0][0], m[1][0], m[2][0],
                vertices[0].getColor()
        );

        VectorAbstract v1 = new Vector(
                m[0][1], m[1][1], m[2][1],
                vertices[1].getColor()
        );

        VectorAbstract v2 = new Vector(
                m[0][2], m[1][2], m[2][2],
                vertices[2].getColor()
        );

        return new Triangle(v0, v1, v2);
    }

    @Override
    public TriangleAbstract rotateX(double theta, VectorAbstract fixedpoint, TriangleAbstract data) {

        AffineTransformation at = new AffineTransformation();
        Matrix m = toMatrix(data);

        MatrixAbstract result = at.rotateX(theta, fixedpoint, m);

        return fromMatrix(result);
    }

    @Override
    public TriangleAbstract rotateY(double theta, VectorAbstract fixedpoint, TriangleAbstract data) {

        AffineTransformation at = new AffineTransformation();
        Matrix m = toMatrix(data);

        MatrixAbstract result = at.rotateY(theta, fixedpoint, m);

        return fromMatrix(result);
    }

    @Override
    public TriangleAbstract rotateZ(double theta, VectorAbstract fixedpoint, TriangleAbstract data) {

        AffineTransformation at = new AffineTransformation();
        Matrix m = toMatrix(data);

        MatrixAbstract result = at.rotateZ(theta, fixedpoint, m);

        return fromMatrix(result);
    }

    @Override
    public TriangleAbstract rotateAxis(VectorAbstract axis, VectorAbstract fixedpoint, double arads, TriangleAbstract data) {

        AffineTransformation at = new AffineTransformation();
        Matrix m = toMatrix(data);

        MatrixAbstract result = at.rotateAxis(axis, fixedpoint, arads, m);

        return fromMatrix(result);
    }

    @Override
    public TriangleAbstract translate(VectorAbstract transvec, TriangleAbstract data) {

        AffineTransformation at = new AffineTransformation();
        Matrix m = toMatrix(data);

        MatrixAbstract result = at.translate(transvec, m);

        return fromMatrix(result);
    }

    @Override
    public TriangleAbstract scale(VectorAbstract factor, VectorAbstract fixedpoint, TriangleAbstract data) {

        AffineTransformation at = new AffineTransformation();
        Matrix m = toMatrix(data);

        MatrixAbstract result = at.scale(factor, fixedpoint, m);

        return fromMatrix(result);
    }

    @Override
    public boolean isVisible(VectorAbstract viewpoint) {

        VectorAbstract normal = getNormal().unit();

        // Vector from triangle to viewer
        VectorAbstract viewVec = viewpoint.sub(getCenter()).unit();

        return normal.dot(viewVec) > 0;
    }
}