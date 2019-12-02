package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Asteroid extends GameObject {

    public Asteroid(double x, double y) {
        super(ImageResource.getImage(ImageResource.ASTEROID), x, y);
        setCollisionRadius(getImage().getWidth() /2);
    }

}
