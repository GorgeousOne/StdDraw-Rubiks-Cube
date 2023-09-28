import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwistAnim {

	private long startTime;
	private long duration;
	int[][][] rubikPermuation;
	Vector3f rotAxis;
	int layer;
	List<Integer> affectedCubes;

	public TwistAnim(long duration, int[][][] rubikPermuation) {
		this.duration = duration;
		this.rubikPermuation = rubikPermuation;
		this.affectedCubes = new ArrayList<>();
	}

	public void twistX(int layerX) {
		int[][][] newPerm = new int[3][3][3];
		copyPermutation(rubikPermuation, newPerm);

		for (int dy = -1; dy <= 1; ++dy) {
			for (int dz = -1; dz <= 1; ++dz) {
				newPerm[layerX + 1][dy + 1][dz + 1] = rubikPermuation[layerX + 1][(-dz + 1)][dy + 1];
				affectedCubes.add(rubikPermuation[layerX + 1][dy + 1][dz + 1]);
			}
		}
		System.out.println(Arrays.deepToString(rubikPermuation));
		System.out.println(Arrays.deepToString(newPerm));
		System.out.println(affectedCubes);
		rubikPermuation = newPerm;
		layer = layerX;
		rotAxis = new Vector3f(1, 0, 0);
	}

	public void twistY(int layerY) {
		int[][][] newPerm = new int[3][3][3];
		copyPermutation(rubikPermuation, newPerm);

		for (int dx = -1; dx <= 1; ++dx) {
			for (int dz = -1; dz <= 1; ++dz) {
				newPerm[dx + 1][layerY + 1][dz + 1] = rubikPermuation[(-dz + 1)][layerY + 1][dx + 1];
				affectedCubes.add(rubikPermuation[dx + 1][layerY + 1][dz + 1]);
			}
		}
		rubikPermuation = newPerm;
		layer = layerY;
		rotAxis = new Vector3f(0, 1, 0);
	}

	public void twistZ (int layerZ) {
		int[][][] newPerm = new int[3][3][3];
		copyPermutation(rubikPermuation, newPerm);

		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {
				newPerm[dx + 1][dy + 1][layerZ + 1] = rubikPermuation[dy + 1][-dx + 1][layerZ + 1];
				affectedCubes.add(rubikPermuation[dx + 1][dy + 1][layerZ + 1]);
			}
		}
		rubikPermuation = newPerm;
		layer = layerZ;
		rotAxis = new Vector3f(0, 0, 1);
	}


	private void copyPermutation(int[][][] a, int[][][] b) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				System.arraycopy(a[i][j], 0, b[i][j], 0, a[0][0].length);
			}
		}
	}

	public boolean isAffected(int cubeIdx) {
		return affectedCubes.contains(cubeIdx);
	}

	void start() {
		startTime = System.currentTimeMillis();
	}

	public float getProgress(long time) {
//		return 0f;
		float progress = (float) (time - startTime) / duration;
		return Math.max(0, Math.min(1, progress));
	}

	public int[][][] getNewPerm() {
		return rubikPermuation;
	}
	public Matrix4f getRotation(long time) {
		float angle = (float) Math.PI * 0.5f * getProgress(time);
		return new Matrix4f().rotate(angle, rotAxis);
	}
}
