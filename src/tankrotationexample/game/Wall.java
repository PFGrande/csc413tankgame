package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    private BufferedImage img;
    private float x;
    private float y;

    public Wall(float x, float y, BufferedImage sprite) {
        this.img = sprite;
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int)x, (int)y, null);

    }
}
