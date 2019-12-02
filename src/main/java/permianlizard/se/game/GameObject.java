package permianlizard.se.game;

import permianlizard.se.sprite.AnimatedSprite;

import java.awt.image.BufferedImage;

public class GameObject extends AnimatedSprite {

    private float collisionRadius;

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
}
