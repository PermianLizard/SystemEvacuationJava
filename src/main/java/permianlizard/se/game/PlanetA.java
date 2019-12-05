package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class PlanetA extends Planet {

    public PlanetA(double x, double y) {
        super(ImageResource.getImage(ImageResource.PLANET_64_1), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
    }

    public void onCollide(GameObject other) {

    }
}
