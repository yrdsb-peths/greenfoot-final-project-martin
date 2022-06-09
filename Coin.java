import greenfoot.*;

/**
 * A collectable that the player may collect.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Coin extends AnimatedActor {
	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new coin.
	 */
	public Coin() {
		loadImages();
		setAnimation(images);
	}

	/**
	 * Load all images that coin actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/coin-" + i + ".png");
		}
		hasLoadedImages = true;
	}
}
