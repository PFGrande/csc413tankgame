package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;

    List<GameObject> gobjs = new ArrayList<>();
    //private long tick = 0; // for tick logic, not necessary to be used.
    List<Animation> anim = new ArrayList<>();

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() { // check collisions here
        try {
            while (true) {
                //this.tick++;
                this.t1.update(); // update tank
                this.t2.update();
                this.repaint();   // redraw game, never call paint component directly; repaint happens on different thread
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144); // artificially slow game down to prevent it from updating too fast
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() { // reminder to add tank 2 to this
        // spawn powerups and walls
        //this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
//        this.world = new BufferedImage(
//                GameConstants.GAME_SCREEN_WIDTH,
//                GameConstants.GAME_SCREEN_HEIGHT,
//                BufferedImage.TYPE_INT_RGB); // floor image
        this.world = new BufferedImage(
                GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB); // floor image

        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream("maps/TankMapConverted.csv")));


        // 0 = empty space
        // 9 = unbreakable barrier
        // 3 = unbreakable wall, collidable
        // 3-6 = power up
        // 8 = breakable wall

        // assume csv file follows the proper format (included in txt file)
        try (BufferedReader mapReader = new BufferedReader(isr)) { // cycle through rows and columns to import map objs
            int row = 0;
            String[] gameItems;

            while (mapReader.ready()) {
                gameItems = mapReader.readLine().strip().split(",");

                for (int column = 0; column < gameItems.length; column++) {
//                    System.out.println(gameItems[column]); // debugging
                    String gameObj = gameItems[column];
                    if ("0".equals(gameObj)) continue; // skip over 0s
                    this.gobjs.add(GameObject.newInstance(gameObj, column*30, row*30));
                    // position objects using the row and column values


                }
                row++;
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        t1 = new Tank(300, 300, 0, ResourceManager.getSprite("tank1"));
        t2 = new Tank(300, 300, 0, ResourceManager.getSprite("tank2"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_N);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);

//        anim.add(new Animation(ResourceManager.getAnimation()))

    }

    /*
    game object doesn't need to know about the objects it doesnt support, only those it does support
    in the try catch, 0 should be checked for instead of doing it in the game objects.
        Distinguished what is related to worlloader and game object. Because 0 isn't a game object,
        it shouldn't be in the GameObject class

     resource pool: prevent constant object creation in java
     synchronized list: ?


     */

//    InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getClassLoader()));
//    try (BufferedReader mapReader = new BufferedReader(isr)) {
//        int row = 0;
//        String[] gameItems;
//        while (mapReader.ready()) {
//            gameItems = mapReader.readLine().strip().split(regex: ",")
//        }
//
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }

//    public void renderFloor(Graphics g) {
//        //g.drawImage(ResourceManager.getInstance(), 0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
//        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i+=320) { // faster, might have to do with scaling
//            for (int j = 0; k < GameConstants.GAME_WORLD_HEIGHT; j+=240) {
//                g.drawImage(ResourceManager.getSprite("floor"), i , j, null);
//            }
//
//        }
//
//    }

    static double miniMapScaleFactor = 0.2;
    public void renderMiniMap(Graphics2D g) {
        /*
        Goal:
        - draw square
        - position square
        -
         */
        //System.out.println("BREAKS1");
        //System.out.println();
        BufferedImage mm = this.world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH-1, GameConstants.GAME_WORLD_HEIGHT-1);
        g.drawImage(mm, 0, 0, null);
        g.scale(.2,.2);
        g.drawImage(mm, 0, 0, null);

//        var mmX = GameConstants.GAME_SCREEN_WIDTH/2 - GameConstants.GAME_WORLD_WIDTH*(miniMapScaleFactor)/2;
//        var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*miniMapScaleFactor) - 35;
//        System.out.println("BREAKS2");
//        AffineTransform mmTransform = AffineTransform.getTranslateInstance(mmX, mmY);
//        g.drawImage(mm, mmTransform, null);
//        System.out.println("BREAKS3");

        //ImageObserver(); // checks for changes in objects?
    }
//    public void renderSplitScreen(Graphics2D g) { // using get subimage, get equally sized sctions of the map for the cameras
//        BufferedImage lh = this.world.getSubimage(this.t1.getX(), this.t1.getY(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
//        BufferedImage lh = this.world.getSubimage(this.t1.getX(), this.t1.getY(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
//
//    }


    private void drawFloor(Graphics g) {
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i+= 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+= 240) {
                g.drawImage(ResourceManager.getSprite("floor"), i, j, null);
            }
        }



    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics(); // all game objects draw to this buffer

        this.drawFloor(buffer);
        //System.out.println(world.getHeight());
        //System.out.println(world.getWidth());

        //placeholder code to get rid of trail with rendering black background
        //buffer.setColor(Color.black);
        //buffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);

        //this.anim.forEach(animation -> animation.update());
        //this.anim.forEach(animation -> animation.drawImage(buffer));

        renderMiniMap(g2); // renders it to the screen instead of the gameScreen. Drawing to game screen draws map inside map.

        g2.drawImage(world, 0, 0, null);
    }
}
