import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class RubiksCube {

	public enum Facing {
		LEFT(new Vector3f(-1, 0, 0)),
		RIGHT(new Vector3f(1, 0, 0)),
		DOWN(new Vector3f(0, -1, 0)),
		UP(new Vector3f(0, 1, 0)),
		BACK(new Vector3f(0, 0, -1)),
		FRONT(new Vector3f(0, 0, 1));

		private final Vector3f dir;

		Facing(Vector3f dir) {
			this.dir = dir;
		}

		Vector3f dir() {
			return new Vector3f(dir);
		}
	}

	private Color GREEN = new Color(0, 155, 72);
	private Color BLUE = new Color(0, 70, 173);
	private Color YELLOW = new Color(255, 213, 0);
	private Color ORANGE = new Color(238, 82, 0);
	private Color RED = new Color(183, 18, 52);

	private List<Cube> cubes;
	private RenderSort renderSort;
//	private Matrix4f[][][] cubeTransforms;
	private Cube[][][] positions;

	public RubiksCube(float size) {
		this.positions = new Cube[3][3][3];
		this.cubes = computeCubes(size / 3);
		this.renderSort = new RenderSort();
	}

	private List<Cube> computeCubes(float cubeSize) {
		List<Cube> cubes = new ArrayList<>();

		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {
				for (int dz = -1; dz <= 1; ++dz) {
					if (dx == 0 && dy == 0 && dz == 0) {
						continue;
					}
					Cube cube = new Cube(cubeSize);
					cube.translate(dx * cubeSize, dy * cubeSize, dz * cubeSize);
					updateColors(cube, dx, dy, dz);
					cubes.add(cube);
					positions[dx + 1][dy + 1][dz + 1] = cube;
				}
			}
		}
		return cubes;
	}

	private void updateColors(Cube cube, int dx, int dy, int dz) {
		if (dz == -1) {
			cube.setFaceColor(0, GREEN);
		}
		if (dz == 1) {
			cube.setFaceColor(1, BLUE);
		}
		if (dy == -1) {
			cube.setFaceColor(2, YELLOW);
		}
		if (dy == 1) {
			cube.setFaceColor(3, Color.WHITE);
		}
		if (dx == -1) {
			cube.setFaceColor(4, ORANGE);
		}
		if (dx == 1) {
			cube.setFaceColor(5, RED);
		}
	}

	public void rotateFace(Facing facing, int turns) {
		Matrix4f axisRot = new Matrix4f().rotate((float) Math.PI * 0.5f * turns, facing.dir);
		Vector3f dir = facing.dir();

		if (dir.x != 0) {
			rotateFaceX((int) dir.x, axisRot);
		} else if (dir.y != 0) {
//			rotateFaceY((int) dir.y, axisRot);
		} else if (dir.z != 0) {
//			rotateFaceZ((int) dir.z, axisRot);
		}
	}

	private void rotateFaceX(int dx, Matrix4f axisRot) {
		for (int dy = -1; dy <= 1; ++dy) {
			for (int dz = -1; dz <= 1; ++dz) {
				if (dx == 0 && dy == 0 && dz == 0) {
					continue;
				}
				Cube cube = positions[dx + 1][dy + 1][dz + 1];
				cube.addTransform(axisRot);
			}
		}
	}

	public void render(Matrix4f viewProjection, Vector3f camPos) {

		renderSort.setCamPos(camPos);
		cubes.sort(renderSort);

		for (Cube cube : cubes) {
			cube.render(new Matrix4f().identity(), viewProjection, camPos);
		}
	}
}