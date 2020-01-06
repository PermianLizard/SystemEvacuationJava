package permianlizard.se.scene;

import java.awt.*;
import java.awt.event.*;
import java.util.BitSet;
import java.util.Stack;

public class Director implements KeyListener, MouseListener, MouseMotionListener {

    private final int width;
    private final int height;
    private Stack<Scene> sceneStack;
    private BitSet keyStateBitSet;

    public Director(int width, int height) {
        this.width = width;
        this.height = height;
        sceneStack = new Stack<>();
        keyStateBitSet = new BitSet(525);
    }

    public void pushScene(Scene scene) {
        if (!sceneStack.empty()) {
            Scene currentScene = sceneStack.peek();
            if (currentScene != null) {
                currentScene.onPause();
            }
        }

        if (scene != null) {
            scene.director = this;
            scene.onEnter();
            sceneStack.push(scene);
        }
    }

    public void popScene() {
        Scene currentScene = sceneStack.pop();
        if (currentScene != null) {
            currentScene.onExit();
        }

        if (!sceneStack.empty()) {
            currentScene = sceneStack.peek();
            if (currentScene != null) {
                currentScene.onUnpause();
            }
        }
    }

    public void update(double delta) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.update(delta);
        }
    }

    public void render(Graphics2D g, int width, int height) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.render(g, width, height);
        }
    }

    public Scene getCurrentScene() {
        return sceneStack.peek();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyStateBitSet.set(e.getKeyCode());
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyStateBitSet.set(e.getKeyCode(), false);
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.keyReleased(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.mouseClicked(mouseEvent);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.mouseEntered(mouseEvent);
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.mouseExited(mouseEvent);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.mousePressed(mouseEvent);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.mouseReleased(mouseEvent);
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.mouseDragged(mouseEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Scene currentScene = sceneStack.peek();
        if (currentScene != null) {
            currentScene.mouseMoved(mouseEvent);
        }
    }
}
