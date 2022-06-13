import greenfoot.*;

/**
 * A glass that tries to capture the spider.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Glass extends AnimatedActor {
	private static final int SPEED = 1;
	private boolean isCapturing = false;

	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new glass.
	 */
	public Glass() {
		loadImages();
		setAnimation(images);
	}

	/**
	 * Load all images that glass actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/glass-" + i + ".png");
		}
		hasLoadedImages = true;
	}

	/**
	 * Update this glass.
	 */
	public void act() {
		if (!isCapturing) {
			Spider spider = ((GameWorld) getWorld()).getSpider();
			int spiderX = spider.getX();
			int spiderY = spider.getY();
			if (getX() == spiderX && getY() + 50 == spiderY) {
				// Begin capturing the spider after reaching its location
				isCapturing = true;
			} else {
				// Follow the spider by moving towards it
				turnTowards(spiderX, spiderY - 50);
				move(SPEED);
				// Don't retain the rotation, because the image will look wrong
				setRotation(0);
			}
		}
		updateAnimation();
	}
}
