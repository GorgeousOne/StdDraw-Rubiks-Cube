import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector3f;

public class AABB {
	private final Vector4f[] vertices; // Array to store the 12 vertices of the AABB
	private final int[][] faces;       // 2D array to store the indices for each face of the cube
	private final Vector4f[] normals;  // Array to store the normals for each face of the cube
	private Matrix4f transform;   // Transformation matrix for the AABB

	public AABB(float width, float height, float length) {
		this.transform = new Matrix4f().identity();
		this.vertices = computeVertices(width, height, length);
		this.faces = computeFaces();
		this.normals = computeNormals();
	}

	// Compute and return the 12 vertices of the AABB
	private Vector4f[] computeVertices(float width, float height, float length) {
		float halfWidth = width * 0.5f;
		float halfHeight = height * 0.5f;
		float halfLength = length * 0.5f;

		return new Vector4f[]{
				new Vector4f(-halfWidth, -halfHeight, -halfLength, 1),
				new Vector4f(halfWidth, -halfHeight, -halfLength, 1),
				new Vector4f(halfWidth, halfHeight, -halfLength, 1),
				new Vector4f(-halfWidth, halfHeight, -halfLength, 1),
				new Vector4f(-halfWidth, -halfHeight, halfLength, 1),
				new Vector4f(halfWidth, -halfHeight, halfLength, 1),
				new Vector4f(halfWidth, halfHeight, halfLength, 1),
				new Vector4f(-halfWidth, halfHeight, halfLength, 1)
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


	// Getter method for the transformation matrix
	public Matrix4f getTransform() {
		return transform;
	}

	// Function to draw visible faces with a parent and perspective transform
	public void drawPerspective(Matrix4f parentTransform, Matrix4f viewProjection, Vector3f viewDir) {
		Matrix4f combinedTransform = new Matrix4f(parentTransform).mul(transform);
		Matrix4f invertedTransform = new Matrix4f(combinedTransform).invert();
		Matrix4f viewProjectionCombined = new Matrix4f(viewProjection).mul(combinedTransform);

		Vector4f inverseViewDir = new Vector4f();
		invertedTransform.transform(new Vector4f(viewDir.x, viewDir.y, viewDir.z, 0f), inverseViewDir);

		for (int i = 0; i < faces.length; i++) {
			Vector4f faceNormal = normals[i];
			float dotProduct = inverseViewDir.dot(faceNormal);

			// Check if the face is visible (dot product is positive)
			if (1 > 0) { //dotProduct
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
			xs[j] = vertProjection.x + 0.5;
			xy[j] = vertProjection.y + 0.5;
		}

//		StdDraw.setPenColor(StdDraw.YELLOW);
//		StdDraw.filledPolygon(xs, xy);
		StdDraw.setPenRadius(0.004);
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		StdDraw.polygon(xs, xy);

	}
}
