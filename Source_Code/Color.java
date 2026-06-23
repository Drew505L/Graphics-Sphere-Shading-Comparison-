package HW;

public class Color extends ColorAbstract {

    // Constructor
    public Color(double r, double g, double b) throws IllegalArgumentException {
        setR(r);
        setG(g);
        setB(b);
    }

    // Scale color components to integer range
    @Override
    public int[] scale(int s) {
        int R = (int)(r * s);
        int G = (int)(g * s);
        int B = (int)(b * s);
        return new int[]{R, G, B};
    }
}