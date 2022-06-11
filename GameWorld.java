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
	private static final int SPRAY_INTERVAL = 10000;

	private Web currentWeb = null;
	private LinkedList<Web> webs;
	private int dragX;
	private int dragY;

	private int score = 0;
	private Label scoreLabel;
	private SimpleTimer timer;
	private SimpleTimer sprayTimer;
	private Label timerLabel;

	/**
	 * Create a new game world.
	 */
	public GameWorld() {
		// Create a new world with 600x400 cells with a cell size of 1x1 pixels.
		super(600, 400, 1, false);
		setPaintOrder(Button.class, Label.class, Gas.class, Spray.class, Spider.class, Coin.class, Web.class);
		GreenfootImage image = getBackground();
		image.setColor(new Color(128, 128, 128));
		image.fill();
		addObject(new Spider(), 300, 200);
		sprayTimer = new SimpleTimer();
		webs = new LinkedList<Web>();
		createInitialWeb();
		// Initialize score, timer, and their labels
		scoreLabel = new Label(score, 50);
		addObject(scoreLabel, 25, 25);
		timer = new SimpleTimer();
		timerLabel = new Label(0, 50);
		addObject(timerLabel, 550, 25);
	}

	/**
	 * Create the web that the spider is to begin a life on.
	 */
	public void createInitialWeb() {
		addWeb(200, 100);
		currentWeb.drag(400, 300);
		currentWeb.lockIn();
		currentWeb = null;
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
	 * Remove a coin from this world and increase the score.
	 *
	 * @param coin the coin to collect
	 */
	public void collectCoin(Coin coin) {
		removeObject(coin);
		score++;
		scoreLabel.setValue(score);
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

		// Add new sprays every once in a while
		if (sprayTimer.millisElapsed() > SPRAY_INTERVAL) {
			addObject(new Spray(), 0, 0);
			sprayTimer.mark();
		}

		// Draw time
		int time = timer.millisElapsed();
		String minutes = String.valueOf(time / 60000);
		String seconds = String.valueOf(time / 1000 % 60);
		// Add leading zero if seconds is only one digit
		if (seconds.length() < 2) {
			seconds = "0" + seconds;
		}
		timerLabel.setValue(minutes + ":" + seconds);
	}
}
