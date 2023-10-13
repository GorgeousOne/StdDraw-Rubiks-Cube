package snake;

import lib.StdDraw;

import java.awt.Color;

public class Apple {


	private double size;
	private int x;
	private int y;
	private int maxX;
	private int maxY;
	private Color color;

	public Apple(double size, int x, int y, int maxX, int maxY) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.maxX = maxX;
		this.maxY = maxY;
		this.color = StdDraw.PRINCETON_ORANGE;
	}

	public void render() {
		double offX = x - 0.5 * maxX + 0.5;
		double offY = y - 0.5 * maxY + 0.5;

		StdDraw.setPenColor(color);
		StdDraw.filledCircle(offX * size, offY * size, 0.4f * size);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
