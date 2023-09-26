import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.event.KeyEvent;

public class Main {

	private static boolean requestExit;

	public static void main(String[] args) {
		StdDraw.enableDoubleBuffering();

		int w = 800;
		int h = 500;

		StdDraw.setCanvasSize(w, h);
		float aspect = 1f * w / h;

		Camera cam = new Camera(new Vector3f(), 2, 0, 30, 90);

		float size = 0.5f;
		AABB box = new AABB(size, size, size);

		while (!requestExit) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				requestExit = true;
			}
			Matrix4f projection = cam.getProjection(aspect);
			Matrix4f view = cam.getView();

			Matrix4f eye = new Matrix4f().identity();
			box.drawPerspective(eye, projection.mul(view), cam.getPos());
			cam.move((float) Math.PI / 2f, 0);

			StdDraw.show();
			StdDraw.pause(15);
			StdDraw.clear();
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