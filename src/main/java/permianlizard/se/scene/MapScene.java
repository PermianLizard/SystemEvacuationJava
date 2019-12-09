package permianlizard.se.scene;

import permianlizard.se.game.Game;
import permianlizard.se.game.Planet;
import permianlizard.se.game.Sun;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MapScene extends Scene {

    public static double SCALE = 0.04;

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

        int screenCenterX = width / 2;
        int screenCenterY = height / 2;

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
        for (Planet planet : planetList) {
            cx = (int)(planet.getX() * SCALE) + screenCenterX;
            cy = (int)(planet.getY() * SCALE) + screenCenterY;
            cw = (int)(planet.getWidth() * SCALE);
            ch = (int)(planet.getHeight() * SCALE);
            g.fillOval(cx, cy, cw, ch);
        }
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

        if (keyCode == 16) { // shift
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
