package rubik;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.LinkedList;
import java.util.List;

public class SpotSphere {
	
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
	
	public void render(Matrix4f viewProjection, float aspect) {
		for (Spot spot : spots) {
			spot.render(viewProjection, aspect);
		}
	}
}
