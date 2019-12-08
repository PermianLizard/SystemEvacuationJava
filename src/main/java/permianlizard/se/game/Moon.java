package permianlizard.se.game;

import java.awt.image.BufferedImage;

public abstract class Moon extends GameObject {

    public Moon(BufferedImage image, double x, double y) {
        super(image, x, y);
        setMass(640000);
        setGravityRadius(300);
    }

    public void onCollide(GameObject other) {

    }
}
