package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Ship extends GameObject {

    public Ship(double x, double y) {
        super(ImageResource.getImage(ImageResource.SHIP), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
    }
}
