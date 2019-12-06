package permianlizard.se.game;

import permianlizard.se.MathUtil;
import permianlizard.se.sprite.AnimatedSprite;

import java.awt.image.BufferedImage;

public abstract class GameObject extends AnimatedSprite {

    public static final float GRAVITY_CONSTANT = 2f;
    public static final float SPEED_LIMIT = 180f;

    private float collisionRadius;
    private double velX;
    private double velY;
    private float mass;
    private boolean staticObject;

    public GameObject(BufferedImage frame, double x, double y) {
        super(frame, x, y);
    }

    public GameObject(BufferedImage frame, double x, double y, double anchorX, double anchorY) {
        super(frame, x, y, anchorX, anchorY);
    }

    public GameObject(BufferedImage[] frames, double x, double y, int period) {
        super(frames, x, y, period);
    }

    public GameObject(BufferedImage[] frames, double x, double y, int period, boolean loop) {
        super(frames, x, y, period, loop);
    }

    public float getCollisionRadius() {
        return collisionRadius;
    }

    public void setCollisionRadius(float collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public boolean collidesWith(GameObject other) {
        float thisCollisionRadius = getCollisionRadius();
        float otherCollisionRadius = other.getCollisionRadius();

        if (thisCollisionRadius == 0 || otherCollisionRadius == 0) {
            return false;
        }

        double thisCenterX = getX() + getWidth() / 2;
        double thisCenterY = getY() + getHeight() / 2;

        double otherCenterX = other.getX() + other.getWidth() / 2;
        double otherCenterY = other.getY() + other.getHeight() / 2;

        double distance = MathUtil.distance(thisCenterX, thisCenterY, otherCenterX, otherCenterY);

        return distance < thisCollisionRadius + otherCollisionRadius;
    }

    public abstract void onCollide(GameObject other);

    @Override
    public void update(double delta) {
        super.update(delta);
    }

    public void applyForce(double forceX, double forceY) {
        double x = forceX / this.mass;
        double y = forceY / this.mass;
        this.velX += x;
        this.velY += y;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public boolean isStaticObject() {
        return staticObject;
    }

    public void setStaticObject(boolean staticObject) {
        this.staticObject = staticObject;
    }
}
