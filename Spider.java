import greenfoot.*;

/**
 * The main character of the game, controlled by the player.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class Spider extends AnimatedActor {
	private static final int SPEED = 2;

	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] imagesIdle = new GreenfootImage[2];

	private boolean isFacingRight = false;
	private boolean wasFacingRight = false;

	private int lives = 3;
	private boolean isDying = false;
	// Y velocity for the spider's dying animation
	private int velY;

	/**
	 * Create a new spider.
	 */
	public Spider() {
		loadImages();
		setAnimation(imagesIdle);
	}

	/**
	 * Load all images that spider actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int i = 0; i < imagesIdle.length; i++) {
			imagesIdle[i] = new GreenfootImage("images/spider-idle-" + i + ".png");
		}
		hasLoadedImages = true;
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
		// Determine whether or not this movement is valid; whether or not there is a web to move on
		int oldX = getX();
		int oldY = getY();
		setLocation(oldX + dx, oldY + dy);
		if (!isOnWeb()) {
			// Check if movement can be done on a single axis instead; only X
			setLocation(oldX + dx, oldY);
			if (!isOnWeb()) {
				// Can't move on the X axis; only Y
				setLocation(oldX, oldY + dy);
				if (!isOnWeb()) {
					// Can't move on either axis, reset
					setLocation(oldX, oldY);
				}
			}
		}
		// Update the direction flag
		wasFacingRight = isFacingRight;
		if (dx > 0) {
			isFacingRight = true;
		} else if (dx < 0) {
			isFacingRight = false;
		}
	}

	/**
	 * Check if this spider is currently on top of a web.
	 *
	 * @return true if on top of a web, false if not
	 */
	private boolean isOnWeb() {
		int x = getX();
		int y = getY();
		for (Web web : getWorld().getObjects(Web.class)) {
			if (web.isLockedIn() && web.isUnderPoint(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Advance this spider's animation and update its image.
	 *
	 * @return true if the animation was advanced, false otherwise
	 */
	protected boolean updateAnimation() {
		boolean wasUpdated = super.updateAnimation();
		if (!wasUpdated && isFacingRight != wasFacingRight) {
			// Animation frame is not yet due to be updated, but the direction has changed
			updateAnimationFrame();
			wasUpdated = true;
		}
		return wasUpdated;
	}

	/**
	 * Set this spider's image according to its current animation frame and direction.
	 */
	protected void updateAnimationFrame() {
		GreenfootImage image = getAnimationFrame();
		// Mirror the image if facing right (all images originally face left)
		if (isFacingRight) {
			image = new GreenfootImage(image);
			image.mirrorHorizontally();
		}
		setImage(image);
	}

	/**
	 * Collect any coins that this spider is touching.
	 */
	private void collectCoins() {
		for (Coin coin : getIntersectingObjects(Coin.class)) {
			((GameWorld) getWorld()).collectCoin(coin);
		}
	}

	/**
	 * Update this spider.
	 */
	public void act() {
		if (isDying) {
			updateDyingAnimation();
			return;
		}
		updateLocation();
		collectCoins();
		updateAnimation();
		if (!isOnWeb()) {
			die();
		}
	}

	/**
	 * Remove a life from this spider.
	 */
	private void die() {
		lives--;
		// Initiate the dying animation
		isDying = true;
		velY = -18;
	}

	/**
	 * Continue the animation of this spider's death.
	 */
	private void updateDyingAnimation() {
		velY++;
		int y = getY() + velY;
		if (y >= 450) {
			if (lives <= 0) {
				// Out of lives, game is over
				Greenfoot.setWorld(new GameOverWorld());
			} else {
				// Reset to start a new life
				setLocation(300, 200);
				isDying = false;
			}
		} else {
			setLocation(getX(), y);
		}
	}
}
