package permianlizard.se.game;

import permianlizard.se.MathUtil;
import permianlizard.se.sprite.AnimatedSprite;

import java.awt.image.BufferedImage;

public abstract class GameObject extends AnimatedSprite {

    private float collisionRadius;
    private double velX;
    private double velY;
    private float mass;
    private boolean staticObject;
    private float gravityRadius;
    private boolean destructible;
    private float impactResistance;
    private float health;
    private float healthMax;

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

    public void addVelX(double amount) {
        this.velX += amount;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void addVelY(double amount) {
        this.velY += amount;
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

    public float getGravityRadius() {
        return gravityRadius;
    }

    public void setGravityRadius(float gravityRadius) {
        this.gravityRadius = gravityRadius;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public float getImpactResistance() {
        return impactResistance;
    }

    public void setImpactResistance(float impactResistance) {
        this.impactResistance = impactResistance;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealthMax() {
        return healthMax;
    }

    public void setHealthMax(float healthMax) {
        this.healthMax = healthMax;
    }
}
