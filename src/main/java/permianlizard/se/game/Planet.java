package permianlizard.se.game;

import java.awt.image.BufferedImage;

public abstract class Planet extends GameObject {
    private String name;

    public Planet(BufferedImage image, String name, double x, double y) {
        super(image, x, y);
        setMass(240000);
        setStaticObject(true);
        setDestructible(false);
        setGravityRadius(500);
        setName(name);
    }

    public void onCollide(GameObject other) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
