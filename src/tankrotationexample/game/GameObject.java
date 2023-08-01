package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class GameObject {

    public static GameObject newInstance(String type, float x, float y) throws UnsupportedOperationException {

        return switch (type) {
            case "9", "3" -> new Wall(x, y, ResourceManager.getSprite("unbreak"));
            default -> throw new UnsupportedOperationException();
        };
    }

    public abstract void drawImage(Graphics buffer);
}
