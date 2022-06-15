import greenfoot.*;
import java.util.LinkedList;

/**
 * The world in which the core Super Spider game runs.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class GameWorld extends World {
	private static final int SPRAY_INTERVAL = 10000;
	private static final int COIN_INTERVAL = 10000;
	private static final GreenfootSound MUSIC_INTRO_SOUND = new GreenfootSound("sounds/new-super-mario-bros-wii-battle-intro.mp3");
	private static final GreenfootSound MUSIC_SOUND = new GreenfootSound("sounds/new-super-mario-bros-wii-battle.mp3");
	private static final GreenfootSound COIN_SOUND = new GreenfootSound("sounds/new-super-mario-bros-wii-coin.mp3");

	private Spider spider;
	private Web currentWeb = null;
	private int dragX;
	private int dragY;

	private boolean isOver = false;
	private int score = 0;
	private Label scoreLabel = new Label(score, 50);
	private SimpleTimer timer = new SimpleTimer();
	private SimpleTimer sprayTimer = new SimpleTimer();
	private SimpleTimer coinTimer = new SimpleTimer();
	private Label timerLabel = new Label(0, 50);

	private GreenfootSound music = MUSIC_INTRO_SOUND;

	private Darken darken = new Darken();
	private Label pausedLabel = new Label("Paused", 100);

	/**
	 * Create a new game world.
	 */
	public GameWorld(GameMode mode) {
		// Create a new world with 600x400 cells with a cell size of 1x1 pixels.
		super(600, 400, 1, false);
		// Stop music if it was playing from a previous game
		MUSIC_SOUND.stop();
		setPaintOrder(Button.class, Label.class, Darken.class, Heart.class, Glass.class, GlassShadow.class, Gas.class, Spray.class, Spider.class, Coin.class, Web.class);
		GreenfootImage image = getBackground();
		image.setColor(new Color(128, 128, 128));
		image.fill();
		spider = new Spider();
		addObject(spider, 300, 200);
		createInitialWeb();
		if (mode == GameMode.HARD) {
			addObject(new Glass(), 650, 450);
		}
		addObject(scoreLabel, 25, 25);
		addObject(timerLabel, 550, 25);
		music.play();
	}

	/**
	 * Return this game's spider.
	 */
	public Spider getSpider() {
		return spider;
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
	 * Create a new web and add it to this world.
	 *
	 * @param x the x position of the new web
	 * @param y the y position of the new web
	 */
	private void addWeb(int x, int y) {
		currentWeb = new Web(x, y);
		addObject(currentWeb, 0, 0);
	}

	/**
	 * Remove a coin from this world, increase the score, and play the sound effect.
	 *
	 * @param coin the coin to collect
	 */
	public void collectCoin(Coin coin) {
		removeObject(coin);
		score++;
		scoreLabel.setValue(score);
		COIN_SOUND.play();
	}

	/**
	 * Place some number of hearts on screen.
	 *
	 * @param count the number of hearts to add
	 */
	public void updateHearts(int count) {
		// Remove all existing hearts
		removeObjects(getObjects(Heart.class));
		// Add new hearts in a vertical line
		for (int i = 0; i < count; i++) {
			addObject(new Heart(), 50, 150 + i * 75);
		}
	}

	/**
	 * End this game and show the game over screen.
	 */
	public void gameOver() {
		isOver = true;
		addObject(darken, 0, 0);
		addObject(new Label("Game Over", 100), 300, 150);
		addObject(new Button("Easy", 50, Color.BLACK, new Color(160, 255, 160), new Color(144, 224, 144), new EasyCallback()), 200, 300);
		addObject(new Button("Hard", 50, Color.BLACK, new Color(255, 160, 160), new Color(224, 144, 144), new HardCallback()), 400, 300);
	}

	/**
	 * Return whether or not this game is over.
	 */
	public boolean isOver() {
		return isOver;
	}

	/**
	 * Update this game.
	 */
	public void act() {
		if (isOver) {
			return;
		}

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
		if (sprayTimer.millisElapsed() >= SPRAY_INTERVAL) {
			addObject(new Spray(), 0, 0);
			sprayTimer.mark();
		}
		// Add new coins every once in a while
		if (coinTimer.millisElapsed() >= COIN_INTERVAL) {
			int x = Greenfoot.getRandomNumber(getWidth());
			int y = Greenfoot.getRandomNumber(getHeight());
			addObject(new Coin(), x, y);
			coinTimer.mark();
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

		// Play the looping background music after the intro is finished
		if (!music.isPlaying()) {
			music = MUSIC_SOUND;
			music.playLoop();
		}
	}

	/**
	 * Pause the background music and display the paused screen.
	 */
	public void stopped() {
		music.pause();
		// Don't display the paused screen if the game over screen is shown
		if (!isOver) {
			addObject(darken, 0, 0);
			addObject(pausedLabel, 300, 200);
		}
	}

	/**
	 * Resume the background music and remove the paused screen.
	 */
	public void started() {
		if (music == MUSIC_INTRO_SOUND) {
			music.play();
		} else {
			music.playLoop();
		}
		if (!isOver) {
			removeObject(darken);
			removeObject(pausedLabel);
		}
	}
}
