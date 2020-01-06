package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Ship extends Construct {

    private final float rotationSpeed;
    private final float thrustForce;
    private int fuel;
    private int fuelMax;

    public Ship(double x, double y) {
        super(ImageResource.getImage(ImageResource.SHIP), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setRotation(-90.0f);
        setMass(100);
        //setStaticObject(true);
        setDestructible(true);
        setImpactResistance(2.8f);
        setHealthMax(8);
        setHealth(getHealthMax());
        setCrewMax(30 * 7 + 1);
        setCrew(1);
        this.rotationSpeed = 6.0f;
        this.thrustForce = 20f;
        this.fuelMax = 700;
        this.fuel = fuelMax;
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

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getFuelMax() {
        return fuelMax;
    }

    public void setFuelMax(int fuelMax) {
        this.fuelMax = fuelMax;
    }

    public boolean thrustForward() {
        if (fuel < 1) {
            return false;
        }

        fuel -= 1;

        double rotationInRadians = Math.toRadians(getRotation());
        double tx = Math.cos(rotationInRadians) * thrustForce;
        double ty = Math.sin(rotationInRadians) * thrustForce;
        applyForce(tx, ty);
        return true;
    }
}
