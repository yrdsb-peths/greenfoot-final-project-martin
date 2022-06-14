import greenfoot.*;

/**
 * A clickable button with a label and an action.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class Button extends Actor {
	private static final int PADDING_X = 16;
	private static final int PADDING_Y = 8;

	private String string;
	private int size;
	private Color foreground;
	private Color background;
	private Color hoverBackground;
	private boolean isHovered = false;
	private Callback callback;

	/**
	 * Creates a new button.
	 *
	 * @param string the text to display on the button
	 * @param size the size of the button text
	 * @param foreground the color of the button text
	 * @param background the color of the button box when not hovered
	 * @param hoverBackground the color of the button box when hovered
	 * @param callback a Callback object with a run method to be called when the button is clicked
	 */
	public Button(String string, int size, Color foreground, Color background, Color hoverBackground, Callback callback) {
		this.string = string;
		this.size = size;
		this.foreground = foreground;
		this.background = background;
		this.hoverBackground = hoverBackground;
		this.callback = callback;
		createImage();
	}

	/**
	 * Creates this button's image, using the appropriate background color depending on whether or not it is being hovered.
	 */
	private void createImage() {
		GreenfootImage text = new GreenfootImage(string, size, foreground, isHovered ? hoverBackground : background);
		// Add padding to the button so that the box isn't too close to the text
		GreenfootImage image = new GreenfootImage(text.getWidth() + PADDING_X * 2, text.getHeight() + PADDING_Y * 2);
		image.setColor(isHovered ? hoverBackground : background);
		image.fill();
		image.drawImage(text, PADDING_X, PADDING_Y);
		setImage(image);
	}

	/**
	 * Updates this button's image if hovered and performs its action if clicked.
	 */
	public void act() {
		MouseInfo mouse = Greenfoot.getMouseInfo();
		if (mouse != null) {
			int mouseX = mouse.getX();
			int mouseY = mouse.getY();
			int x = getX();
			int y = getY();
			GreenfootImage image = getImage();
			int halfwidth = image.getWidth() / 2;
			int halfheight = image.getHeight() / 2;
			// Determine if mouse is above the button
			boolean isNowHovered = mouseX > x - halfwidth && mouseX < x + halfwidth && mouseY > y - halfheight && mouseY < y + halfheight;
			// When hover state changes, image should be updated to reflect it
			if (isHovered != isNowHovered) {
				isHovered = isNowHovered;
				createImage();
			}
		}
		if (Greenfoot.mouseClicked(this)) {
			callback.run();
		}
	}
}
