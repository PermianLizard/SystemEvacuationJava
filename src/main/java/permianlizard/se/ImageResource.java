package permianlizard.se;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImageResource {

    public static Path IMAGES_PATH = Paths.get("resources", "images");

    public static final String SHIP = "ship.png";
    public static final String EXPLOSION = "explosion.png";
    public static final String THRUST = "thrust.png";
    public static final String ASTEROID = "aster.png";
    public static final String BASE = "base.png";
    public static final String MOON_48_1 = "m48_1.png";
    public static final String PLANET_64_1 = "p64_1.png";
    public static final String PLANET_64_2 = "p64_2.png";
    public static final String SUN = "sun.png";

    private static final String[] IMAGE_FILES = {SHIP, EXPLOSION, THRUST, ASTEROID,
            BASE, MOON_48_1, PLANET_64_1, PLANET_64_2, SUN
    };

    private static final Map<String, BufferedImage> imageMap;
    private static final Map<String, BufferedImage[]> animationMap;

    static {
        imageMap = new HashMap<>(IMAGE_FILES.length);
        animationMap = new HashMap<>();
    }

    public static BufferedImage loadImage(File file) throws IOException {

        BufferedImage image = ImageIO.read(file);
        BufferedImage convertedImage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
        GraphicsDevice gd = ge.getDefaultScreenDevice ();
        GraphicsConfiguration gc = gd.getDefaultConfiguration ();
        convertedImage = gc.createCompatibleImage(image.getWidth (),
                image.getHeight (),
                image.getTransparency () );
        //convertedImage.setAccelerationPriority(1);  // FIXME: Is this useful?
        Graphics2D g2d = convertedImage.createGraphics ();
        g2d.drawImage(image, 0, 0, image.getWidth (), image.getHeight (), null );
        g2d.dispose();
        return convertedImage;
    }

    public static void loadImages() throws IOException {
         for (String imageFileName : IMAGE_FILES) {
             File imageFile = new File(IMAGES_PATH.resolve(imageFileName).toString());
             BufferedImage image = loadImage(imageFile);
             imageMap.put(imageFile.getName(), image);
         }
    }

    public static void createAnimationSequences() {
        BufferedImage[] animationSequence;
        BufferedImage image;

        animationSequence = new BufferedImage[6];
        image = imageMap.get(EXPLOSION);
        for (int i = 0; i < 6; ++i) {
            animationSequence[i] = image.getSubimage(i * 32, 0, 32, 32);
        }
        animationMap.put(EXPLOSION, animationSequence);

        animationSequence = new BufferedImage[3];
        image = imageMap.get(THRUST);
        for (int i = 0; i < 3; ++i) {
            animationSequence[i] = image.getSubimage(i * 38, 0, 38, 38);
        }
        animationMap.put(THRUST, animationSequence);
    }

    public static BufferedImage getImage(String imageName) {
        return imageMap.get(imageName);
    }

    public static BufferedImage[] getAnimation(String animationName) {
        return animationMap.get(animationName);
    }

    public static BufferedImage getAnimationFrame(String animationName, int frameNum) {
        return animationMap.get(animationName)[frameNum];
    }
}
