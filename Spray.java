import greenfoot.*;

/**
 * Bug spray that kills the spider.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Spray extends AnimatedActor {
	private static final int STILL_TIME = 2000;
	private static final int AWAY_TIME = 8000;
	private static final int LIFESPAN = 10000;

	private SimpleTimer timer;
	private boolean isMovingIn;
	private int targetX;
	private int targetY;
	private double x;
	private double y;

	private static boolean hasLoadedImages = false;
	private static final GreenfootImage[] images = new GreenfootImage[2];

	/**
	 * Create a new spray object.
	 */
	public Spray() {
		setAnimation(images);
		timer = new SimpleTimer();
	}

	/**
	 * Load all images that this spray uses.
	 */
	protected void loadImages() {
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
			targetY = Greenfoot.getRandomNumber(worldHeight);
		} else {
			targetY = Greenfoot.getRandomNumber(50);
			if (Greenfoot.getRandomNumber(2) == 0) {
				targetY = worldHeight - targetY;
			}
		}
		setLocation(targetX, targetY);
		x = (double) targetX;
		y = (double) targetY;
		turnTowards(worldWidth / 2, worldHeight / 2);
		move(-200);
		isMovingIn = true;
	}

	/**
	 * Move this spray the specified distance in the direction it is currently facing.
	 *
	 * @param distance the distance to move (in cell-size units); a negative value will move backwards
	 */
	public void move(int distance) {
		// Use double coordinates to avoid loss of precision in movement like the Actor.move method does
		double rotation = Math.toRadians(getRotation());
		x += Math.cos(rotation) * (double) distance;
		y += Math.sin(rotation) * (double) distance;
		setLocation((int) x, (int) y);
	}

	/**
	 * Update this spray.
	 */
	public void act() {
		if (isMovingIn) {
			// Current objective: move to the target position
			if (Math.abs((int) x - targetX ) < 3 && Math.abs((int) y - targetY) < 3) {
				isMovingIn = false;
				timer.mark();
			} else {
				move(1);
			}
		} else {
			// Wait to spray, then move out of the world, then remove this spray
			int time = timer.millisElapsed();
			if (time >= LIFESPAN + 5000) {
				getWorld().removeObject(this);
			} else if (time >= LIFESPAN) {
				move(-1);
			}
		}
		updateAnimation();
	}
}
