import greenfoot.*;

/**
 * A spider web which the Spider may walk on
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Web extends Actor {
	private GreenfootImage image;
	private int startX;
	private int startY;

	/**
	 * Create a new Web.
	 *
	 * @param startX the position of the left side of this web
	 * @param startY the position of the top of this web
	 */
	public Web(int startX, int startY) {
		this.startX = startX;
		this.startY = startY;
		setLocation(startX, startY);
		// Load and set initial image, used to prevent data loss from upscaling after downscaling
		image = new GreenfootImage("images/web.png");
		// A web will initially have no size
		GreenfootImage scaledImage = new GreenfootImage(image);
		scaledImage.scale(1, 1);
		setImage(scaledImage);
	}

	/**
	 * Update this web's position and size from a mouse drag.
	 *
	 * @param endX the position of the right side of this web
	 * @param endY the position of the bottom of this web
	 */
	public void drag(int endX, int endY) {
		int width = endX - startX;
		int height = endY - startY;
		// Resize the web image
		GreenfootImage scaledImage = new GreenfootImage(image);
		scaledImage.scale(width != 0 ? Math.abs(width) : 1, height != 0 ? Math.abs(height) : 1);
		setImage(scaledImage);
		// Update this web's location (location is centred in image)
		setLocation(startX + width / 2, startY + height / 2);
	}
}
