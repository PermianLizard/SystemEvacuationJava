package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Asteroid extends GameObject {

    public Asteroid(double x, double y) {
        super(ImageResource.getImage(ImageResource.ASTEROID), x, y);
        setCollisionRadius(getImage().getWidth() /2);
        setMass(400);
        setDestructible(true);
        setImpactResistance(2.2f);
        setHealthMax(15);
        setHealth(getHealthMax());
    }

    public Asteroid() {
        this(0, 0);
    }

    public void onCollide(GameObject other) {

    }
}
