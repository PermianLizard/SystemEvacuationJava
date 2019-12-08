package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Asteroid extends GameObject {

    public Asteroid(double x, double y) {
        super(ImageResource.getImage(ImageResource.ASTEROID), x, y);
        setCollisionRadius(getImage().getWidth() /2);
        setMass(400);
        setDestructible(true);
        setImpactResistance(1.3f);
        setHealthMax(15);
        setHealth(getHealthMax());
    }

    public void onCollide(GameObject other) {

    }
}
