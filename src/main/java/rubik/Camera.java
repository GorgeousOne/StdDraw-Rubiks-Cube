package rubik;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Math;

/**
 * A class that represents a camera orbiting a target point in 3D space with a yaw and pitch angle.
 * It can be used to generate view and projection transform matrices.
 */
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

	/**
	 * Rotates the camera based on the input delta values.
	 */
	public void move(float deltaYaw, float deltaPitch) {
		// Update yaw, pitch, and distance based on input deltas
		yaw += deltaYaw;
		pitch += deltaPitch;
		// Ensure pitch stays within [-90, 90] degrees
		pitch = Math.min(89.9f, Math.max(-89.9f, pitch));
	}
	
	/**
	 * Returns the projection transform matrix to transform points from camera space to clip space.
	 */
	public Matrix4f getViewProjection(float aspectRatio) {
		Matrix4f viewTransform = new Matrix4f().lookAt(getPos(), target, UP);
		Matrix4f projectTransform = new Matrix4f().perspective(Math.toRadians(fov), aspectRatio, 0.1f, 100f);
		return projectTransform.mul(viewTransform);
	}

	public Vector3f getPos() {
		return new Vector3f(target).add(getViewDir().mul(-distance));
	}

	/**
	 * Returns the forward vector of the camera.
	 */
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
