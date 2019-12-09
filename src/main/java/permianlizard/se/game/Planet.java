package permianlizard.se.game;

import java.awt.image.BufferedImage;

public abstract class Planet extends GameObject {

    public Planet(BufferedImage image, double x, double y) {
        super(image, x, y);
        setMass(240000);
        setStaticObject(true);
        setDestructible(false);
        setGravityRadius(500);
    }

    public void onCollide(GameObject other) {

    }
}
