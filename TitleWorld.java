import greenfoot.*;

/**
 * The world that displays the game title screen.
 *
 * @author Martin Baldwin
 * @version May 2022
 */
public class TitleWorld extends World {
	/**
	 * Create a new title screen world.
	 */
	public TitleWorld() {
		// Create a new world with 600x400 cells with a cell size of 1x1 pixels.
		super(600, 400, 1);
		Label titleLabel = new Label("Super Spider", 100);
		titleLabel.setFillColor(Color.BLACK);
		addObject(titleLabel, 300, 100);
		addObject(new Button("Easy", 50, Color.BLACK, new Color(160, 255, 160), new Color(144, 224, 144), new EasyCallback()), 300, 200);
		addObject(new Button("Hard", 50, Color.BLACK, new Color(255, 160, 160), new Color(224, 144, 144), new HardCallback()), 300, 300);
	}
}

/**
 * The callback for the easy mode button, which starts the game in easy mode.
 */
class EasyCallback implements Callback {
	public void run() {
		Greenfoot.setWorld(new GameWorld(GameMode.EASY));
	}
}

/**
 * The callback for the hard mode button, which starts the game in hard mode.
 */
class HardCallback implements Callback {
	public void run() {
		Greenfoot.setWorld(new GameWorld(GameMode.HARD));
	}
}
