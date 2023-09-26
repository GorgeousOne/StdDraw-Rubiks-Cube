import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Vector3f pos;
	private Vector3f viewDir;
	private float fov; // Field of View (in degrees)

	public Camera(Vector3f pos, Vector3f viewDirection, float fov) {
		this.pos = pos;
		this.viewDir = viewDirection;
		this.fov = fov;
	}

	public Matrix4f getView() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.lookAt(pos, pos.add(viewDir, new Vector3f()), new Vector3f(0, 1, 0));
		return viewMatrix;
	}

	public Matrix4f getProjection(float aspectRatio) {
		Matrix4f projectionMatrix = new Matrix4f();
		return projectionMatrix.perspective((float) Math.toRadians(fov), aspectRatio, 0.1f, 100f);
	}

	// Getter and Setter methods for position, view direction, and fov

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f newPos) {
		this.pos = newPos;
	}

	public Vector3f getViewDir() {
		return viewDir;
	}

	public void setViewDir(Vector3f newViewDir) {
		this.viewDir = newViewDir;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}
}
