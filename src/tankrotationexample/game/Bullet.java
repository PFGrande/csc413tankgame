package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private float x;
    private float y;
    private float vx;
    private float vy;
    BufferedImage img;
    private float angle;
    private float R;
    private int owner;

    public Bullet(float x, float y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.img = sprite;
        this.vx = 0;
        this.vy = 0;

    }

    void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void checkBorder() { // game screen measurements should be changed to game world measurements
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    //called when shoot is pressed. Spawns bullet at position
    public void spawnBullet(float x, float y, float angle) { // helper for getting bullet from resource pool
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public void drawImage(Graphics g) {
        //buffer.drawImage(this.img, (int)x, (int)y, null);
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.scale(2,2);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);

    }
}
