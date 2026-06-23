package HW;

public abstract class ShaderAbstract {
		
		public static enum FILLSTYLE {FLAT, GOURAUD, PHONG, NONE};
		
		/**
		 * Render a filled triangle with a solid color. The color is selected from the first vertex listed
		 * for the triangle
		 * @param tri Triangle to be filled/rendered
		 * @param framebuffer Frame buffer to receive rendering
		 */
		public abstract void solidFill(TriangleAbstract tri, int[][][] framebuffer, double[][] zbuffer, Light light);

		

		/**
		 * Render a Gouraud shaded triangle. The colors for the bilinear interpolatio (Gouraud shading) are
		 * taken from the vertices of the triangle.
		 * @param tri Triangle to be filled/rendered
		 * @param framebuffer Frame buffer to receive rendering
		 */
		public abstract void shadeFill(TriangleAbstract tri, int[][][] framebuffer, double[][] zbuffer, Light light);

		public abstract void phongFill(TriangleAbstract tri, int[][][] framebuffer, double[][] zbuffer, Light light);



}
