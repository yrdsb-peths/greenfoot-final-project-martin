import greenfoot.*;

/**
 * The main character of the game, controlled by the player.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class Spider extends AnimatedActor {
	private static final int SPEED = 2;
	private static final int HIT_RADIUS = 50;
	private static final int IDLE_FRAME_DELAY = AnimatedActor.DEFAULT_FRAME_DELAY;
	private static final int WALK_FRAME_DELAY = 75;
	private static final GreenfootSound DEATH_SOUND = new GreenfootSound("sounds/new-super-mario-bros-u-death.mp3");

	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] imagesIdle = new GreenfootImage[2];
	private static final GreenfootImage[] imagesWalk = new GreenfootImage[4];

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
		for (int i = 0; i < imagesWalk.length; i++) {
			imagesWalk[i] = new GreenfootImage("images/spider-walk-" + i + ".png");
		}
		hasLoadedImages = true;
	}

	/**
	 * Tell the game world to draw hearts when this spider is added to it.
	 */
	protected void addedToWorld(World world) {
		((GameWorld) world).updateHearts(lives);
	}

	/**
	 * Move this spider according to the WASD and direction keys that are pressed.
	 *
	 * @return whether or not this spider's location was changed
	 */
	private boolean updateLocation() {
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
		if (dx != 0 || dy != 0) {
			// Determine whether or not this movement is valid; whether or not there is a web to move on
			int oldX = getX();
			int oldY = getY();
			setLocation(oldX + dx, oldY + dy);
			if (!isOnWeb() || isOutOfBounds()) {
				// Check if movement can be done on a single axis instead; only X
				setLocation(oldX + dx, oldY);
				if (!isOnWeb() || isOutOfBounds()) {
					// Can't move on the X axis; only Y
					setLocation(oldX, oldY + dy);
					if (!isOnWeb() || isOutOfBounds()) {
						// Can't move on either axis, reset
						setLocation(oldX, oldY);
					}
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
		return dx != 0 || dy != 0;
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
	 * Check if this spider is currently out of bounds.
	 *
	 * @return whether or not the spider's position lies outside of its world
	 */
	private boolean isOutOfBounds() {
		int x = getX();
		int y = getY();
		World world = getWorld();
		return x < 0 || x >= world.getWidth() || y < 0 || y >= world.getHeight();
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
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}

		if (isDying) {
			updateDyingAnimation();
			return;
		}

		boolean wasMoved = updateLocation();
		// Use the appropriate animation: if this spider was just moved, use the walk animation, but if not, use the idle animation
		if (wasMoved && getAnimation() != imagesWalk) {
			setAnimation(imagesWalk);
			setFrameDelay(WALK_FRAME_DELAY);
		} else if (!wasMoved && getAnimation() != imagesIdle) {
			setAnimation(imagesIdle);
			setFrameDelay(IDLE_FRAME_DELAY);
		}
		collectCoins();
		updateAnimation();
		if (!isOnWeb() || getObjectsInRange(HIT_RADIUS, Gas.class).size() != 0) {
			die();
		}
	}

	/**
	 * Remove a life from this spider.
	 */
	public void die() {
		lives--;
		((GameWorld) getWorld()).updateHearts(lives);
		// Initiate the dying animation
		isDying = true;
		velY = -18;
		DEATH_SOUND.play();
	}

	/**
	 * Continue the animation of this spider's death.
	 */
	private void updateDyingAnimation() {
		velY++;
		int y = getY() + velY;
		if (y >= 450) {
			GameWorld world = (GameWorld) getWorld();
			if (lives <= 0) {
				// Out of lives, game is over
				world.gameOver();
			} else {
				// Reset to start a new life
				setLocation(300, 200);
				isDying = false;
				world.createInitialWeb();
			}
		} else {
			setLocation(getX(), y);
		}
	}
}
