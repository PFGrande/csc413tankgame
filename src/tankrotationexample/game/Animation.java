package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {
    private float x, y;
    private int currentFrame = 0;
    private final List<BufferedImage> frames;
    private long timeSinceLastUpdate;
    private long delay = 40;
    private boolean isRunning;

    public Animation(float x, float y, List<BufferedImage> frames) {
        this.frames = frames;
        this.x = x;
        this.y = y;
        timeSinceLastUpdate = 0;
        isRunning = true;
        currentFrame = 0;
    }

    public void update() {
        if (this.timeSinceLastUpdate + this.delay < System.currentTimeMillis()) {
            this.timeSinceLastUpdate = System.currentTimeMillis();
            this.currentFrame++;

            this.currentFrame = (this.currentFrame + 1) % this.frames.size(); // infinite loop

//            if (this.currentFrame == this.frames.size()) {
//                isRunning = false;
//            }
        }
    }

    public void drawImage(Graphics2D g) {
        if (isRunning) {
            g.drawImage(frames.get(this.currentFrame), (int)x, (int)y, null);
        }
    }

}
