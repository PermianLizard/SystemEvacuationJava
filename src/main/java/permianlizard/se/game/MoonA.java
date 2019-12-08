package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class MoonA extends Moon {

    public MoonA(double x, double y) {
        super(ImageResource.getImage(ImageResource.MOON_48_1), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setMass(640000);
    }

    public void onCollide(GameObject other) {

    }
}
