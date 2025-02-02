package rubik;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lib.StdDraw;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class SpotSphere {
	
	class Spot {
		private Vector4f pos;
		private float size;

		public Spot(Vector4f pos, float size) {
			this.pos = pos;
			this.size = size;
		}

		public void render(Matrix4f viewProjection) {
			Vector4f p = viewProjection.transform(pos, new Vector4f());

			if (p.z < 0) {
				return;
			}
			p.div(p.w);
			StdDraw.circle(p.x, p.y, size);
		}
	}

	private List<Spot> spots;
	
	public SpotSphere(float radius, int count, float spotSize) {
		spots = new LinkedList<>();
		
		for (int i = 0; i < count; ++i) {
			double theta = (Math.random() * Math.PI * 2);
			double phi = Math.asin(Math.random() * 2 - 1);
			float x = (float) (Math.sin(theta) * Math.cos(phi));
			float y = (float) Math.sin(phi);
			float z = (float) (Math.cos(theta) * Math.cos(phi));
			spots.add(new Spot(new Vector4f(x*radius, y*radius, z*radius, 1), spotSize));
		}
	}
	
	public void render(Matrix4f viewProjection, Vector3f camViewDir) {
		for (Spot spot : spots) {
			float cosViewAngle = new Vector3f(spot.pos.x, spot.pos.y, spot.pos.z).dot(camViewDir);
			if (cosViewAngle > 0) {
				spot.render(viewProjection);
			}
		}
	}
}
