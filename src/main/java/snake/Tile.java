package snake;

import lib.StdDraw;

import java.awt.Color;

public class Tile {
	
	private double size;
	private int x;
	private int y;
	private int maxX;
	private int maxY;
	private Color color;
	
	public Tile(double size, int x, int y, int maxX, int maxY) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.maxX = maxX;
		this.maxY = maxY;
		this.color = Color.GREEN;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void render() {
		double offX = x - 0.5 * maxX + 0.5;
		double offY = y - 0.5 * maxY + 0.5;

		StdDraw.setPenColor(color);
		StdDraw.filledSquare(offX * size, offY * size, 0.5f * size);
	}
}
