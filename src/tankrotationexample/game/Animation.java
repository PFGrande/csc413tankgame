package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation extends GameObject{
    private float x, y;
    private int currentFrame = 0;
    private List<BufferedImage> frames;
    private long timeSinceLastUpdate;
    private long delay = 40; // milliseconds
    private boolean isRunning = false; // false = stops running
    Rectangle hitbox = new Rectangle((int)this.x, (int)this.y, 1,1);

    public Animation(float x, float y, List<BufferedImage> frames) {
        this.frames = frames;
        this.x = x;
        this.y = y;
        timeSinceLastUpdate = 0;
        isRunning = true;
        currentFrame = 0;

    }

    public boolean update() {

        if (this.timeSinceLastUpdate + this.delay < System.currentTimeMillis()) {
            this.timeSinceLastUpdate = System.currentTimeMillis();
            this.currentFrame++;


            //this.currentFrame = (this.currentFrame + 1) % this.frames.size(); // infinite loop
        }
        return currentFrame < this.frames.size();
    }

//    public void drawImage(Graphics2D g) {
//        if (isRunning) {
//            g.drawImage(this.frames.get(this.currentFrame), (int)x, (int)y, null);
//        }
//    }

    @Override
    public void drawImage(Graphics buffer) {
        if (isRunning) {
            buffer.drawImage(this.frames.get(this.currentFrame), (int)x, (int)y, null);
        }
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox.getBounds();
    }

    @Override
    public void collides(GameObject with) {
    }
}
