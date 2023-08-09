package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourcePool;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject{ // normally player and tank are seperated
    //private float cameraX, cameraY;
    private static ResourcePool<Bullet> bulletPool = new ResourcePool<>("bullet", 300);
    static {
        bulletPool.fillPool(Bullet.class, 300);
    }
    private int lives = 3;
    List<Bullet> ammo = new ArrayList<>();
    static int count = 0;
    int id;
    private float x;
    private float y;
    private float vx = 0; // change in x
    private float vy = 0; // change in y
    private float angle; // way tank is facing

    private float R = 5; // movement speed
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;
    private long timeSinceLastShot = 0L;
    private long cooldown = 2000;
    Rectangle hitbox;

    Tank(float x, float y, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        count++;
        this.id = count;
        this.hitbox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());

    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void update() {
        //System.out.println(vx);
        //System.out.println(Math.toRadians(Math.cos(angle)));

        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.shootPressed && ((this.timeSinceLastShot + this.cooldown) < System.currentTimeMillis())) {
            this.timeSinceLastShot = System.currentTimeMillis();
            Bullet temp = bulletPool.getResource();
            temp.spawnBullet(this.x, this.y, this.angle);
            this.ammo.add(temp);

        }
        this.ammo.forEach(Bullet::update);

        this.hitbox.setLocation((int)this.x, (int)this.y);

    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
    }

    private void moveForwards() {
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

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        if (!this.ammo.isEmpty()) {
            this.ammo.forEach(b -> b.drawImage(g2d));
        }
        g2d.setColor(Color.WHITE);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

        g2d.drawRect((int)x-30,(int)y-20,100, 15); // place cooldown bar above tank
        long currentWidth = 100 - ((this.timeSinceLastShot + this.cooldown) - System.currentTimeMillis())/20;
        if (currentWidth > 100) {
            currentWidth = 100;
        }
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int)x-30, (int)y-20, (int)currentWidth, 15);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public BufferedImage cameraPosition(BufferedImage world) {
        int cameraX, cameraY;
        BufferedImage screenSide;

        //check for x axis border
        if ((int) this.x <= GameConstants.GAME_SCREEN_WIDTH/4) { // checks if too far left
            cameraX = GameConstants.GAME_SCREEN_WIDTH/4;
        } else if ((int) this.x >= (3*(GameConstants.GAME_SCREEN_WIDTH/4))+((GameConstants.GAME_SCREEN_WIDTH/4)*2)) { // checks for right edge
            cameraX = (3*(GameConstants.GAME_SCREEN_WIDTH/4))+((GameConstants.GAME_SCREEN_WIDTH/4)*2);
        } else { // default behavior (follow tank)
            cameraX = (int) this.x;
        }

        //check for y axis border
        if ((int) this.y <= GameConstants.GAME_SCREEN_HEIGHT/2) { // checks if too high up
            //System.out.println("too high");
            cameraY = GameConstants.GAME_SCREEN_HEIGHT/2;
        } else if (this.y >= GameConstants.GAME_SCREEN_HEIGHT) {
            cameraY = GameConstants.GAME_SCREEN_HEIGHT;
        } else {
            cameraY = (int) this.y;
        }

        screenSide = world.getSubimage(
                cameraX - GameConstants.GAME_SCREEN_WIDTH/4,
                cameraY - GameConstants.GAME_SCREEN_HEIGHT/2,
                GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);

        return screenSide;
    }

    public void toggleShootPressed() {
        shootPressed = true;
    }

    public void untoggleShootPressed() {
        shootPressed = false;
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox.getBounds();
    }

    @Override
    public void collides(GameObject with) {
        if (with instanceof Bullet) {
            lives--;
        } else if (with instanceof Wall) {
            // boost pad code:
//            this.x = this.x - 2f;
//            this.vx =0;

            //System.out.println(vx);
            int temp = 0;

            // CLEAN UP THIS CODE LATER MAKE IT A METHOD
            if (vx < 0) {
//                temp = -1;
                if (this.UpPressed) {
                    temp = -1;
                    unToggleUpPressed();
                } else if (this.DownPressed) {
                    temp=1;
                    unToggleDownPressed();
                }
                this.x -=10*temp;
            } else if (vx > 0) {
                if (this.UpPressed) {
                    temp = 1;
                    unToggleUpPressed();
                } else if (this.DownPressed) {
                    temp = -1;
                    unToggleDownPressed();
                }
                //temp = 1;
                this.x -=10*temp;
            }

//            this.vx = 0;


            if (vy < 0) {
//                temp = -1;
                if (this.UpPressed) {
                    temp = 1;
                    unToggleUpPressed();
                } else if (this.DownPressed) {
                    temp=-1;
                    unToggleDownPressed();

                }
                this.y -=10*temp;
            } else if (vy > 0) {
                if (this.UpPressed) {
                    temp = 1;
                    unToggleUpPressed();
                } else if (this.DownPressed) {
                    temp = -1;
                    unToggleDownPressed();
                }
                //temp = 1;
                this.y -=10*temp;
            }

//            this.vy = 0;








//
//            System.out.println("TANK:   " + this.x);
//            System.out.println("WALL:   " + ((Wall) with).getX());
//            System.out.println("DIFFERENCE   :  " + ((((Wall) with).getX() - this.x)));
//
//            System.out.println("NEW POS:  " + (this.x - (((Wall) with).getX() - this.x)));
//
//            Rectangle collisionAt = hitbox.intersection(((Wall) with).hitbox);
//            this.x = this.x -  (((Wall) with).getX() - this.x);




            //S
            // ystem.out.println(vx);
            //System.out.println(collisionAt.x);
            //System.out.println(collisionAt.y);


            //this.x = (float) (((Wall) with).getX() - (Math.toRadians(Math.cos(angle))));
//            this.vx = (Math.round(R * Math.cos(Math.toRadians(angle))));
//            this.x -= this.vx;
//            this.vy = Math.round(R * Math.sin(Math.toRadians(angle)));
//            this.y -= this.vx;
        } else if (with instanceof PowerUp) {
            ((PowerUp) with).activatePowerUp(this);
        }

    }
}
