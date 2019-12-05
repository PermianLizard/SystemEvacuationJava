package permianlizard.se.scene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Scene {

    private final String name;
    protected Director director;

    public Scene(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Director getDirector() {
        return director;
    }

    public abstract void onEnter();

    public abstract void onExit();

    public abstract void update(double delta);

    public abstract void render(Graphics2D g, int width, int height);

    public abstract void keyTyped(KeyEvent e);

    public abstract void keyPressed(KeyEvent e);

    public abstract void keyReleased(KeyEvent e);

    public abstract void mouseClicked(MouseEvent mouseEvent);

    public abstract void mouseEntered(MouseEvent mouseEvent);

    public abstract void mouseExited(MouseEvent mouseEvent);

    public abstract void mousePressed(MouseEvent mouseEvent);

    public abstract void mouseReleased(MouseEvent mouseEvent);

    public abstract void mouseDragged(MouseEvent mouseEvent);

    public abstract void mouseMoved(MouseEvent mouseEvent);
}
