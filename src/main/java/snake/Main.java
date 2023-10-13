package snake;

import lib.StdDraw;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Main {

	int windowSize = 400;
	int gameSize = 10;
	float tileSize = 1f * windowSize / gameSize;

	private Snake snake;
	private Apple apple;
	private int score = 0;
	private boolean isGameOver = false;
	private final StdButton restartButton;

	private final Random rnd = new Random();

	Main() {
		StdDraw.setCanvasSize(windowSize, windowSize);
		StdDraw.setXscale(0, windowSize);
		StdDraw.setYscale(0, windowSize);

		snake = new Snake(0, 4, gameSize, tileSize);
		spawnApple();

		restartButton = new StdButton(windowSize /2, windowSize/2, 100, 100);
		restartButton.setText("Restart");
		restartButton.setAction(() -> restartGame());

		runGameLoop();
	}

	private void restartGame() {
		isGameOver = false;
		score = 0;
		snake = new Snake(0, 4, gameSize, tileSize);
		spawnApple();
		accumulator = -3 * moveInterval;
		moveX = 1;
		moveY = 0;
		lastMoveX = 1;
		lastMoveY = 0;
	}

	private void spawnApple() {
		int x = rnd.nextInt(gameSize);
		int y = rnd.nextInt(gameSize);
		apple = new Apple(tileSize, x, y);
	}

	private boolean checkAppleCollision() {
		return snake.getHeadX() == apple.getX() && snake.getHeadY() == apple.getY();
	}

	long accumulator = 0;
	long moveInterval = 400;
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

	void update() {
		getMovementInput();

		if (!isGameOver) {
			moveSnake();
		}

		renderTiles();
		apple.render();
		snake.render();
		StdDraw.textLeft(0, 50, "Score: " + score);

		if (isGameOver) {
			restartButton.update();
		}
	}

	private void renderTiles() {
		for (int y = 0; y < gameSize; ++y) {
			for (int x = 0; x < gameSize; ++x) {
				double offX = x + 0.5;
				double offY = y + 0.5;

				Color color = (x + y) % 2 == 0 ? StdDraw.BOOK_BLUE : StdDraw.BOOK_LIGHT_BLUE;
				StdDraw.setPenColor(color);
				StdDraw.filledSquare(offX * tileSize, offY * tileSize, 0.5f * tileSize);

			}
		}
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

	private void moveSnake() {
		if (accumulator < moveInterval) {
			return;
		}
		snake.move(moveX, moveY);
		lastMoveX = moveX;
		lastMoveY = moveY;
		accumulator -= moveInterval;

		if (snake.checkCollision()) {
			isGameOver = true;
		}

		if (checkAppleCollision()) {
			if (snake.length() < gameSize * gameSize - 1) {
				snake.grow();
			}
			++score;

			if (score % 3 == 0) {
				moveInterval = Math.max(150, moveInterval - 50);
			}
			spawnApple();
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
