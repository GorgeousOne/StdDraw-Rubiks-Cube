import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RubiksCube {
	private List<AABB> cubes;
	private RenderSort renderSort;

	public RubiksCube(float size) {
		this.cubes = computeCubes(size / 3);
		this.renderSort = new RenderSort();
	}

	private List<AABB> computeCubes(float cubeSize) {
		List<AABB> cubes = new ArrayList<>();

//		AABB box1 = new AABB(cubeSize, cubeSize, cubeSize);
//		AABB box2 = new AABB(cubeSize, cubeSize, cubeSize);
//		AABB box3 = new AABB(cubeSize, cubeSize, cubeSize);
//		box1.translate(-2 * cubeSize, 0, 0);
//		box3.translate(2 * cubeSize, 0, 0);
//		cubes.add(box1);
//		cubes.add(box2);
//		cubes.add(box3);

		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {
				for (int dz = -1; dz <= 1; ++dz) {
					if (dx == 0 && dy == 0 && dz == 0) {
						continue;
					}
					AABB cube = new AABB(cubeSize, cubeSize, cubeSize);
					cube.translate(dx * cubeSize, dy * cubeSize, dz * cubeSize);
					cubes.add(cube);
				}
			}
		}
		return cubes;
	}

	public void render(Matrix4f viewProjection, Vector3f camPos) {

		renderSort.setCamPos(camPos);
		cubes.sort(renderSort);

		for (AABB aabb : cubes) {
			aabb.render(new Matrix4f().identity(), viewProjection, camPos);
		}
	}
}