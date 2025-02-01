package rubik;

import lib.StdDraw;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * A class that can queue polygons and render them in back-to-front order sorted by their center distance to the camera.
 */
public class RenderQueue {

	class FlatPoly {
		public Vector4f[] verts;
		public Vector3f[] screenVerts;
		public Color fill;
		public Color stroke;

		public FlatPoly(Vector4f[] verts, Color fill, Color stroke) {
			this.verts = verts;
			this.fill = fill;
			this.stroke = stroke;
		}

		/**
		 * Calculates the vertices of the poly projected onto the screen, uses z as depth.
		 * @param camViewProjection
		 * @param aspect
		 */
		public void projectVerts(Matrix4f camViewProjection, Vector4f camPos) {
			screenVerts = new Vector3f[verts.length];
			for (int i = 0; i < verts.length; i++) {
				Vector4f vert = new Vector4f(verts[i]);
				float depth = camPos.distance(vert);
				Vector4f screenVert = camViewProjection.transform(vert);
				screenVert.div(screenVert.w);
				screenVerts[i] = new Vector3f(screenVert.x, screenVert.y, depth);
			}
		}
		
		public double[] getXs() {
			double[] xs = new double[screenVerts.length];
			for (int i = 0; i < screenVerts.length; i++) {
				//convert from JOML [-1, 1] to StdDraw [0, 1] screen space
				xs[i] = screenVerts[i].x * 0.5 + 0.5;
			}
			return xs;
		}

		public double[] getYs() {
			double[] ys = new double[screenVerts.length];
			for (int i = 0; i < screenVerts.length; i++) {
				ys[i] = screenVerts[i].y * 0.5 + 0.5;
			}
			return ys;
		}
	}
	private List<FlatPoly> queue;
	
	public RenderQueue() {
		queue = new ArrayList<>();
	}

	/**
	 * Renders all polygons in the queue in back-to-front order sorted by their center distance to the camera.
	 */
	public void render(Vector4f camPos, Matrix4f camViewProjection) {
		for (FlatPoly poly : queue) {
			poly.projectVerts(camViewProjection, camPos);
		}
		// draw all polygons by distance to camera, back to front
		// queue.sort((a, b) -> {

		// });

		for (FlatPoly p : queue) {
			if (p.fill != null) {
				StdDraw.setPenColor(p.fill);
				StdDraw.filledPolygon(p.getXs(), p.getYs());
			}
			if (p.stroke != null) {
				StdDraw.setPenColor(p.stroke);
				StdDraw.polygon(p.getXs(), p.getYs());
			}
		}
		queue.clear();
	}

	/**
	 * I trust you to only queue convex polys with vertices in one plane.
	 */
	public void queueFlatPoly(Vector4f[] verts, Color fill, Color stroke) {
		queue.add(new FlatPoly(verts, fill, stroke));
	}
}
