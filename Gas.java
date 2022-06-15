import greenfoot.*;

/**
 * The gas sprayed from the spray can.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Gas extends AnimatedActor {
	private static final int TYPE_COUNT = 2;
	private static final int ANGLE_RANGE = 60;

	private static final GreenfootImage[][] images = new GreenfootImage[TYPE_COUNT][2];
	private static boolean hasLoadedImages = false;

	private Spray spray;

	/**
	 * Create a new gas.
	 */
	public Gas(Spray spray) {
		this.spray = spray;
		loadImages();
		int type = Greenfoot.getRandomNumber(TYPE_COUNT);
		setAnimation(images[type]);
	}

	/**
	 * Load all images that gas actors use.
	 */
	private static void loadImages() {
		if (hasLoadedImages) {
			return;
		}
		for (int type = 0; type < TYPE_COUNT; type++) {
			for (int i = 0; i < images[type].length; i++) {
				images[type][i] = new GreenfootImage("images/gas-" + type + "-" + i + ".png");
			}
		}
		hasLoadedImages = true;
	}

	/**
	 * Initialize this gas's location and rotation when added to a world.
	 */
	protected void addedToWorld(World world) {
		// Set the location of this gas to the location of the spray's nozzle, which is 118.1 pixels away from the spray's centre at 45.34 degrees
		int sprayRotation = spray.getRotation();
		int xOffset = (int) (Math.cos(Math.toRadians(sprayRotation - 45.34)) * 118.1);
		int yOffset = (int) (Math.sin(Math.toRadians(sprayRotation - 45.34)) * 118.1);
		setLocation(spray.getX() + xOffset, spray.getY() + yOffset);
		// Set rotation to a random angle relative to the rotation of the spray
		int angleOffset = Greenfoot.getRandomNumber(ANGLE_RANGE) - ANGLE_RANGE / 2;
		setRotation(spray.getRotation() + angleOffset);
	}

	/**
	 * Update this gas.
	 */
	public void act() {
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}
		move(1);
		updateAnimation();
		updateFade();
	}
}
