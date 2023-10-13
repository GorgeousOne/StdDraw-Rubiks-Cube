package snake;

import lib.StdDraw;

public class Button {

	private double x;
	private double y;
	private double width;
	private double height;
	private String text;
	private Runnable action;

	public Button(double x, double y, double width, double height) {
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
