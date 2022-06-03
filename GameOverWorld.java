import greenfoot.*;

/**
 * The world that is displayed after a game ends.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class GameOverWorld extends World {
	/**
	 * Create a new game over world.
	 */
	public GameOverWorld() {
		// Create a new world with 600x400 cells with a cell size of 1x1 pixels.
		super(600, 400, 1);
		Label gameOverLabel = new Label("Game Over", 100);
		gameOverLabel.setFillColor(Color.BLACK);
		addObject(gameOverLabel, 300, 200);
	}
}
