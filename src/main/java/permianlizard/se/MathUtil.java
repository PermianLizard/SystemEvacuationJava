package permianlizard.se;

public class MathUtil {

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static double distance(Vector2D v1, Vector2D v2) {
        return Math.sqrt(Math.pow(v2.getX() - v1.getX(), 2) + Math.pow(v2.getY() - v1.getY(), 2));
    }

    public static Vector2D getCircleClosestPoint(double x, double y, double cx, double cy, float cradius) {
        double dv = Math.sqrt(Math.pow(cx - x, 2) + Math.pow(cy - y, 2));
        double rx = cx + (cradius * (x - cx) / dv);
        double ry = cy + (cradius * (y - cy) / dv);

        return new Vector2D(rx, ry);
    }

    public static Vector2D getCircleClosestPoint(Vector2D v, Vector2D cv, float cRadius) {
        double dv = Math.sqrt(Math.pow(cv.getX() - v.getX(), 2) + Math.pow(cv.getY() - v.getY(), 2));
        double rx = cv.getX() + (cRadius * (v.getX() - cv.getX()) / dv);
        double ry = cv.getY() + (cRadius * (v.getY() - cv.getY()) / dv);
        return new Vector2D(rx, ry);
    }
}
