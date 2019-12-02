package permianlizard.se.scene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Director implements KeyListener {

    private Scene currentScene;
    private Map<String, Scene> sceneMap;

    public Director() {
        currentScene = null;
        sceneMap = new HashMap<>();
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

    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }

    public void render(Graphics2D g, int width, int height) {
        if (currentScene != null) {
            currentScene.render(g, width, height);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (currentScene != null) {
            currentScene.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (currentScene != null) {
            currentScene.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (currentScene != null) {
            currentScene.keyReleased(e);
        }
    }
}
