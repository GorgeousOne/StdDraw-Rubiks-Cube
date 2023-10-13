package snake;

import lib.StdDraw;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Final Project
 *
 * The goal of the project is to implement a Snake game in which the player must control a snake on a chessboard-like playing field to eat apples and not collide with himself or the edge of the playing field.
 * You have 7 weeks to complete this project. Here are tasks and sample code to break this large task into small manageable steps.
 * Please start to work on the project early and ask questions if you are stuck.
 *
 * Grid
 * The first thing to do is to implement the playing field. The field should simply consist of squares right next to each other filling the entire window.
 *
 * Drawing squares across the screen
 * - Use the template code to create a square window with StdDraw
 * - Decide on how many squares your game should have in either x or y direction
 * - Write a calculation how big one tile should be so that exactly as many squares fit into your window horizontally/vertically as you decided previously
 * - Implement a nested for loop inside the update function to draw a square for each tile
 *
 * Snake
 * The next step is to implement the snake.
 * The snake can be represented as a list of lines that are connected to each other.
 * Over time these lines move across the game field in one of four directions (up, down, left, right).
 * The user should be able to control the direction of the snake with the arrow keys.
 * Later on, the game should also check if the snake collides with it's own body or the edge of the playing field when moving in a direction.
 *
 * Drawing the snake
 * - Create a new class called "Snake"
 * - Think about what data the snake needs to store.
 *   Since the body of the snake will cover multiple tiles, you need a way to store an increasing amount of positions for the snake's body.
 * - create a drawing function for the snake that draws a line between all positions in the snake's body
 *   you can use StdDraw.line(x1, y1, x2, y2) to draw a line between two points
 *
 * Making the snake movement
 * - create a function that moves the snakes body by one tile in a given x or y direction.
 *   You can think of the snake as a list of positions. To move the snake, you need to add a new position at the front of the list and remove the last position from the list
 * - to move the snake in certain time intervals you need to keep track of the time since you moved it last.
 *   You can use System.currentTimeMillis() to get the current time in milliseconds.
 *   So in the update function only move the snake if the time since the last move is greater than a certain interval, e.g. 500ms.
 * - In the original snake game, the snake always moves, even if the player does not press any keys.
 *   To implement this, you have to keep track of the last direction the snake moved in and move the snake in that direction.
 *   The next step will be to change the direction of the snake when the player presses a key.
 *
 * To let the player control the snake, you need to check if the player pressed a key.
 * In the update function, you can e.g. use StdDraw.isKeyPressed(KeyEvent.VK_UP) to check if the player pressed the up arrow key.
 * (You will need to add "import java.awt.event.KeyEvent;" at the top of the file to use this)
 * - Implement the code to change the direction of the snake when the player presses a key.
 * - Think about what would happen if the player tried to move the snake in the exact opposite direction it came from.
 *   Is this possible in the original game? Can you come up with a way to prevent this movement if you know in which direction the snake moved last?
 *
 * Apple
 * The next step is to implement the apple that always spawns at a random position on the game field for the snake to eat.
 * Upon eating the apple the snakes length will increase by one and the apple will change its position.
 * -
 */
public class Main {

	int windowSize = 600;
	int gameSize = 10;
	double tileSize = 1d * windowSize / gameSize;

	private Snake snake;
	private boolean isGameOver = false;
	private final Button restartButton;

	private int appleX;
	private int appleY;
	private int score = 0;

	private final Random rnd = new Random();

	Main() {
		StdDraw.setCanvasSize(windowSize, windowSize);
		StdDraw.setXscale(0, windowSize);
		StdDraw.setYscale(0, windowSize);

		snake = new Snake(0, 4, gameSize, tileSize);
		spawnApple();

		restartButton = new Button(windowSize /2, windowSize/2, 2*tileSize, 2*tileSize);
		restartButton.setText("Restart");
		restartButton.setAction(this::restartGame);

		runGameLoop();
	}

	private void restartGame() {
		isGameOver = false;
		score = 0;
		snake = new Snake(0, 4, gameSize, tileSize);
		spawnApple();
		moveX = 1;
		moveY = 0;
		lastMoveX = 1;
		lastMoveY = 0;
		moveInterval = 400;
		lastMoveTime = System.currentTimeMillis() + 3 * moveInterval;
	}

	private void spawnApple() {
		appleX = rnd.nextInt(gameSize);
		appleY = rnd.nextInt(gameSize);
	}

	private boolean checkAppleCollision() {
		return snake.getHeadX() == appleX && snake.getHeadY() == appleY;
	}

	long lastMoveTime = 0;
	long moveInterval = 400;
	int moveX = 1;
	int moveY = 0;
	int lastMoveX = 0;
	int lastMoveY = 0;

	private void runGameLoop() {
		StdDraw.enableDoubleBuffering();
		lastMoveTime = System.currentTimeMillis();

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
		getMovementInput();

		if (!isGameOver) {
			moveSnake();
		}
		renderTiles();
		renderApple();
		snake.render();
		StdDraw.textLeft(0, tileSize/2, "Score: " + score);
		if (isGameOver) {
			restartButton.update();
		}
	}

	private void renderApple() {
		StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
		StdDraw.filledCircle((appleX + 0.5) * tileSize, (appleY + 0.5) * tileSize, 0.4f * tileSize);
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
		if (System.currentTimeMillis() - lastMoveTime < moveInterval) {
			return;
		}
		snake.move(moveX, moveY);
		lastMoveX = moveX;
		lastMoveY = moveY;
		lastMoveTime = System.currentTimeMillis();

		if (snake.checkCollision()) {
			isGameOver = true;
		}
		if (checkAppleCollision()) {
			if (snake.length() < gameSize * gameSize - 1) {
				snake.grow();
			}
			++score;

			//Extra credit: make the game harder as the score increases
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
