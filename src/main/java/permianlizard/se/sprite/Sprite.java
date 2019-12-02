package permianlizard.se.sprite;

import java.awt.image.BufferedImage;

public class Sprite {

    private double x;
    private double y;
    private double anchorX;
    private double anchorY;
    private BufferedImage image;
    private float rotation;
    private boolean visible;

    public Sprite(BufferedImage image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.anchorX = image.getWidth() / 2;
        this.anchorY = image.getHeight() / 2;
        this.visible = true;
    }

    public Sprite(BufferedImage image, double x, double y, double anchorX, double anchorY) {
        this(image, x, y);
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public void update() {

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void translate(double tx, double ty) {
        this.x += tx;
        this.y += ty;
    }

    public double getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(double anchorX) {
        this.anchorX = anchorX;
    }

    public double getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(double anchorY) {
        this.anchorY = anchorY;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void addRotation(float value) {
        rotation += value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
