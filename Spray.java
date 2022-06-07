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

	/**
	 * Create a new spray object.
	 */
	public Spray() {
		// Load images
		GreenfootImage[] images = new GreenfootImage[2];
		for (int i = 0; i < images.length; i++) {
			images[i] = new GreenfootImage("images/spray-" + i + ".png");
		}
		setAnimation(images);
		timer = new SimpleTimer();
	}

	protected void addedToWorld(World world) {
		targetX = Greenfoot.getRandomNumber(100);
		if (Greenfoot.getRandomNumber(2) == 0) {
			targetX = world.getWidth() - targetX;
		}
		targetY = Greenfoot.getRandomNumber(100);
		if (Greenfoot.getRandomNumber(2) == 0) {
			targetY = world.getHeight() - targetY;
		}
		setLocation(targetX, targetY);
		turnTowards(world.getWidth() / 2, world.getHeight() / 2);
		move(-100);
		isMovingIn = true;
	}

	/**
	 * Update this spray.
	 */
	public void act() {
		if (isMovingIn) {
			// Current objective: move to the target position
			if (Math.abs(getX() - targetX) <= 100 && Math.abs(getY() - targetY) <= 100) {
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
