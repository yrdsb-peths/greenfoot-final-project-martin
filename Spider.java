import greenfoot.*;

/**
 * The main character of the game, controlled by the player.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class Spider extends Actor {
	private static final int SPEED = 2;
	private static final int ANIM_DELAY = 250;

	private static GreenfootImage[] imagesIdle = new GreenfootImage[2];
	private GreenfootImage[] animFrames;
	private int animIndex;
	private SimpleTimer animTimer;

	public Spider() {
		// Load images into image arrays
		for (int i = 0; i < imagesIdle.length; i++) {
			imagesIdle[i] = new GreenfootImage("images/spider-idle-" + i + ".png");
		}
		// Initialize animation
		animFrames = imagesIdle;
		animIndex = 0;
		animTimer = new SimpleTimer();
		setImage(animFrames[animIndex]);
	}

	/**
	 * Move this spider according to the WASD and direction keys that are pressed.
	 */
	private void updateLocation() {
		int dx = 0;
		int dy = 0;
		if (Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left")) {
			dx -= SPEED;
		}
		if (Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right")) {
			dx += SPEED;
		}
		if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up")) {
			dy -= SPEED;
		}
		if (Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down")) {
			dy += SPEED;
		}
		setLocation(getX() + dx, getY() + dy);
	}

	/**
	 * Advance this spider's animation and update its image.
	 */
	private void updateAnimation() {
		if (animTimer.millisElapsed() > ANIM_DELAY) {
			animIndex = (animIndex + 1) % animFrames.length;
			setImage(animFrames[animIndex]);
			animTimer.mark();
		}
	}

	/**
	 * Update this spider.
	 */
	public void act() {
		updateAnimation();
		updateLocation();
	}
}
