package permianlizard.se;

import java.util.Objects;

public class Vector2D {

    private double x;
    private double y;

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void sub(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void div(Vector2D other) {
        this.x /= other.x;
        this.y /= other.y;
    }

    public void mult(Vector2D other) {
        this.x *= other.x;
        this.y *= other.y;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector2D getUnit() {
        double length = getLength();
        x = this.x / length;
        y = this.y / length;
        return new Vector2D(x, y);
    }

    public void makeUnit() {
        double length = getLength();
        x = x / length;
        y = y / length;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(vector2D.x, x) == 0 &&
                Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
