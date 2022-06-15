import greenfoot.*;

/**
 * A collectable that the player may collect.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Coin extends AnimatedActor {
	private static final int LIFESPAN = 7500;

	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new coin.
	 */
	public Coin() {
		loadImages();
		setAnimation(images);
		setLifespan(LIFESPAN);
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

	/**
	 * Update this coin.
	 */
	public void act() {
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}
		updateAnimation();
		updateFade();
	}
}
