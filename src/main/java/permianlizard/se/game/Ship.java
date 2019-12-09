package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Ship extends GameObject {

    private final float rotationSpeed;
    private final float thrustForce;

    public Ship(double x, double y) {
        super(ImageResource.getImage(ImageResource.SHIP), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setRotation(-90.0f);
        setMass(100);
        setDestructible(true);
        setImpactResistance(1.0f);
        setHealthMax(8);
        setHealth(getHealthMax());
        this.rotationSpeed = 6.0f;
        this.thrustForce = 20f;
    }

    public Ship() {
        this(0, 0);
    }

    public void onCollide(GameObject other) {

    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void rotateLeft() {
        addRotation(-rotationSpeed);
    }

    public void rotateRight() {
        addRotation(rotationSpeed);
    }

    public float getThrustForce() {
        return thrustForce;
    }
}
