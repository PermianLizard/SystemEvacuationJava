package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class PlanetB extends Planet {

    public PlanetB(double x, double y) {
        super(ImageResource.getImage(ImageResource.PLANET_64_2), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
    }

    public void onCollide(GameObject other) {

    }
}
