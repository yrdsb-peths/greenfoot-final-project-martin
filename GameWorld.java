import greenfoot.*;

/**
 * The world in which the core Super Spider game runs.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class GameWorld extends World {
	private Web currentWeb;
	private int dragX;
	private int dragY;

	/**
	 * Create a new game world.
	 */
	public GameWorld() {
		// Create a new world with 600x400 cells with a cell size of 1x1 pixels.
		super(600, 400, 1);
		setPaintOrder(Button.class, Label.class, Spider.class, Web.class);
		GreenfootImage image = getBackground();
		image.setColor(new Color(128, 128, 128));
		image.fill();
		addObject(new Spider(), 300, 200);
	}

	/**
	 * Create webs when the mouse is dragged.
	 */
	public void act() {
		if (Greenfoot.mousePressed(null)) {
			MouseInfo mouse = Greenfoot.getMouseInfo();
			currentWeb = new Web(mouse.getX(), mouse.getY());
			addObject(currentWeb, 0, 0);
		}
		if (Greenfoot.mouseDragged(null)) {
			MouseInfo mouse = Greenfoot.getMouseInfo();
			currentWeb.drag(mouse.getX(), mouse.getY());
		}
	}
}
