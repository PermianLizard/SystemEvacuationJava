package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class PlanetA extends Planet {

    public PlanetA(String name, double x, double y) {
        super(ImageResource.getImage(ImageResource.PLANET_64_1), name, x, y);
        setCollisionRadius(getImage().getWidth() / 2);
    }

    public void onCollide(GameObject other) {

    }
}
