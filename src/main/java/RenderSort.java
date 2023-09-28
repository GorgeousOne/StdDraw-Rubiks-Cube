import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Comparator;

public class RenderSort implements Comparator<Cube> {

	private Vector3f camPos;

	public void setCamPos(Vector3f camPos) {
		this.camPos = camPos;
	}

	public int compare(Cube a1, Cube a2) {
		float camDist1 = camPos.distance(a1.getCenter());
		float camDist2 = camPos.distance(a2.getCenter());
		return Float.compare(camDist2, camDist1);
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}
}
