package snake;

import lib.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class Snake {

	private int maxX;
	private int maxY;
	private double size;

	private int length;
	private List<Integer> bodyXs;
	private List<Integer> bodyYs;

	public Snake(int x, int y, int maxX, int maxY, double size) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.size = size;
		bodyXs = new ArrayList<>();
		bodyYs = new ArrayList<>();

		length = 1;
		bodyXs.add(x);
		bodyYs.add(y);

		move(1, 0);
		move(1, 0);
		grow();
		grow();
	}

	public void move(int dx, int dy) {
		int newX = bodyXs.get(0) + dx;
		int newY = bodyYs.get(0) + dy;

		bodyXs.add(0, newX);
		bodyYs.add(0, newY);
	}

	public boolean checkCollision() {
		int headX = bodyXs.get(0);
		int headY = bodyYs.get(0);

		if (headX < 0 || headX >= maxX || headY < 0 || headY >= maxY) {
			return true;
		}

		for (int i = 1; i < length; ++i) {
			if (headX == bodyXs.get(i) && headY == bodyYs.get(i)) {
				return true;
			}
		}

		return false;
	}

	public void grow() {
		length++;
	}

	public void render() {
		double offX = -0.5 * maxX + 0.5;
		double offY = -0.5 * maxY + 0.5;
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.05);

		for (int i = 0; i < length - 1; ++i) {
			StdDraw.line(
					(bodyXs.get(i) + offX) * size,
					(bodyYs.get(i) + offY) * size,
					(bodyXs.get(i + 1) + offX) * size,
					(bodyYs.get(i + 1) + offY) * size);
		}

		int headX = getHeadX();
		int headY = getHeadY();
		int tailX = bodyXs.get(1);

		StdDraw.setPenColor(StdDraw.BLACK);

		if (tailX - headX != 0) {
			StdDraw.filledCircle((headX + offX) * size, (headY + 0.25 + offY) * size, 0.1 * size);
			StdDraw.filledCircle((headX + offX) * size, (headY - 0.25 + offY) * size, 0.1 * size);
		} else {
			StdDraw.filledCircle((headX + 0.25 + offX) * size, (headY + offY) * size, 0.1 * size);
			StdDraw.filledCircle((headX - 0.25 + offX) * size, (headY + offY) * size, 0.1 * size);
		}
	}

	public int getHeadX() {
		return bodyXs.get(0);
	}

	public int getHeadY() {
		return bodyYs.get(0);
	}

	public int length() {
		return length;
	}
}
