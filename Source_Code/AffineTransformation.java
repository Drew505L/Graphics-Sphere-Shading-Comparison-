package HW;

public class AffineTransformation extends AffineTransformationAbstract {

    @Override
    public MatrixAbstract translate(VectorAbstract transvec, MatrixAbstract data) {

        double tx = transvec.getX();
        double ty = transvec.getY();
        double tz = transvec.getZ();

        double[][] T = {{1,0,0,tx}, {0,1,0,ty}, {0,0,1,tz}, {0,0,0,1}};

        Matrix transform = new Matrix(T);

        return transform.mult(data);
    }

    @Override
    public MatrixAbstract rotateX(double theta, VectorAbstract fixedpoint, MatrixAbstract data) {

        double c = Math.cos(theta);
        double s = Math.sin(theta);

        double[][] R = {{1,0,0,0}, {0,c,-s,0}, {0,s,c,0}, {0,0,0,1}};

        Matrix rot = new Matrix(R);

        data = translate(fixedpoint.mult(-1), data);
        data = rot.mult(data);
        data = translate(fixedpoint, data);

        return data;
    }

    @Override
    public MatrixAbstract rotateY(double theta, VectorAbstract fixedpoint, MatrixAbstract data) {

        double c = Math.cos(theta);
        double s = Math.sin(theta);

        double[][] R = {{c,0,s,0}, {0,1,0,0}, {-s,0,c,0}, {0,0,0,1}};

        Matrix rot = new Matrix(R);

        data = translate(fixedpoint.mult(-1), data);
        data = rot.mult(data);
        data = translate(fixedpoint, data);

        return data;
    }

    @Override
    public MatrixAbstract rotateZ(double theta, VectorAbstract fixedpoint, MatrixAbstract data) {

        double c = Math.cos(theta);
        double s = Math.sin(theta);

        double[][] R = {{c,-s,0,0}, {s,c,0,0}, {0,0,1,0}, {0,0,0,1}};

        Matrix rot = new Matrix(R);

        data = translate(fixedpoint.mult(-1), data);
        data = rot.mult(data);
        data = translate(fixedpoint, data);

        return data;
    }

    @Override
    public MatrixAbstract scale(VectorAbstract factor, VectorAbstract fixedpoint, MatrixAbstract data) {

        double sx = factor.getX();
        double sy = factor.getY();
        double sz = factor.getZ();

        double[][] S = {{sx,0,0,0}, {0,sy,0,0}, {0,0,sz,0}, {0,0,0,1}};

        Matrix scale = new Matrix(S);

        data = translate(fixedpoint.mult(-1), data);
        data = scale.mult(data);
        data = translate(fixedpoint, data);

        return data;
    }

    @Override
    public MatrixAbstract rotateAxis(VectorAbstract axis, VectorAbstract fixedpoint, double arads, MatrixAbstract data) {

        double x = axis.getX();
        double y = axis.getY();
        double z = axis.getZ();

        double mag = Math.sqrt(x*x + y*y + z*z);

        x /= mag;
        y /= mag;
        z /= mag;

        double c = Math.cos(arads);
        double s = Math.sin(arads);
        double t = 1 - c;

        double[][] R = {{t*x*x + c, t*x*y - s*z, t*x*z + s*y, 0}, {t*x*y + s*z, t*y*y + c, t*y*z - s*x, 0}, {t*x*z - s*y, t*y*z + s*x, t*z*z + c, 0}, {0,0,0,1}};

        Matrix rot = new Matrix(R);

        data = translate(fixedpoint.mult(-1), data);
        data = rot.mult(data);
        data = translate(fixedpoint, data);

        return data;
    }
}