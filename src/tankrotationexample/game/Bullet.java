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
    private float R = 5;
    private int owner;

    public Bullet(float x, float y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.img = sprite;
        this.vx = 2;
        this.vy = 2;

    }

    void update() {
        this.vx = Math.round(this.R * Math.cos(Math.toRadians(angle)));
        this.vy = Math.round(this.R * Math.sin(Math.toRadians(angle)));
        this.x += this.vx;
        this.y += this.vy;
        checkBorder();
    }

    private void checkBorder() { // game screen measurements should be changed to game world measurements
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 45) {
            x = GameConstants.GAME_WORLD_WIDTH - 45;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 45) {
            y = GameConstants.GAME_WORLD_HEIGHT - 45;
        }
    }

    //called when shoot is pressed. Spawns bullet at position
    public void spawnBullet(float x, float y, float angle) { // helper for getting bullet from resource pool
        this.x = x+50;
        this.y = y+15;
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
