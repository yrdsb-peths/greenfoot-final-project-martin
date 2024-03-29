import greenfoot.*;
import java.util.LinkedList;

/**
 * A spider web which the Spider may walk on
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Web extends AnimatedActor {
	private static final int MAX_WEB_COUNT = 3;
	// Number of milliseconds that a web will exist for after being locked in
	private static final int MIN_LIFESPAN = 1000;
	private static final int MAX_LIFESPAN = 5000;
	// Area of a web to be considered large
	private static final int LARGE_AREA = 600 * 400;

	private static LinkedList<Web> webs = new LinkedList<Web>();

	private static GreenfootImage notlockedImage;
	private static GreenfootImage lockedImage;
	private static boolean hasLoadedImages = false;

	private int startX;
	private int startY;
	private boolean isLockedIn = false;

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
		if (!hasLoadedImages) {
			// Load and set initial image, used to prevent data loss from upscaling after downscaling
			notlockedImage = new GreenfootImage("images/web-notlocked.png");
			lockedImage = new GreenfootImage("images/web.png");
			hasLoadedImages = true;
		}
		// A web will initially have no size
		GreenfootImage image = new GreenfootImage(notlockedImage);
		image.scale(1, 1);
		setImage(image);
		webs.addLast(this);
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
		// Adjust for the circular web because it doesn't reach the corner of the image
		int imageWidth = width != 0 ? Math.abs(width * 381 / 296) : 1;
		int imageHeight = height != 0 ? Math.abs(height * 347 / 230) : 1;
		// Resize the web image
		GreenfootImage image = new GreenfootImage(notlockedImage);
		image.scale(imageWidth, imageHeight);
		setImage(image);
		// Update this web's location (location is centred in image)
		setLocation(startX + width / 2, startY + height / 2);
	}

	/**
	 * Return whether or not this web is locked in.
	 */
	public boolean isLockedIn() {
		return isLockedIn;
	}

	/**
	 * Lock in this web and start its timer.
	 */
	public void lockIn() {
		// Remove the oldest web after reaching the max to prevent the player from creating unlimited webs
		if (webs.size() >= MAX_WEB_COUNT) {
			Web oldestWeb = webs.removeFirst();
			oldestWeb.fadeAway();
		}
		// Switch to the locked-in image
		GreenfootImage image = new GreenfootImage(lockedImage);
		GreenfootImage currentImage = getImage();
		int width = currentImage.getWidth();
		int height = currentImage.getHeight();
		image.scale(width, height);
		setImage(image);
		// Start timing the existence of this web
		int lifespan = (LARGE_AREA - width * height) * MAX_LIFESPAN / LARGE_AREA;
		if (lifespan < MIN_LIFESPAN) {
			lifespan = MIN_LIFESPAN;
		}
		setLifespan(lifespan);
		startLifespan();
		isLockedIn = true;
	}

	/**
	 * Check if a point is inside of this web.
	 *
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 * @return true if the point is inside of this web, false if not
	 */
	public boolean isUnderPoint(int x, int y) {
		// Since webs may be elliptical, Actor.getObjectsInRange doesn't cut it
		GreenfootImage image = getImage();
		double a = Math.pow(x - getX(), 2) / Math.pow(image.getWidth() / 2, 2);
		double b = Math.pow(y - getY(), 2) / Math.pow(image.getHeight() / 2, 2);
		return a + b < 1.0;
	}

	/**
	 * Update this web.
	 */
	public void act() {
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}
		updateFade();
	}
}
