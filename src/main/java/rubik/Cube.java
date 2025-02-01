package rubik;

import lib.StdDraw;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector3f;

import java.awt.Color;

public class Cube {
	// Array to store the 8 vertices of the AABB
	private final Vector4f[] vertices;
	// 2D array to store the indices for each face of the cube
	private final int[][] facesIndices;
	// Array to store the normals for each face of the cube
	private final Vector4f[] normals;

	// Transformation matrix for the AABB
	private Matrix4f transform;
	//like a parent transform but without the parent :p
	private Matrix4f tempTransform;
	private final Color[] faceColors;

	public Cube(float size) {
		this.transform = new Matrix4f().identity();
		this.tempTransform = new Matrix4f().identity();
		this.vertices = computeVertices(0.5f * size);
		this.facesIndices = computeIndices();
		this.normals = computeNormals();
		this.faceColors = new Color[] {
				StdDraw.DARK_GRAY, StdDraw.DARK_GRAY, StdDraw.DARK_GRAY,
				StdDraw.DARK_GRAY, StdDraw.DARK_GRAY, StdDraw.DARK_GRAY
		};
	}

	private Vector4f[] computeVertices(float size) {
		return new Vector4f[] {
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
	private int[][] computeIndices() {
		return new int[][] {
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
		return new Vector4f[] {
				new Vector4f(0, 0, -1, 0), // Front face normal
				new Vector4f(0, 0, 1, 0),  // Back face normal
				new Vector4f(0, -1, 0, 0), // Bottom face normal
				new Vector4f(0, 1, 0, 0),  // Top face normal
				new Vector4f(-1, 0, 0, 0), // Left face normal
				new Vector4f(1, 0, 0, 0)   // Right face normal
		};
	}

	public void setFaceColor(int face, Color color) {
		faceColors[face] = color;
	}

	public void translate(float dx, float dy, float dz) {
		transform.translate(dx, dy, dz);
	}

	public void addTransform(Matrix4f transform) {
		transform.mul(this.transform, this.transform);
	}

	/*
	 * Set a temporary transform for the cube e.g. for animation purposes.
	 */
	public void setTempTransform(Matrix4f transform) {
		this.tempTransform = transform;
	}

	public void render(Vector3f camPos, RenderQueue renderQueue) {
		Matrix4f worldTransform = tempTransform.mul(transform, new Matrix4f());
		
		for (int i = 0; i < facesIndices.length; i++) {
			int[] indices = facesIndices[i];
			Vector4f anyFaceVertex = worldTransform.transform(vertices[indices[0]], new Vector4f());
			Vector4f camDist = new Vector4f(camPos, 1).sub(anyFaceVertex);
			Vector4f faceNormal = worldTransform.transform(normals[i], new Vector4f());
			float cosViewAngle = faceNormal.dot(camDist.normalize());

			//back-face cull cube
			if (cosViewAngle > 0) {
				renderQueue.queueFlatPoly(
						new Vector4f[] {
								worldTransform.transform(vertices[indices[0]], new Vector4f()),
								worldTransform.transform(vertices[indices[1]], new Vector4f()),
								worldTransform.transform(vertices[indices[2]], new Vector4f()),
								worldTransform.transform(vertices[indices[3]], new Vector4f())
						},
						faceColors[i],
						StdDraw.BLACK
				);
			}
		}
	}
}
