package tankrotationexample.game;

import java.awt.*;

public class ShieldEntity extends GameObject implements MovableObjects {

    private float x;
    private float y;
    private int width;
    private int height;
    private Tank owner;

    public ShieldEntity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawImage(Graphics buffer) {
        buffer.setColor(Color.ORANGE);
        buffer.drawOval((int)this.x, (int)this.y, this.getHitbox().width+30, this.getHitbox().height+30);
    }

    

    @Override
    public Rectangle getHitbox() {
        return null;
    }

    @Override
    public void collides(GameObject with) {

    }

    @Override
    public void update() {
        this.x = owner.getX();
        this.y = owner.getY();
    }

    @Override
    public boolean expired() {
        return false;
    }
}
