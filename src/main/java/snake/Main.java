package snake;

import lib.StdDraw;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

	int w = 800;
	int h = 600;
	private List<Tile> tiles;
	private Snake snake;

	Main() {
		StdDraw.setCanvasSize(w, h);
		StdDraw.setXscale(-w/2, w/2);
		StdDraw.setYscale(-h/2, h/2);

		tiles = new ArrayList<>();

		int gamePxHeight = 400;
		int gameWidth = 10;
		int gameHeight = 10;
		double tileSize = gamePxHeight / 10d;

		createTiles(gameWidth, gameHeight, tileSize);

		snake = new Snake(0, 4, gameWidth, gameHeight, tileSize);
		runGameLoop();
	}

	private void createTiles(int countX, int countY, double tileSize) {

		for (int y = 0; y < countY; ++y) {
			for (int x = 0; x < countX; ++x) {
				Tile tile = new Tile(tileSize, x, y, countX, countY);
				tile.setColor((x + y) % 2 == 0 ? StdDraw.BOOK_BLUE : StdDraw.BOOK_LIGHT_BLUE);
				tiles.add(tile);
			}
		}
	}

	long accumulator = 0;
	long moveInterval = 500;
	int moveX = 1;
	int moveY = 0;
	int lastMoveX = 0;
	int lastMoveY = 0;

	private void runGameLoop() {
		StdDraw.enableDoubleBuffering();
		long lastTime = System.currentTimeMillis();

		while (true) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				break;
			}

			accumulator += System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();

			update();
			StdDraw.show();
			StdDraw.pause(10);
			StdDraw.clear();
		}
		System.exit(0);
	}

	Random rnd = new Random();
	void update() {
		for (Tile tile : tiles) {
			tile.render();
		}
		getMovementInput();

		if (accumulator > moveInterval) {
			snake.move(moveX, moveY);
			lastMoveX = moveX;
			lastMoveY = moveY;
			accumulator -= moveInterval;

			if (snake.checkCollision()) {
				System.out.println("Game over!");
				System.exit(0);
			}
		}
		snake.render();
	}

	private void getMovementInput() {
		if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
			if (lastMoveX == 0) {
				moveX = -1;
				moveY = 0;
			}
		} else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
			if (lastMoveX == 0) {
				moveX = 1;
				moveY = 0;
			}
		} else if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
			if (lastMoveY == 0) {
				moveX = 0;
				moveY = 1;
			}
		} else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
			if (lastMoveY == 0) {
				moveX = 0;
				moveY = -1;
			}
		}
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
