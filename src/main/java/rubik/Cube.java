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
	private final int[][] faces;
	// Array to store the normals for each face of the cube
	private final Vector4f[] normals;
	// Array to store the normals for each face of the cube
	private final Vector4f[] centers;

	// Transformation matrix for the AABB
	private Matrix4f transform;
	//like a parent transform but without the parent :p
	private Matrix4f tempTransform;
	private final Color[] faceColors;

	public Cube(float size) {
		this.transform = new Matrix4f().identity();
		this.tempTransform = new Matrix4f().identity();
		this.vertices = computeVertices(0.5f * size);
		this.faces = computeFaces();
		this.normals = computeNormals();
		this.centers = computeFaceCenters();
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
	private int[][] computeFaces() {
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

	private Vector4f[] computeFaceCenters() {
		Vector4f[] centers = new Vector4f[faces.length];

		for (int i = 0; i < faces.length; i++) {
			Vector4f v0 = vertices[faces[i][0]];
			Vector4f v2 = vertices[faces[i][2]];
			centers[i] = v0.add(v2, new Vector4f()).mul(0.5f);
		}
		return centers;
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

	public void setTempTransform(Matrix4f transform) {
		this.tempTransform = transform;
	}

	public void render(Matrix4f viewProjection, Vector3f camPos, RenderQueue renderQueue) {
		Matrix4f combinedTransform = tempTransform.mul(transform, new Matrix4f());
		Matrix4f inverseTransform = new Matrix4f(combinedTransform).invert();
		Matrix4f viewProjectionCombined = viewProjection.mul(combinedTransform, new Matrix4f());
		Vector4f inverseCamPos = inverseTransform.transform(new Vector4f(camPos.x, camPos.y, camPos.z, 1f));

		for (int i = 0; i < faces.length; i++) {
			Vector4f faceNormal = normals[i];
			Vector4f anyFaceVertex = vertices[faces[i][0]];
			Vector4f dist = new Vector4f(inverseCamPos).sub(anyFaceVertex);
			float cosViewAngle = faceNormal.dot(dist.normalize());

			//cull invisible faces
			if (cosViewAngle > 0) {
				//give render queue a reference for 3d position of a face
				Vector4f faceCenter = combinedTransform.transform(centers[i], new Vector4f());
				faceCenter.div(faceCenter.w);
				drawFace(i, viewProjectionCombined, new Vector3f(faceCenter.x, faceCenter.y, faceCenter.z), renderQueue);
			}
		}
	}

	private void drawFace(int i, Matrix4f projection, Vector3f faceCenter, RenderQueue renderQueue) {
		int[] faceIndices = faces[i];

		double[] xs = new double[faces[i].length];
		double[] ys = new double[faces[i].length];

		for (int j = 0; j < faceIndices.length; j++) {
			Vector4f vertProjection = new Vector4f();
			projection.transform(vertices[faceIndices[j]], vertProjection);
			vertProjection.div(vertProjection.w);
			xs[j] = vertProjection.x + 0.5; //screen center is at (0.5, 0.5)
			ys[j] = vertProjection.y + 0.5;
		}
		renderQueue.queue(xs, ys, faceCenter, faceColors[i], StdDraw.BLACK);
	}
}
