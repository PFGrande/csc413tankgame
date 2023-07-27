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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;

    private final BufferedImage floor =  ResourceManager.getSprite("floor");
    //private long tick = 0; // for tick logic, not necessary to be used.

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
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
        this.world = new BufferedImage(GameConstants.GAME_SCREEN_WIDTH,
                GameConstants.GAME_SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);


        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank1"));
        t2 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank2"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_N);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
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


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics(); // all game objects draw to this buffer

        //placeholder code to get rid of trail with rendering black background
        buffer.setColor(Color.black);
        buffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        g2.drawImage(world, 0, 0, null);
    }
}
