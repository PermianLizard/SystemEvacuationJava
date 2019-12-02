package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Base extends GameObject {

    public Base(double x, double y) {
        super(ImageResource.getImage(ImageResource.BASE), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
    }
}
