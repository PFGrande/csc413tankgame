package tankrotationexample.game;

import tankrotationexample.GameConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author anthony-pc
 */
public class Tank{ // normally player and tank are seperated
    //private float cameraX, cameraY;
    static int count = 0;
    int id;
    private float x;
    private float y;
    private float vx = 0; // change in x
    private float vy = 0; // change in y
    private float angle; // way tank is facing

    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    Tank(float x, float y, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        count++;
        id = count;
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


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

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
            //System.out.println("AHHHHHHH");
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
}
