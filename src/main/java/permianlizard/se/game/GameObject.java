package permianlizard.se.game;

import permianlizard.se.sprite.AnimatedSprite;

import java.awt.image.BufferedImage;

public abstract class GameObject extends AnimatedSprite {

    public static final float GRAVITY_CONSTANT = 2f;
    public static final float SPEED_LIMIT = 180f;

    private float collisionRadius;
    private float velX;
    private float velY;
    private float accX;
    private float accY;
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

        double distance = Math.sqrt(Math.pow(otherCenterX - thisCenterX , 2) + Math.pow(otherCenterY - thisCenterY , 2));

        return distance < thisCollisionRadius + otherCollisionRadius;
    }

    public abstract void onCollide(GameObject other);

    @Override
    public void update(double delta) {
        super.update(delta);
        if (staticObject == false) {

        }
    }

    public void applyForce(float forceX, float forceY) {
        float x = forceX / this.mass;
        float y = forceY / this.mass;
        this.velX += x;
        this.velY += y;
    }
}
