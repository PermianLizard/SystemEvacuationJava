package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Sun extends GameObject {

    public Sun(double x, double y) {
        super(ImageResource.getImage(ImageResource.SUN), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setMass(1280000);
        setStaticObject(true);
        setDestructible(false);
        setGravityRadius(800);
    }

    public void onCollide(GameObject other) {

    }
}
