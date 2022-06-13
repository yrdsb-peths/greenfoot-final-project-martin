import greenfoot.*;

/**
 * The shadow of a glass.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class GlassShadow extends AnimatedActor {
	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new glass shadow.
	 */
	public GlassShadow() {
		loadImages();
		setAnimation(images);
	}

	/**
	 * Load all images that glass shadow actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/glass-shadow-" + i + ".png");
		}
		hasLoadedImages = true;
	}
}
