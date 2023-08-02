package tankrotationexample.Resources;

import tankrotationexample.game.Bullet;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<String, List<BufferedImage>>();;
    //private final static Map<String, Sound> sounds = new HashMap<String, Clip>();
    private static final Map<String, Integer> animationInfo = new HashMap<String, Integer>() {{
        put("bullethit", 24);
        put("bulletshoot", 24);
        put("powerpick", 32);
        put("puffsmoke", 32);
        put("rocketflame", 16);
        put("rockethit", 32);
    }};

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
        //ResourceManager.initAnimations();
//        ResourceManager.initSounds();

        // create pool of bullets, reminder to find where it belongs
        ResourcePool<Bullet> bulletPool = new ResourcePool<>("bullet", 300);
        bulletPool.fillPool(Bullet.class, 300);
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

    private static void loadAnimations() {
        String baseName = "anmations/%s/%s-%04d.png";
        ResourceManager.animationInfo.forEach((animationName, frameCount) -> {
            List<BufferedImage> frames = new ArrayList<>();
            try {
                for (int i = 0; i < frameCount; i++) {

                    loadSprite(baseName.formatted(animationName, animationName, i));

                }
                ResourceManager.animations.put(animationName, frames);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<BufferedImage> getAnimation(String animationName) {
        return animations.get(animationName);
    }

//    private static Sound loadSound(String path) throws UnsupportedAudioFileException, IOException {
//        AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path)));
//
//        Clip c = AudioSystem;
//    }
//
//    public static void initSounds() {
//        try {
//            ResourceManager.sounds.put("shoot",loadSound("sounds/bullet.wav"));
//        }
//    }
}
