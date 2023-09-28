import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.Color;
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
	private int[][][] cubesPermutation;
	private TwistAnim twistAnim;

	public RubiksCube(float size) {
		this.cubesPermutation = new int[3][3][3];
		computeCubes(size / 3);
	}

	private void computeCubes(float cubeSize) {
		cubes = new ArrayList<>();

		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {
				for (int dz = -1; dz <= 1; ++dz) {
					if (dx == 0 && dy == 0 && dz == 0) {
						continue;
					}
					Cube cube = new Cube(cubeSize);
					cube.translate(dx * cubeSize, dy * cubeSize, dz * cubeSize);
					updateColors(cube, dx, dy, dz);
					cubesPermutation[dx + 1][dy + 1][dz + 1] = cubes.size();
					cubes.add(cube);
				}
			}
		}
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

	public void animate(TwistAnim anim) {
		this.twistAnim = anim;
	}

	public void render(Matrix4f viewProjection, Vector3f camPos, RenderQueue renderQueue) {

		long time = System.currentTimeMillis();

		if (twistAnim != null && twistAnim.getProgress(time) >= 1) {
			applyTwist();
			twistAnim = null;
		}
		if (twistAnim != null) {
			for (int i : twistAnim.getAffectedCubes()) {
				cubes.get(i).setTempTransform(twistAnim.getRotation(time));
			}
		}
		for (Cube cube : cubes) {
			cube.render(viewProjection, camPos, renderQueue);
		}
	}

	private void applyTwist() {
		for (int i : twistAnim.getAffectedCubes()) {
			Cube cube = cubes.get(i);
			cube.addTransform(twistAnim.getRotation(System.currentTimeMillis()));
			cube.setTempTransform(new Matrix4f().identity());
		}
		this.cubesPermutation = twistAnim.getNewPerm();
	}

	public int[][][] getPerm() {
		return cubesPermutation;
	}
}