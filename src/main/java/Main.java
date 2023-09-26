import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.event.KeyEvent;

public class Main {

	private Camera cam;
	private RubiksCube box;
	private float aspect;

	private float pMouseX;
	private float pMouseY;
	private float mouseSensitivity;

	private boolean requestExit;

	Main() {
		mouseSensitivity = 150;
		cam = new Camera(new Vector3f(), 2, 0, -30, 60);
		box = new RubiksCube(0.5f);
		StdDraw.setPenRadius(0.006);
		setGameSize(800, 600);

		runGameLoop();
	}

	private void setGameSize(int w, int h) {
		StdDraw.setCanvasSize(w, h);
		this.aspect = 1f * w / h;
//		StdDraw.setXscale(-w / 2f, w / 2f);
	}

	private void runGameLoop() {
		StdDraw.enableDoubleBuffering();
		pMouseX = (float) StdDraw.mouseX();
		pMouseY = (float) StdDraw.mouseY();

		while (!requestExit) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				requestExit = true;
			}
			handleMouseInput();
			render();

			StdDraw.show();
			StdDraw.pause(15);
			StdDraw.clear();
		}
		System.exit(0);
	}

	private void render() {
		Matrix4f projection = cam.getProjection(aspect);
		Matrix4f view = cam.getView();

		box.render(projection.mul(view), cam.getPos());
	}

	private void handleMouseInput() {
		float mouseX = (float) StdDraw.mouseX();
		float mouseY = (float) StdDraw.mouseY();

		if (StdDraw.isMousePressed()) {
			float dx = mouseX - pMouseX;
			float dy = mouseY - pMouseY;
			cam.move(-dx * mouseSensitivity,
					-dy * mouseSensitivity);
		}

		pMouseX = mouseX;
		pMouseY = mouseY;
	}

	public static void printMatrix(Matrix4f matrix) {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				System.out.printf("%.2f\t", matrix.get(col, row));
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}