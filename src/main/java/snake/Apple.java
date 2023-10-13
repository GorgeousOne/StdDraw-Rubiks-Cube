package snake;

import lib.StdDraw;

import java.awt.Color;

public class Apple {


	private double size;
	private int x;
	private int y;
	private Color color;

	public Apple(double size, int x, int y) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.color = StdDraw.PRINCETON_ORANGE;
	}

	public void render() {
		double offX = x + 0.5;
		double offY = y + 0.5;

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
