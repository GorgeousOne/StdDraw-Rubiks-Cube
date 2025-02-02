package rubik;

import lib.StdDraw;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private Camera cam;
	private RenderQueue renderQueue;
	private RubiksCube box;
	private SpotSphere sphere;
	private float aspect;

	private float pMouseX;
	private float pMouseY;
	private float mouseSensitivity;

	private long start;
	private boolean requestExit;

	private TwistAnim anim2;

	Main() {
		mouseSensitivity = 200;
		setGameSize(800, 600);
		cam = new Camera(new Vector3f(), 3, 30, 25, 30);
		renderQueue = new RenderQueue();

		box = new RubiksCube(0.5f);
		sphere = new SpotSphere(2f, 100, 0.02f);

		TwistAnim twist = new TwistAnim(5000, box.getPerm());
		twist.twistZ(-1);
		twist.start();
		box.animate(twist);

		runGameLoop();
	}

	private void setGameSize(int w, int h) {
		StdDraw.setCanvasSize(w, h);
		this.aspect = 1f * w / h;
		StdDraw.setXscale(-1, 1);
		StdDraw.setYscale(-1 / aspect, 1 / aspect);
	}

	private void runGameLoop() {
		StdDraw.enableDoubleBuffering();
		start = System.currentTimeMillis();
		pMouseX = (float) StdDraw.mouseX();
		pMouseY = (float) StdDraw.mouseY();

		while (!requestExit) {
			StdDraw.clear();

			if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				requestExit = true;
			}

			if (System.currentTimeMillis() - start > 7000 && anim2 == null) {
				anim2 = new TwistAnim(5000, box.getPerm());
				anim2.twistY(0);
				anim2.start();
				box.animate(anim2);
			}
			handleMouseInput();
			render();

			StdDraw.show();
			//StdDraw.pause(15);
		}
		System.exit(0);
	}

	private void render() {
		StdDraw.setPenRadius(0.006);
		
		Matrix4f viewProjection = cam.getViewProjection();
		sphere.render(viewProjection, cam.getViewDir());
		box.render(cam.getPos(), renderQueue);
		renderQueue.render(new Vector4f(cam.getPos(), 1), viewProjection);
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