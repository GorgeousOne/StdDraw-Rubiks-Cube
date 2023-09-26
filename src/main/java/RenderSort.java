import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Comparator;

public class RenderSort implements Comparator<AABB> {

	private Vector3f camPos;

	public void setCamPos(Vector3f camPos) {
		this.camPos = camPos;
	}

	public int compare(AABB a1, AABB a2) {
		float distance1 = calculateDistanceFromCamera(a1, camPos);
		float distance2 = calculateDistanceFromCamera(a2, camPos);
		return Float.compare(distance2, distance1); // Reverse order (further away first)
	}

	private float calculateDistanceFromCamera(AABB aabb, Vector3f cameraPos) {
		// Calculate the center of the AABB in world space
		Vector3f center = aabb.getCenter(new Matrix4f().identity());
		return cameraPos.distance(center);
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}
}
