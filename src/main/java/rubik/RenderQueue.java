package rubik;

import lib.StdDraw;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * A class that can queue polygons and render them in back-to-front order sorted by their center distance to the camera.
 */
public class RenderQueue {

	class TriangleFan {
		public Vector4f[] verts;
		public Vector3f[] screenVerts;
		public Color fill;
		public Color stroke;

		public Vector2f screenMin;
		public Vector2f screenMax;
		public float depthMin;
		public float depthMean;
		
		public TriangleFan(Vector4f[] verts, Color fill, Color stroke) {
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
			screenMin = new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
			screenMax = new Vector2f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
			screenVerts = new Vector3f[verts.length];

			for (int i = 0; i < verts.length; i++) {
				Vector4f vert = new Vector4f(verts[i]);
				float depth = camPos.distance(vert);
				Vector4f screenVert = camViewProjection.transform(vert);
				screenVert.div(screenVert.w);
				screenVerts[i] = new Vector3f(screenVert.x, screenVert.y, depth);
				screenMin.x = Math.min(screenMin.x, screenVert.x);
				screenMin.y = Math.min(screenMin.y, screenVert.y);
				screenMax.x = Math.max(screenMax.x, screenVert.x);
				screenMax.y = Math.max(screenMax.y, screenVert.y);
				depthMin = Math.min(depthMin, depth);
				depthMean += depth;
			}
			depthMean /= verts.length;
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

		public boolean boundsOverlap(TriangleFan other) {
			return screenMin.x < other.screenMax.x && screenMax.x > other.screenMin.x &&
					screenMin.y < other.screenMax.y && screenMax.y > other.screenMin.y;
		}

		public boolean intersects(TriangleFan other) {
			for (int i = 0; i < verts.length; i++) {
				if (other.pointInPoly(screenVerts[i]) != 0) {
					return true;
				}
			}
			for (int i = 0; i < other.verts.length; i++) {
				if (pointInPoly(other.screenVerts[i]) != 0) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Do some whacky point in triangle check to determine if a poly has a vertex inside the other poly, closer to the camera.
		 */
		public boolean isCloserThan(TriangleFan other) {
			for (int i = 0; i < verts.length; i++) {
				float delta = other.pointInPoly(screenVerts[i]);
				if (delta != 0) {
					return delta < 0;
				}
			}
			for (int i = 0; i < other.verts.length; i++) {
				float delta = pointInPoly(other.screenVerts[i]);
				if (delta != 0) {
					return delta > 0;
				}
			}
			throw new IllegalStateException("Polygons do not intersect");
		}
		
		private float pointInPoly(Vector3f point) {
			for (int i = 1; i < verts.length - 1; i++) {
				Vector3f bary = barycentric(
						new Vector2f(point.x, point.y),
						new Vector2f(screenVerts[0].x, screenVerts[0].y),
						new Vector2f(screenVerts[i].x, screenVerts[i].y),
						new Vector2f(screenVerts[i + 1].x, screenVerts[i + 1].y));
				if (bary.x >= 0 && bary.y >= 0 && bary.z >= 0 && bary.x <= 1 && bary.y <= 1 && bary.z <= 1) {
					float depth = bary.x * screenVerts[0].z + bary.y * screenVerts[i].z + bary.z * screenVerts[i + 1].z;
					return point.z - depth;
				}
			}
			return 0;
		}
		
		//still magic to me
		private Vector3f barycentric(Vector2f p, Vector2f a, Vector2f b, Vector2f c) {
			Vector2f v0 = new Vector2f(b).sub(a);
			Vector2f v1 = new Vector2f(c).sub(a);
			Vector2f v2 = new Vector2f(p).sub(a);
			float d00 = v0.dot(v0);
			float d01 = v0.dot(v1);
			float d11 = v1.dot(v1);
			float d20 = v2.dot(v0);
			float d21 = v2.dot(v1);
			float denom = 1f / (d00 * d11 - d01 * d01);
			float v = (d11 * d20 - d01 * d21) * denom;
			float w = (d00 * d21 - d01 * d20) * denom;
			float u = 1 - v - w;
			return new Vector3f(u, v, w);
		}
	}
	private List<TriangleFan> queue;
	
	public RenderQueue() {
		queue = new ArrayList<>();
	}

	/**
	 * Renders all polygons in the queue in back-to-front order sorted by their center distance to the camera.
	 */
	public void render(Vector4f camPos, Matrix4f camViewProjection) {
		for (TriangleFan poly : queue) {
			poly.projectVerts(camViewProjection, camPos);
		}
		try {
			queue.sort((a, b) -> {
				if (!a.boundsOverlap(b) || !a.intersects(b)) {
					return a.depthMean < b.depthMean ? 1 : -1;
				}
				return a.isCloserThan(b) ? 1 : -1;
			});
		} catch (IllegalArgumentException ignore) {
			System.out.println("break");
		}
		
		for (TriangleFan p : queue) {
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
		queue.add(new TriangleFan(verts, fill, stroke));
	}
}
