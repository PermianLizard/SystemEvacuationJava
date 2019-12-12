package permianlizard.se.scene;

import permianlizard.se.FontResource;
import permianlizard.se.Vector2D;
import permianlizard.se.game.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MapScene extends Scene {

    public static double SCALE = 0.04;
    public static int TOKEN_SIZE = 50;
    private final static Stroke DASHED_STROKE;

    static {
        DASHED_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    }

    public MapScene() {
        super("MAP_SCENE");
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onUnpause() {

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        g = (Graphics2D) g.create();

        int screenCenterX = width / 2;
        int screenCenterY = height / 2;

        // draw grid
        g.setColor(new Color(0, 0, 80));
        for (int x = 50; x < width; x += 50) {
            g.drawLine(x, 0, x, height);
        }
        for (int y = 50; y < height; y += 50) {
            g.drawLine(0, y, width, y);
        }

        Game game = Game.getInstance();

        Sun sun = game.getSun();
        g.setColor(new Color(220, 220, 220));
        int cx = (int)(sun.getX() * SCALE) + screenCenterX;
        int cy = (int)(sun.getY() * SCALE) + screenCenterY;
        int cw = (int)(sun.getWidth() * SCALE);
        int ch = (int)(sun.getHeight() * SCALE);
        g.fillOval(cx, cy, cw, ch);

        java.util.List<Planet> planetList = game.getPlanetList();

        g.setStroke(DASHED_STROKE);
        g.setColor(new Color(128, 128, 128));
        for (Planet planet : planetList) {
            cx = (int)((planet.getX() + planet.getAnchorX()) * SCALE);
            cy = (int)((planet.getY() + planet.getAnchorX()) * SCALE);

            Vector2D v = new Vector2D(cx, cy);
            int radius = (int)v.getLength();
            int diameter = radius * 2;

            int x = screenCenterX - radius;
            int y = screenCenterY - radius;
            g.drawOval(x, y, diameter, diameter);
        }

        g.setColor(new Color(204, 204, 204));
        for (Planet planet : planetList) {
            cx = (int)(planet.getX() * SCALE) + screenCenterX;
            cy = (int)(planet.getY() * SCALE) + screenCenterY;
            cw = (int)(planet.getWidth() * SCALE);
            ch = (int)(planet.getHeight() * SCALE);
            g.fillOval(cx, cy, cw, ch);

            Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(16.0f).deriveFont(Font.PLAIN);
            g.setFont(defaultFont);
            g.setColor(new Color(255, 255, 255, 120));
            String text = planet.getName();
            Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
            int x = (int) (((planet.getX() + planet.getAnchorX()) * SCALE) - textBounds.getWidth() / 2 + screenCenterX);
            int y = (int) ((planet.getY() + planet.getAnchorY()) * SCALE) - 10 + screenCenterY;
            g.drawString(text, x, y);
        }

        java.util.List<Moon> moonList = game.getMoonList();
        for (Moon moon : moonList) {
            cx = (int)(moon.getX() * SCALE) + screenCenterX;
            cy = (int)(moon.getY() * SCALE) + screenCenterY;
            cw = (int)(TOKEN_SIZE * SCALE);
            ch = (int)(TOKEN_SIZE * SCALE);
            g.fillOval(cx, cy, cw, ch);
        }

        java.util.List<Base> baseList = game.getBaseList();

        for (Base base : baseList) {
            if (base.hasCrew()) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(110, 110, 0));
            }
            cx = (int)(base.getX() * SCALE) + screenCenterX;
            cy = (int)(base.getY() * SCALE) + screenCenterY;
            cw = (int)(TOKEN_SIZE * SCALE);
            ch = (int)(TOKEN_SIZE * SCALE);
            g.fillOval(cx, cy, cw, ch);
        }

        // show player position
        Ship ship = game.getShip();
        g.setColor(Color.RED);
        cx = (int)(ship.getX() * SCALE) + screenCenterX;
        cy = (int)(ship.getY() * SCALE) + screenCenterY;
        cw = (int)(TOKEN_SIZE * SCALE);
        ch = (int)(TOKEN_SIZE * SCALE);
        g.fillOval(cx, cy, cw, ch);

        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 9) { // tab
            this.director.popScene();
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
