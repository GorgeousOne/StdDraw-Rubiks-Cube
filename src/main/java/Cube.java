import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector3f;

import java.awt.*;

public class Cube {
	private final Vector4f[] vertices; // Array to store the 8 vertices of the AABB
	private final int[][] faces;       // 2D array to store the indices for each face of the cube
	private final Vector4f[] normals;  // Array to store the normals for each face of the cube
	private Matrix4f transform;   // Transformation matrix for the AABB
	private Matrix4f temTransform;
	private final Color[] faceColos;

	public Cube(float size) {
		this.transform = new Matrix4f().identity();
		this.temTransform = new Matrix4f().identity();
		this.vertices = computeVertices(0.5f * size);
		this.faces = computeFaces();
		this.normals = computeNormals();
		this.faceColos = new Color[] {
				StdDraw.DARK_GRAY, StdDraw.DARK_GRAY, StdDraw.DARK_GRAY,
				StdDraw.DARK_GRAY, StdDraw.DARK_GRAY, StdDraw.DARK_GRAY
		};
	}

	private Vector4f[] computeVertices(float size) {
		return new Vector4f[]{
				new Vector4f(-size, -size, -size, 1),
				new Vector4f(size, -size, -size, 1),
				new Vector4f(size, size, -size, 1),
				new Vector4f(-size, size, -size, 1),
				new Vector4f(-size, -size, size, 1),
				new Vector4f(size, -size, size, 1),
				new Vector4f(size, size, size, 1),
				new Vector4f(-size, size, size, 1)
		};
	}

	// Compute and return the indices for each face of the cube
	private int[][] computeFaces() {
		return new int[][]{
				{0, 1, 2, 3}, // Front face
				{4, 5, 6, 7}, // Back face
				{0, 1, 5, 4}, // Bottom face
				{3, 2, 6, 7}, // Top face
				{0, 3, 7, 4}, // Left face
				{1, 2, 6, 5}  // Right face
		};
	}

	// Compute and return the normals for each face of the cube
	private Vector4f[] computeNormals() {
		return new Vector4f[]{
				new Vector4f(0, 0, -1, 0), // Front face normal
				new Vector4f(0, 0, 1, 0),  // Back face normal
				new Vector4f(0, -1, 0, 0), // Bottom face normal
				new Vector4f(0, 1, 0, 0),  // Top face normal
				new Vector4f(-1, 0, 0, 0), // Left face normal
				new Vector4f(1, 0, 0, 0)   // Right face normal
		};
	}

	public void setFaceColor(int face, Color color) {
		faceColos[face] = color;
	}

	public void translate(float dx, float dy, float dz) {
		transform.translate(dx, dy, dz);
	}

	public void addTransform(Matrix4f transform) {
		transform.mul(this.transform, this.transform);
	}

	public void setTemTransform(Matrix4f transform) {
		this.temTransform = transform;
	}

	// Getter method for the transformation matrix
	public Matrix4f getTransform() {
		return transform;
	}

	public Vector3f getCenter() {
		Vector4f center = temTransform.mul(transform, new Matrix4f()).transform(new Vector4f());
		return new Vector3f(center.x, center.y, center.z);
	}

	public void render(Matrix4f viewProjection, Vector3f camPos) {
		Matrix4f combinedTransform = temTransform.mul(transform, new Matrix4f());
		Matrix4f inverseTransform = new Matrix4f(combinedTransform).invert();
		Matrix4f viewProjectionCombined = viewProjection.mul(combinedTransform, new Matrix4f());
		Vector4f inverseCamPos = inverseTransform.transform(new Vector4f(camPos.x, camPos.y, camPos.z, 1f));

		for (int i = 0; i < faces.length; i++) {
			Vector4f faceNormal = normals[i];
			Vector4f anyFaceVertex = vertices[faces[i][0]];
			Vector4f dist = new Vector4f(inverseCamPos).sub(anyFaceVertex);
			float cosViewAngle = faceNormal.dot(dist.normalize());
//			cosViewAngle = 1;

			// Check if the face is visible (dot product is positive)
			if (cosViewAngle > 0) {
				drawFace(i, viewProjectionCombined);
			}
		}
	}

	private void drawFace(int i, Matrix4f projection) {
		int[] faceIndices = faces[i];

		double[] xs = new double[faces[i].length];
		double[] xy = new double[faces[i].length];

		for (int j = 0; j < faceIndices.length; j++) {
			Vector4f vertProjection = new Vector4f();
			projection.transform(vertices[faceIndices[j]], vertProjection);
			vertProjection.div(vertProjection.w);
			xs[j] = vertProjection.x + 0.5; //center at 1/2 screen width & height
			xy[j] = vertProjection.y + 0.5;
		}
		StdDraw.setPenColor(faceColos[i]);
		StdDraw.filledPolygon(xs, xy);

		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.polygon(xs, xy);
	}
}
