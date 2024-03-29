import greenfoot.*;

/**
 * Bug spray that kills the spider.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Spray extends AnimatedActor {
	private static final int LIFESPAN = 5000;
	private static final int GAS_INTERVAL = 750;

	private SimpleTimer timer = new SimpleTimer();
	private SimpleTimer gasTimer = new SimpleTimer();
	private boolean isMovingIn;
	private int targetX;
	private int targetY;

	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new spray object.
	 */
	public Spray() {
		loadImages();
		setAnimation(images);
	}

	/**
	 * Load all images that spray actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/spray-" + i + ".png");
		}
		hasLoadedImages = true;
	}

	/**
	 * Initialize this spray's position randomly when added to a world.
	 */
	protected void addedToWorld(World world) {
		int worldWidth = world.getWidth();
		int worldHeight = world.getHeight();
		targetX = Greenfoot.getRandomNumber(worldWidth);
		if (targetX < 50 || targetX > worldWidth - 50) {
			// If on the left or right edges of the world, this spray can be placed anywhere on the y-axis
			targetY = Greenfoot.getRandomNumber(worldHeight);
		} else {
			// If anywhere else on the x-axis, this spray can be placed on the top or bottom edges of the world
			targetY = Greenfoot.getRandomNumber(50);
			if (Greenfoot.getRandomNumber(2) == 0) {
				targetY = worldHeight - targetY;
			}
		}
		setLocation(targetX, targetY);
		turnTowards(worldWidth / 2, worldHeight / 2);
		// Start out of the world and move in
		move(-200);
		isMovingIn = true;
	}

	/**
	 * Update this spray.
	 */
	public void act() {
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}

		if (isMovingIn) {
			// Current objective: move to the target position
			if (Math.abs(getX() - targetX) < 3 && Math.abs(getY() - targetY) < 3) {
				isMovingIn = false;
				timer.mark();
			} else {
				move(1);
			}
		} else {
			// Wait to spray, then move out of the world, then remove this spray
			int time = timer.millisElapsed();
			if (time >= LIFESPAN + 5000) {
				// This spray has been moved outside of the world; remove it
				getWorld().removeObject(this);
			} else if (time >= LIFESPAN) {
				// This spray has finished spraying; move it outside of the world
				move(-1);
			} else if (!isFading() && gasTimer.millisElapsed() >= GAS_INTERVAL) {
				// Add a new gas every so often while spraying
				getWorld().addObject(new Gas(this), 0, 0);
				gasTimer.mark();
			}
		}
		updateAnimation();
		updateFade();
	}
}
