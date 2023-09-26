import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.event.KeyEvent;

public class Main {

	private static boolean requestExit;

	public static void main(String[] args) {
		StdDraw.enableDoubleBuffering();
		StdDraw.setCanvasSize(800, 500);

		float aspect = 800f / 500f;

		Camera cam = new Camera(
				new Vector3f(0.2f, 0f, -2f),
				new Vector3f(0f, 0f, 1f),
				90);


		float size = 0.5f;
		AABB box = new AABB(size, size, size);

		while (!requestExit) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				requestExit = true;
			}
			Matrix4f projection = cam.getProjection(aspect);
			Matrix4f view = cam.getView();
//			printMatrix(projection);

			Matrix4f eye = new Matrix4f().identity();
			box.drawPerspective(eye, projection.mul(view), cam.getViewDir());
			cam.getPos().add(0.005f, 0f, 0f);

			StdDraw.show();
			StdDraw.clear();
			StdDraw.pause(15);
		}
		System.exit(0);
	}

	public static void printMatrix(Matrix4f matrix) {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				System.out.printf("%.2f\t", matrix.get(col, row));
			}
			System.out.println();
		}
	}

}