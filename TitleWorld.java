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
		addObject(titleLabel, 300, 150);
		addObject(new Button("Play", 50, Color.BLACK, new Color(160, 255, 160), new Color(144, 224, 144), new PlayCallback()), 300, 250);
	}
}

/**
 * The callback for the play button, which starts the game.
 */
class PlayCallback implements Callback {
	public void run() {
		Greenfoot.setWorld(new GameWorld());
	}
}
