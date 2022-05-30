import greenfoot.*;

/**
 * The main character of the game, controlled by the player.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class Spider extends Actor {
	private static final int SPEED = 2;

	public Spider() {
		setImage(new GreenfootImage("images/spider-idle-0.png"));
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
	 * Update this spider.
	 */
	public void act() {
		updateLocation();
	}
}
