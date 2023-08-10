package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

// Note to self: when the professor says work with abstractions at higher level class, I believe he refers to abstracting other classes
// to make the highest level class work almost sole on abstractions

public class Bullet extends GameObject implements MovableObjects {
    private float x;
    private float y;
    private float vx;
    private float vy;
    BufferedImage img;
    private float angle;
    private float R = 5;
    private int owner;
    private Rectangle hitbox;
    private boolean hasCollided = false;


    public Bullet(float x, float y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.img = sprite;
        this.vx = 2;
        this.vy = 2;
        this.hitbox = new Rectangle((int) x, (int) y, this.img.getWidth()*2, this.img.getHeight()*2);
    }

    public void update() {
        this.vx = Math.round(this.R * Math.cos(Math.toRadians(angle)));
        this.vy = Math.round(this.R * Math.sin(Math.toRadians(angle)));
        this.x += this.vx;
        this.y += this.vy;

        this.hitbox.setLocation((int)this.x, (int)this.y);
    }

    private void checkBorder() { // game screen measurements should be changed to game world measurements
        if (x < 30) {
            x = 30;
            this.hasCollided = true;
        } // 46 = wall img size + (bullet img size *2)
        if (x >= GameConstants.GAME_WORLD_WIDTH - 46) {
            x = GameConstants.GAME_WORLD_WIDTH - 46;
            this.hasCollided = true;
        }
        if (y < 30) {
            y = 30;
            this.hasCollided = true;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 46) {
            y = GameConstants.GAME_WORLD_HEIGHT - 46;
            this.hasCollided = true;
        }
    }

    //called when shoot is pressed. Spawns bullet at position
    public void spawnBullet(float x, float y, float angle, int owner) { // helper for getting bullet from resource pool

        this.x = x+17; // 17 = tank position relative to top left + (tank img width/2 - (bullet img width * 2) (might be /2)
        this.y = y+15;
        this.angle = angle;
        this.owner = owner;
    }

    public int getOwner() {
        return this.owner;
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.scale(2,2);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);

    }

    @Override
    public Rectangle getHitbox() {
        return hitbox.getBounds();
    }

    @Override
    public void collides(GameObject with) {

        if (with instanceof Tank) { // object collision with tank
            System.out.println(with.getClass());
            hasCollided = ((Tank) with).getId() != this.owner;
        }

        if (with instanceof Wall) {
            hasCollided = true;
        }
    }

    @Override
    public boolean expired() {
        return hasCollided;
    }
}
