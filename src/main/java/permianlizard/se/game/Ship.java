package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Ship extends GameObject {

    private final float rotationSpeed;
    private final float thrustForce;
    private int crew;

    public Ship(double x, double y) {
        super(ImageResource.getImage(ImageResource.SHIP), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setRotation(-90.0f);
        setMass(100);
        setStaticObject(true); // FIXME Temporary
        setDestructible(true);
        setImpactResistance(1.0f);
        setHealthMax(8);
        setHealth(getHealthMax());
        this.rotationSpeed = 6.0f;
        this.thrustForce = 20f;
        this.crew = 0;
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

    public int getCrew() {
        return crew;
    }

    public void setCrew(int crew) {
        this.crew = crew;
    }

    public void addCrew(int amount) {
        this.crew += amount;
    }

    public boolean hasCrew() {
        return crew > 0;
    }
}
