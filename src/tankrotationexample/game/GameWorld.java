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
import java.util.*;
import java.util.List;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    public int worldSize;

    List<GameObject> gobjs = new ArrayList<>();
    //private long tick = 0; // for tick logic, not necessary to be used.
    List<Animation> anims = new ArrayList<>();

    private boolean isRunning = true;

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }
    //Animation an = new Animation(300, 300, ResourceManager.getAnimation("bullethit"));

    @Override
    public void run() { // check collisions here
        try {
            while (isRunning) {
                //this.tick++;
//                this.t1.update(); // update tank
//                this.t2.update();
                updateObjs();
                checkCollision();
                //an.update();
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
        //this.lf.setFrame("end"); doesnt actually end run function
        this.lf.killGame();
        this.lf.setFrame("end");
    }

    public synchronized void updateObjs() {
        Iterator<GameObject> objItr = gobjs.iterator();
        GameObject currentObj;
        Queue<GameObject> toAdd = new LinkedList<>();
        Queue<GameObject> toRemove = new LinkedList<>();

        while (objItr.hasNext()) {
            currentObj = objItr.next();
            if (currentObj instanceof MovableObjects) {
                ((MovableObjects) currentObj).update();

                if (currentObj instanceof Bullet) {
                    if (((MovableObjects) currentObj).expired()) {
                        toAdd.add(((Bullet) currentObj).playExplode());
                        toRemove.add(currentObj);
                    }
                }

                if (currentObj instanceof Tank) {
                    GameObject temp = ((Tank) currentObj).addBulletToGameObjs();
                    if (temp != null) {
                        toAdd.add(temp);
                        //toAdd.add(((Tank) currentObj).playShoot());
                    }
                    if (((Tank) currentObj).expired()) {
                        gameOver();
                    }
                }

            }
            if (currentObj instanceof Animation) {
                if (!((Animation) currentObj).update()) {
                    toRemove.add(currentObj);
                }
            }

            if (currentObj instanceof PowerUp) {
                if (((PowerUp) currentObj).expired()) {
                    toRemove.add(currentObj);
                }
            }
        }

        for (GameObject toSpawn : toAdd) {
            gobjs.add(toAdd.remove());
        }
        for (GameObject toDespawn : toRemove) {
            gobjs.remove(toRemove.remove());
        }
//        for (GameObject bullet : toRemove) { // removes references to bullet from both lists so it despawns
//            gobjs.remove(toRemove.remove());
//            System.out.println("bullet removed");
//        }

    }

    private void gameOver() {
        this.isRunning = false;
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
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
        this.world = new BufferedImage(
                GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB); // floor image

        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream("maps/TankMapConverted.csv")));
        //InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream("maps/TankMapSpawns.csv"))); // random spawns map


        // 0 = empty space
        // 9 = unbreakable barrier
        // 3 = unbreakable wall, collidable
        // 4-7 = power up
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


        t1 = new Tank(500, 500, 0, ResourceManager.getSprite("tank1"));
        t2 = new Tank(500, 500, 0, ResourceManager.getSprite("tank2"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_N);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
        this.gobjs.add(t1);
        this.gobjs.add(t2);


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
        BufferedImage mm = this.world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        //g.drawImage(mm, 0, 0, null);
        g.scale(.2,.2);
        g.drawImage(mm,
                (GameConstants.GAME_SCREEN_WIDTH*5)/2 - (GameConstants.GAME_WORLD_WIDTH/2),
                (GameConstants.GAME_SCREEN_HEIGHT*5) - (GameConstants.GAME_WORLD_HEIGHT) - 140, null);

    }


    public void renderSplitScreen(Graphics2D g) {
        g.drawImage(t1.cameraPosition(world), 0, 0, null);
        g.drawImage(t2.cameraPosition(world), GameConstants.GAME_SCREEN_WIDTH/2+4, 0, null);
    }

    private void drawFloor(Graphics g) {
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i+= 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+= 240) {
                g.drawImage(ResourceManager.getSprite("floor"), i, j, null);
            }
        }



    }

    private void checkCollision() {
        //Queue<GameObject> toRemove = new LinkedList<GameObject>();

        for (int i = 0; i < gobjs.size(); i++) { // moving object collision
            GameObject obj1 = gobjs.get(i);
            //if (obj1 instanceof Wall || obj1 instanceof PowerUp) continue; // for now continue
            if (!(obj1 instanceof MovableObjects) && !(obj1 instanceof PowerUp)) continue;
            for (int j = 0; j < gobjs.size(); j++) { // other objects in world
                if (i == j) continue; // prevents checking an object from collision with itself
                GameObject obj2 = gobjs.get(j);

                if (obj1.getHitbox().intersects(obj2.getHitbox())) {
                    obj1.collides(obj2);
                }
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics(); // all game objects draw to this buffer

        this.drawFloor(buffer);

        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        //this.t1.drawImage(buffer);
        //
        // this.t2.drawImage(buffer);
        //this.anim.forEach(animation -> animation.update());
        //this.anim.forEach(animation -> animation.drawImage(buffer));
        //g2.drawImage(world, 0, 0, null);
        renderSplitScreen(g2);
        renderMiniMap(g2); // renders it to the screen instead of the gameScreen. Drawing to game screen draws map inside map.


//        renderSplitScreen(g2);
        //g2.drawImage(world, 0, 0, null);
    }

    /**
     * TODO:
     * - make GameWorld aware of bullets
     *      - Cant have bulletpool here because it would need to know when tank is shooting.
     *      - Check bullet collision with tank inside the tank class? Would allow for removing
     *          bullets from the ammo array and deleting them.
     * - Add Wall Collision Detection
     * - Remove Tank class from being directly instanciated in GameWorld
     *      - put it in the gameOBJ list through the map?
     *      - Make spawn points on map, randomly choose one before spawning tanks
     *
     */
}
