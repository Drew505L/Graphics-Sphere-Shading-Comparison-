package HW;

public class Vector extends VectorAbstract {

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = new Color(1,1,1);
        this.normal = null;
    }

    public Vector(double x, double y, double z, ColorAbstract c) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = (Color) c;
        this.normal = null;
    }
    @Override
    public double angleBetween(VectorAbstract v2) {
        double denominator = length() * v2.length();
        if (denominator == 0) {
            return 0;
        }
        double cosTheta = dot(v2) / denominator;
        return Math.acos(cosTheta);
    }
    @Override
    public double dot(VectorAbstract v2) {
        return x * v2.x + y * v2.y + z * v2.z;
    }

    @Override
    public VectorAbstract cross(VectorAbstract v2) {
        double cx = y * v2.z - z * v2.y;
        double cy = z * v2.x - x * v2.z;
        double cz = x * v2.y - y * v2.x;

        Vector cross = new Vector(cx, cy, cz, this.color);
        return cross.unit();
    }

    @Override
    public VectorAbstract unit() {
        double len = length();
        if (len == 0) {
            return new Vector(0,0,0,this.color);
        }
        return new Vector(x / len, y / len, z / len, this.color);
    }

    @Override
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public VectorAbstract add(VectorAbstract v2) {
        return new Vector(x + v2.x, y + v2.y, z + v2.z, this.color);
    }

    @Override
    public VectorAbstract sub(VectorAbstract v2) {
        return new Vector(x - v2.x, y - v2.y, z - v2.z, this.color);
    }

    @Override
    public VectorAbstract mult(double scale) {
        return new Vector(x * scale, y * scale, z * scale, this.color);
    }
}
