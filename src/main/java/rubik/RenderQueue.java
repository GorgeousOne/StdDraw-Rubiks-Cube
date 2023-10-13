package rubik;

import lib.StdDraw;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.List;
import java.util.LinkedList;

public class RenderQueue {

	class Polygon {
		public double[] xs;
		public double[] ys;
		public Vector3f center;
		public Color fill;
		public Color stroke;

		public Polygon(double[] xs, double ys[], Vector3f center, Color fill, Color stroke) {
			this.xs = xs;
			this.ys = ys;
			this.center = center;
			this.fill = fill;
			this.stroke = stroke;
		}
	}
	private List<Polygon> queue;
	
	public RenderQueue() {
		queue = new LinkedList<>();
	}

	public void render(Vector3f camPos) {
		// draw all polygons by distance to camera, back to front
		queue.sort((a, b) -> {
			float camDist1 = camPos.distanceSquared(a.center);
			float camDist2 = camPos.distanceSquared(b.center);
			return Float.compare(camDist2, camDist1);
		});
		for (Polygon p : queue) {
			if (p.fill != null) {
				StdDraw.setPenColor(p.fill);
				StdDraw.filledPolygon(p.xs, p.ys);
			}
			if (p.stroke != null) {
				StdDraw.setPenColor(p.stroke);
				StdDraw.polygon(p.xs, p.ys);
			}
		}
		queue.clear();
	}

	public void queue(double[] xs, double[] ys, Vector3f center, Color fill, Color stroke) {
		queue.add(new Polygon(xs, ys, center, fill, stroke));
	}
}
