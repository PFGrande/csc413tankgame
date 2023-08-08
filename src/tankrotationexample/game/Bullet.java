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
    private Rectangle hitbox;
    private boolean collisionStatus;

    public Bullet(float x, float y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.img = sprite;
        this.vx = 2;
        this.vy = 2;
        this.hitbox = new Rectangle((int) x, (int) y, this.img.getWidth()*2, this.img.getHeight()*2);
    }

    void update() {
        this.vx = Math.round(this.R * Math.cos(Math.toRadians(angle)));
        this.vy = Math.round(this.R * Math.sin(Math.toRadians(angle)));
        this.x += this.vx;
        this.y += this.vy;
        checkBorder();
        this.hitbox.setLocation((int)this.x, (int)this.y);
    }

    private void checkBorder() { // game screen measurements should be changed to game world measurements
        if (x < 30) {
            x = 30;
        } // 46 = wall img size + (bullet img size *2)
        if (x >= GameConstants.GAME_WORLD_WIDTH - 46) {
            x = GameConstants.GAME_WORLD_WIDTH - 46;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 46) {
            y = GameConstants.GAME_WORLD_HEIGHT - 46;
        }
    }

    //called when shoot is pressed. Spawns bullet at position
    public void spawnBullet(float x, float y, float angle) { // helper for getting bullet from resource pool
        this.x = x+17; // 17 = tank position relative to top left + (tank img width/2 - (bullet img width * 2) (might be /2)
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



    public boolean getCollisionStatus() { // used to remove bullets from list
        return collisionStatus;
    }

    public void setCollisionStatus(boolean collisionStatus) { // used in other classes to update the status
        this.collisionStatus = collisionStatus;
    }
    @Override
    public Rectangle getHitbox() {
        return hitbox.getBounds();
    }

    @Override
    public void collides(GameObject with) {
        System.out.println("HIT");
        //play animation
    }
}
