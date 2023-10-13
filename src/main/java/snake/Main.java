package snake;

import lib.StdDraw;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

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
		snake.move(1, 0);
		snake.move(1, 0);
		snake.move(1, 0);
		snake.move(1, 0);
		snake.move(0, 1);
		snake.move(0, 1);
		snake.grow();
		snake.grow();
		snake.grow();
		snake.grow();
		snake.grow();
		snake.grow();


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
		for (Tile tile : tiles) {
			tile.render();
		}

		snake.render();
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
