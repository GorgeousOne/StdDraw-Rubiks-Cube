package snake;

import lib.StdDraw;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Template {

	//window size in pixels
	int windowSize = 400;

	public Template() {
		//set canvas size
		StdDraw.setCanvasSize(windowSize, windowSize);
		//set coordinate scale to match window size
		//so (0, 0) is the bottom left corner and (windowSize, windowSize) is the top right corner
		StdDraw.setXscale(0, windowSize);
		StdDraw.setYscale(0, windowSize);

		//start the game loop
		runGameLoop();
	}

	private void runGameLoop() {
		StdDraw.enableDoubleBuffering();

		//continuously update and render the game
		while (true) {
			//exit game if ESC is pressed
			if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				break;
			}
			update();
			StdDraw.show();
			StdDraw.pause(20);
			StdDraw.clear();
		}
	}

	//run game logic and render game objects
	void update() {
		//draw an example circle at the center of the screen
		StdDraw.setPenColor(Color.MAGENTA);
		StdDraw.filledCircle(windowSize/2, windowSize/2, 20);
	}

	public static void main(String[] args) {
		new Template();
	}
}
