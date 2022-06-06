import greenfoot.*;

/**
 * A collectable that the player may collect.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Coin extends AnimatedActor {
	/**
	 * Create a new coin.
	 */
	public Coin() {
		// Load animation images
		GreenfootImage[] images = new GreenfootImage[2];
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/coin-" + i + ".png");
		}
		setAnimation(images);
	}
}
