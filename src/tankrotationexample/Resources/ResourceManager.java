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

        try {
            ResourceManager.sprites.put("tank1", loadSprite("tank1.png"));
            ResourceManager.sprites.put("tank2", loadSprite("tank2.png"));
            ResourceManager.sprites.put("menu", loadSprite("title.png"));
            ResourceManager.sprites.put("bullet", loadSprite("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1", loadSprite("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2", loadSprite("bullet/rocket2.png"));
            ResourceManager.sprites.put("floor", loadSprite("floor/bg.bmp"));
            ResourceManager.sprites.put("unbreak", loadSprite("walls/unbreak.jpg"));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //BufferedImage t = ImageIO.read(ResourceManager.class.getClassLoader().getResource("tank1.png"));


    }

    public static void loadResources() {
        ResourceManager.initSprites();
    }

    public static void main(String[] args) { // test assets loading
        ResourceManager.initSprites();
    }

    public static BufferedImage getSprite(String type) {
        if (!ResourceManager.sprites.containsKey(type)) {
            throw new RuntimeException("%s is missing from the sprite resources".formatted(type));
        }

        return ResourceManager.sprites.get(type);
    }

    private static BufferedImage loadSprite(String path) throws IOException {

        return ImageIO.read(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path), "Unable to find image at path: %s".formatted(path)));
    }
}
