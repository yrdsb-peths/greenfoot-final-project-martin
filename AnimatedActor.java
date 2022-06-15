import greenfoot.*;

/**
 * An actor class adding an animation system.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public abstract class AnimatedActor extends Actor {
	public static final int DEFAULT_FRAME_DELAY = 250;
	// Number of steps to decrease 'transparency' (really opacity) each act cycle while fading away
	private static final int FADE_INTERVAL = 4;

	private GreenfootImage[] frames;
	private int index;
	private int delay = DEFAULT_FRAME_DELAY;
	private int lifespan;
	private int transparency = 255;
	private boolean willFade = false;
	private SimpleTimer timer = new SimpleTimer();
	private SimpleTimer fadeTimer = new SimpleTimer();

	// Precise location for fine movement
	private double x;
	private double y;

	/**
	 * Set this actor's animation frame delay.
	 *
	 * @param delay the number of milliseconds to delay between each animation frame
	 */
	protected void setFrameDelay(int delay) {
		this.delay = delay;
	}

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
	 * Return the array of animation frames that this actor is currently using.
	 */
	protected GreenfootImage[] getAnimation() {
		return frames;
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
	 * Advance this actor's animation and update its image accordingly.
	 *
	 * @return true if the animation was advanced, false otherwise
	 */
	protected boolean updateAnimation() {
		if (timer.millisElapsed() < delay) {
			return false;
		}
		// Advance animation frame
		index = (index + 1) % frames.length;
		updateAnimationFrame();
		timer.mark();
		return true;
	}

	/**
	 * Set this actor's lifespan.
	 *
	 * @param lifespan the number of milliseconds for this actor to exist before fading away
	 */
	protected void setLifespan(int lifespan) {
		this.lifespan = lifespan;
		willFade = true;
	}

	/**
	 * Start fading away this actor now.
	 */
	protected void fadeAway() {
		lifespan = 0;
		willFade = true;
	}

	/**
	 * Start timing this actor's life.
	 */
	protected void startLifespan() {
		fadeTimer.mark();
	}

	/**
	 * Return whether or not this animated actor is currently fading away.
	 */
	public boolean isFading() {
		return willFade && fadeTimer.millisElapsed() >= lifespan;
	}

	/**
	 * Fade this animated actor away and remove it after its lifespan has passed.
	 */
	protected void updateFade() {
		if (!isFading()) {
			return;
		}
		// Gradually decrease the image 'transparency' (really opacity) before removing this actor
		transparency -= FADE_INTERVAL;
		if (transparency <= 0) {
			// Remove this actor once it has become fully transparent
			getWorld().removeObject(this);
			return;
		}
		// Set the transparency on a copy of the original image so the change is not permanent
		GreenfootImage image = new GreenfootImage(getImage());
		image.setTransparency(transparency);
		setImage(image);
	}

	/**
	 * Update this animated actor.
	 */
	public void act() {
		if (((GameWorld) getWorld()).isOver()) {
			return;
		}
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
