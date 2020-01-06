package permianlizard.se;

import java.util.Objects;

public class Vector2D {

    private final double x;
    private final double y;

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(a.x - b.x, a.y - b.y);
    }

    public static Vector2D div(Vector2D a, Vector2D b) {
        return new Vector2D(a.x / b.x, a.y / b.y);
    }

    public static Vector2D mult(Vector2D a, Vector2D b) {
        return new Vector2D(a.x * b.x, a.y * b.y);
    }

    public static Vector2D mult(Vector2D v, double value) {
        return new Vector2D(v.x * value, v.y * value);
    }

    public static double dot(Vector2D a, Vector2D b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double cross(Vector2D a, Vector2D b) {
        return a.x * b.x - a.y * b.y;
    }

    public static double getAngleBetween(Vector2D a, Vector2D b) {
        double cross = Vector2D.cross(a, b);
        double dot = Vector2D.dot(a, b);
        return Math.toDegrees(Math.atan2(cross, dot));
    }

    public static Vector2D getUnit(Vector2D v) {
        double length = v.getLength();
        double x = v.x / length;
        double y = v.y / length;
        return new Vector2D(x, y);
    }

    public static Vector2D setLength(Vector2D v,  double length) {
        double len = v.getLength();
        double x = (v.x / len) * length;
        double y = (v.y / len) * length;
        return new Vector2D(x, y);
    }

    public static Vector2D rotate(Vector2D v,  double angleInDegrees) {
        double radians = Math.toRadians(angleInDegrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double x = v.x * cos - v.y * sin;
        double y = v.x * sin - v.y * cos;
        return new Vector2D(x, y);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double getAngle() {
        double lengthSqrd = Math.pow(x, 2) + Math.pow(y, 2);
        if (lengthSqrd == 0) {
            return 0;
        }
        return Math.toDegrees(Math.atan2(y, x));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
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
