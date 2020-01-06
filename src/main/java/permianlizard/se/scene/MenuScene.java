package permianlizard.se.scene;

import permianlizard.se.FontResource;
import permianlizard.se.Starfield;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuScene extends Scene {
    private GameScene gameScene;
    private InstructionScene instructionScene;
    private Starfield starfield;
    private double ty;

    public MenuScene() {
        super("MENU_SCENE");
        gameScene = new GameScene();
        instructionScene = new InstructionScene();
    }

    @Override
    public void onEnter() {
        starfield = new Starfield(200, director.getWidth(), director.getHeight());
        starfield.generate();
        ty = 0;
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
        ty += delta;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        starfield.render(g, 0, ty);

        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(32.0f);
        g.setFont(defaultFont);
        g.setColor(Color.WHITE);
        g.drawString("SYSTEM EVACUATION", 15, 50);

        defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(20.0f);
        g.setFont(defaultFont);
        g.drawString("<S> Start Game", 15, 100);
        g.drawString("<I> Instructions", 15, 125);
        g.drawString("<X> Exit", 15, 150);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        //System.out.println("keyCode: "+keyCode);

        if (keyCode == 27 || keyCode == 88) { // esc, close application
            System.exit(0);
        }
        if (keyCode == 83) { // s, start game
            this.getDirector().pushScene(gameScene);
        }
        if (keyCode == 73) { // i, instructions
            this.getDirector().pushScene(instructionScene);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

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
