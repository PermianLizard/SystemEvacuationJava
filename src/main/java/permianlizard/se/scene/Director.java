package permianlizard.se.scene;

import java.awt.*;
import java.awt.event.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Director implements KeyListener, MouseListener, MouseMotionListener {

    private final int width;
    private final int height;
    private Scene currentScene;
    private Map<String, Scene> sceneMap;
    private BitSet keyStateBitSet;

    public Director(int width, int height) {
        this.width = width;
        this.height = height;
        currentScene = null;
        sceneMap = new HashMap<>();
        keyStateBitSet = new BitSet(525);
    }

    public Scene addScene(Scene scene) {
        scene.director = this;
        return sceneMap.put(scene.getName(), scene);
    }

    public void setScene(String name) {
        Scene sceneToSet = sceneMap.get(name);

        if (currentScene != null) {
            currentScene.onExit();
        }

        if (sceneToSet != null) {
            currentScene = sceneToSet;
            currentScene.onEnter();
        }
    }

    public void update(double delta) {
        if (currentScene != null) {
            currentScene.update(delta);
        }
    }

    public void render(Graphics2D g, int width, int height) {
        if (currentScene != null) {
            currentScene.render(g, width, height);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (currentScene != null) {
            currentScene.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyStateBitSet.set(e.getKeyCode());
        if (currentScene != null) {
            currentScene.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyStateBitSet.set(e.getKeyCode(), false);
        if (currentScene != null) {
            currentScene.keyReleased(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (currentScene != null) {
            currentScene.mouseClicked(mouseEvent);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (currentScene != null) {
            currentScene.mouseEntered(mouseEvent);
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (currentScene != null) {
            currentScene.mouseExited(mouseEvent);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (currentScene != null) {
            currentScene.mousePressed(mouseEvent);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (currentScene != null) {
            currentScene.mouseReleased(mouseEvent);
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (currentScene != null) {
            currentScene.mouseDragged(mouseEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if (currentScene != null) {
            currentScene.mouseMoved(mouseEvent);
        }
    }
}
