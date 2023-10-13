package snake;

import lib.StdDraw;

import java.awt.event.KeyEvent;

public class Main {

	int w = 800;
	int h = 600;

	Main() {
		StdDraw.setCanvasSize(w, h);
		StdDraw.setXscale(-w/2, w/2);
		StdDraw.setYscale(-h/2, h/2);
		runGameLoop();
	}

	private void runGameLoop() {
		StdDraw.enableDoubleBuffering();

		while (true) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				break;
			}

			update();
			StdDraw.show();
			StdDraw.pause(10);
			StdDraw.clear();
		}
		System.exit(0);
	}

	void update() {
		drawSnakeField(10, 10,50);
	}

	void drawSnakeField(int width, int height, float size) {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.005);

		float offx = -0.5f * width * size + 0.5f * size;
		float offy = -0.5f * height * size + 0.5f * size;

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				StdDraw.square(offx + x * size, offy + y * size, 0.5f * size);
			}
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
