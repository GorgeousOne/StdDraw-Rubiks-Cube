import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Math;

public class Camera {

	private final static Vector3f UP = new Vector3f(0, 1, 0);
	private Vector3f target; // Target point to orbit around
	private float distance; // Distance from the target point
	private float yaw; // Yaw angle (horizontal rotation)
	private float pitch; // Pitch angle (vertical rotation)
	private float fov; // Field of View (in degrees)

	public Camera(Vector3f target, float distance, float yaw, float pitch, float fov) {
		this.target = target;
		this.distance = distance;
		this.yaw = yaw;
		this.pitch = pitch;
		this.fov = fov;
	}

	public void move(float deltaYaw, float deltaPitch) {
		// Update yaw, pitch, and distance based on input deltas
		yaw += deltaYaw;
		pitch += deltaPitch;
		// Ensure pitch stays within [-90, 90] degrees
		pitch = Math.min(90.0f, Math.max(-90.0f, pitch));
	}

	public Matrix4f getView() {
		// Calculate the camera's position based on yaw, pitch, and distance
		Vector3f position = getPos();

		// Create a view matrix to look at the target from the calculated position
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.lookAt(position, target, UP);

		return viewMatrix;
	}

	public Matrix4f getProjection(float aspectRatio) {
		Matrix4f projectionMatrix = new Matrix4f();
		return projectionMatrix.perspective(Math.toRadians(fov), aspectRatio, 0.1f, 100f);
	}

	public Vector3f getPos() {
		return new Vector3f(target).add(getViewDir().mul(-distance));
	}

	public Vector3f getViewDir() {
		float cosPitch = Math.cos(Math.toRadians(-pitch));
		return new Vector3f(
				Math.cos(Math.toRadians(-yaw)) * cosPitch,
				Math.sin(Math.toRadians(-pitch)),
				Math.sin(Math.toRadians(-yaw)) * cosPitch
		).normalize();
	}
	// Getter and Setter methods for distance, yaw, pitch, and fov

	public float getDistance() {
		return distance;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}
}
