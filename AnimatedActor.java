import greenfoot.*;

/**
 * An actor class adding an animation system.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public abstract class AnimatedActor extends Actor {
	private static final int FRAME_DELAY = 250;

	private GreenfootImage[] frames;
	private int index;
	private SimpleTimer timer;

	// Precise location for fine movement
	private double x;
	private double y;

	/**
	 * Create a new animated actor.
	 */
	public AnimatedActor() {
		loadImages();
		timer = new SimpleTimer();
	}

	/**
	 * Load all images that this animated actor uses.
	 */
	protected abstract void loadImages();

	/**
	 * Set this actor's animation to an array of animation frames.
	 *
	 * @param frames the array of animation frames to use
	 */
	protected void setAnimation(GreenfootImage[] frames) {
		this.frames = frames;
		index = 0;
		updateAnimationFrame();
	}

	/**
	 * Return the GreenfootImage of this actor's current animation frame.
	 *
	 * @return this actor's current animation frame
	 */
	protected GreenfootImage getAnimationFrame() {
		return frames[index];
	}

	/**
	 * Set this actor's image according to its current animation frame.
	 */
	protected void updateAnimationFrame() {
		setImage(getAnimationFrame());
	}

	/**
	 * Advance this actors's animation and update its image accordingly.
	 *
	 * @return true if the animation was advanced, false otherwise
	 */
	protected boolean updateAnimation() {
		if (timer.millisElapsed() < FRAME_DELAY) {
			return false;
		}
		// Advance animation frame
		index = (index + 1) % frames.length;
		updateAnimationFrame();
		timer.mark();
		return true;
	}

	/**
	 * Update this animated actor.
	 */
	public void act() {
		updateAnimation();
	}

	/**
	 * Assign a new location for this animated actor.
	 *
	 * @param x location index on the x-axis
	 * @param y location index on the y-axis
	 */
	public void setLocation(int x, int y) {
		this.x = (double) x;
		this.y = (double) y;
		super.setLocation(x, y);
	}

	/**
	 * Move this animated actor the specified distance in the direction it is currently facing.
	 *
	 * @param distance the distance to move (in cell-size units); a negative value will move backwards
	 */
	public void move(int distance) {
		double rotation = Math.toRadians(getRotation());
		x += Math.cos(rotation) * (double) distance;
		y += Math.sin(rotation) * (double) distance;
		super.setLocation((int) x, (int) y);
	}
}
