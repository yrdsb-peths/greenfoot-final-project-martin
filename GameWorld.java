import greenfoot.*;
import java.util.LinkedList;

/**
 * The world in which the core Super Spider game runs.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class GameWorld extends World {
	private static final int MAX_WEB_COUNT = 3;

	private Web currentWeb = null;
	private LinkedList<Web> webs;
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
		webs = new LinkedList<Web>();
	}

	/**
	 * Create a new web and add it to this world and its list of webs.
	 *
	 * @param x the x position of the new web
	 * @param y the y position of the new web
	 */
	private void addWeb(int x, int y) {
		currentWeb = new Web(x, y);
		addObject(currentWeb, 0, 0);
		// Prevent the player from making unlimited webs; remove the oldest after reaching the max
		if (webs.size() >= MAX_WEB_COUNT) {
			fadeWebAway(webs.getFirst());
		}
		webs.add(currentWeb);
	}

	/**
	 * Start fading a web away and remove it from this world's list of webs.
	 *
	 * @param web the web to fade away
	 */
	public void fadeWebAway(Web web) {
		webs.remove(web);
		web.fadeAway();
	}

	/**
	 * Create webs when the mouse is dragged.
	 */
	public void act() {
		// Create new webs when the mouse is pressed
		if (Greenfoot.mousePressed(null)) {
			MouseInfo mouse = Greenfoot.getMouseInfo();
			addWeb(mouse.getX(), mouse.getY());
		}
		// Update the current web while dragging the mouse
		if (Greenfoot.mouseDragged(null)) {
			MouseInfo mouse = Greenfoot.getMouseInfo();
			currentWeb.drag(mouse.getX(), mouse.getY());
		}
		// Lock in the current web after a mouse drag has ended
		if (Greenfoot.mouseDragEnded(null)) {
			currentWeb.lockIn();
			currentWeb = null;
		}
	}
}
