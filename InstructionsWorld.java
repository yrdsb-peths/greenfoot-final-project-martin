import greenfoot.*;

/**
 * A screen to display instructions on how to play the Super Spider game.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class InstructionsWorld extends World {
	/**
	 * Create a new instructions world.
	 */
	public InstructionsWorld() {
		// Create a new world with 600x400 cells with a cell size of 1x1 pixels.
		super(600, 400, 1);
        GreenfootImage image = getBackground();
        image.setColor(new Color(160, 160, 255));
        image.fill();
        addObject(new Label(
            "You are a spider. Move with WASD.\n" +
            "Click and drag to create a web.\n" +
            "\n" +
            "The larger the web, the shorter it\n" +
            "lasts. You must stay on a web at all\n" +
            "times.", 40), 300, 150);
        addObject(new Button("Done", 30, Color.BLACK, new Color(160, 255, 160), new Color(144, 224, 144), new DoneCallback()), 300, 350);
	}
}

/**
 * The callback for the done button, which returns to the title screen.
 */
class DoneCallback implements Callback {
	public void run() {
		Greenfoot.setWorld(new TitleWorld());
	}
}
