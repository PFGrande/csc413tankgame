package tankrotationexample.Resources;

import tankrotationexample.game.GameWorld;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    private final static Map<String, List<BufferedImage>> animation = new HashMap<String, List<BufferedImage>>();;

    private final static Map<String, Clip> sounds = new HashMap<String, Clip>();

    private static void initSprites() {
        //BufferedImage t = ImageIO.read(ResourceManager.class.getClassLoader().getResource("tank1.png"));
        ResourceManager.sprites.put("tank1", loadSprite("tank1.png"));





//        try {
//            return ImageIO.read(
//                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource(imgPath),
//                            "Could not find " + imgPath + ".png")
//            );
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
    }

    public static void loadResources() {
        ResourceManager.initSprites();
    }

    public static void main(String[] args) { // test assets loading
        ResourceManager.initSprites();
    }

    private static BufferedImage loadSprite(String path) {
        try {
            BufferedImage t = ImageIO.read(ResourceManager.class.getClassLoader().getResource(path));
            //ResourceManager.sprites.put("tank1", t);
        } catch (IOException e) {

        }
    }
}
