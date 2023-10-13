package snake;

import lib.StdDraw;

public class StdButton {

	private int x;
	private int y;
	private int width;
	private int height;
	private String text;
	private Runnable action;

	public StdButton(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width  = width;
		this.height = height;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void update() {
		render();
		if (isClicked()) {
			action.run();
		}
	}

	public boolean isClicked() {
		return StdDraw.isMousePressed()
				&& StdDraw.mouseX() >= x - 0.5 * width
				&& StdDraw.mouseX() <= x + 0.5 * width
				&& StdDraw.mouseY() >= y - 0.5 * height
				&& StdDraw.mouseY() <= y + 0.5 * height;
	}

	public void render() {
		StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
		StdDraw.filledRectangle(x, y, 0.5 * width, 0.5 * height);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(x, y, text);
	}
}
