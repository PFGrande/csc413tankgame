package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class GameObject {

    public static GameObject newInstance(String type, float x, float y) throws UnsupportedOperationException {
        // add spawn points to map
        // add method to tank that randomly chooses spawn point when it dies
        return switch (type) {
            case "9", "3" -> new Wall(x, y, ResourceManager.getSprite("unbreak"));
            case "4" -> new SpeedPowerUp(x, y, ResourceManager.getSprite("speed"));
            default -> throw new UnsupportedOperationException();
        };
    }

    public abstract void drawImage(Graphics buffer);

    public abstract Rectangle getHitbox();

    public abstract void collides(GameObject with);
}
