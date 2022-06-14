import greenfoot.*;

/**
 * A collectable that the player may collect.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Coin extends AnimatedActor {
	private static final int LIFESPAN = 7500;
	// Number of steps to decrease 'transparency' (really opacity) each act cycle while fading away
	private static final int FADE_INTERVAL = 4;

	private int transparency = 255;
	private SimpleTimer timer = new SimpleTimer();

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

	/**
	 * Check if it is time to fade or remove this coin.
	 */
	public void act() {
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}
		updateAnimation();
		if (timer.millisElapsed() >= LIFESPAN) {
			// Gradually decrease the image 'transparency' (really opacity) before removing this coin
			transparency -= FADE_INTERVAL;
			if (transparency <= 0) {
				// Remove this coin once it has become fully transparent
				getWorld().removeObject(this);
				return;
			}
			// Set the transparency on a copy of the original image so the change is not permanent
			GreenfootImage image = new GreenfootImage(getImage());
			image.setTransparency(transparency);
			setImage(image);
		}
	}
}
