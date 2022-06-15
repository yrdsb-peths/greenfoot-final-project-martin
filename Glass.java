import greenfoot.*;

/**
 * A glass that tries to capture the spider.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Glass extends AnimatedActor {
	private static final int SPEED = 1;
	private static final int UP_SPEED = 1;
	private static final int DOWN_SPEED = 6;
	private static final int SHADOW_OFFSET = 100;
	private static final int CAPTURE_OFFSET = 50;

	private GlassShadow shadow;

	private enum State {
		FOLLOWING, MOVING_UP, MOVING_DOWN, RETURNING
	}
	private State state = State.FOLLOWING;
	// Y position to peak at during capturing
	private int peakY;
	// Y position to capture at
	private int targetY;
	// Y position to return to after capturing
	private int returnY;

	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new glass.
	 */
	public Glass() {
		loadImages();
		setAnimation(images);
		shadow = new GlassShadow();
	}

	/**
	 * Load all images that glass actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/glass-" + i + ".png");
		}
		hasLoadedImages = true;
	}

	/**
	 * Add this glass's shadow to the world of this glass when it is added to one.
	 */
	protected void addedToWorld(World world) {
		world.addObject(shadow, getX(), getY() + SHADOW_OFFSET);
	}

	/**
	 * Update this glass.
	 */
	public void act() {
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}

		Spider spider = ((GameWorld) getWorld()).getSpider();
		switch (state) {
			case FOLLOWING:
				int spiderX = spider.getX();
				int spiderY = spider.getY();
				if (getX() == spiderX && getY() + SHADOW_OFFSET == spiderY) {
					// Begin capturing the spider after reaching its location
					state = State.MOVING_UP;
					targetY = getY() + CAPTURE_OFFSET;
					peakY = getY() - 50;
					returnY = getY();
				} else {
					// Follow the spider by moving towards it
					turnTowards(spiderX, spiderY - SHADOW_OFFSET);
					move(SPEED);
					// Don't retain the rotation, because the image will look wrong
					setRotation(0);
					shadow.setLocation(getX(), getY() + SHADOW_OFFSET);
				}
				break;

			case MOVING_UP:
				// First move the glass up before going down
				setLocation(getX(), getY() - UP_SPEED);
				// Start moving down once reached the peak
				if (getY() <= peakY) {
					state = State.MOVING_DOWN;
				}
				break;

			case MOVING_DOWN:
				setLocation(getX(), getY() + DOWN_SPEED);
				if (getY() >= targetY) {
					// Kill the spider if it was under this glass
					if (getOneObjectAtOffset(0, 70, Spider.class) != null) {
						spider.die();
					}
					// Begin moving back to the position before capture
					state = State.RETURNING;
				}
				break;

			case RETURNING:
				setLocation(getX(), getY() - UP_SPEED);
				// Return to following after trying to capture
				if (getY() <= returnY) {
					state = State.FOLLOWING;
				}
				break;
		}
		updateAnimation();
	}
}
