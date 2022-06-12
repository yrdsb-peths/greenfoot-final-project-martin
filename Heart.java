import greenfoot.*;

/**
 * A visual representation of one spider life.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Heart extends AnimatedActor {
	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new heart.
	 */
	public Heart() {
		loadImages();
		setAnimation(images);
	}

	/**
	 * Load all images that heart actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/heart-" + i + ".png");
		}
		hasLoadedImages = true;
	}
}
