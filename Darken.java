import greenfoot.*;

/**
 * An actor that will darken everything appearing under it.
 *
 * @author Martin Baldwin
 * @version June 2022
 */
public class Darken extends Actor {
    private static final Color COLOR = new Color(0, 0, 0, 128);

    /**
     * Initialize this darken when added to a world.
     */
    protected void addedToWorld(World world) {
        // Create an image the size of the world filled with the colour
        GreenfootImage image = new GreenfootImage(world.getWidth(), world.getHeight());
        image.setColor(COLOR);
        image.fill();
        setImage(image);
        // Centre this actor so that it covers the whole world
        setLocation(world.getWidth() / 2, world.getHeight() / 2);
    }
}
