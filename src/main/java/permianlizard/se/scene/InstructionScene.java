package permianlizard.se.scene;

import permianlizard.se.FontResource;
import permianlizard.se.Starfield;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class InstructionScene extends Scene {
    private Starfield starfield;
    private double ty;

    public InstructionScene() {
        super("INSTRUCTION_SCENE");
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
        g.drawString("Instructions", 15, 50);

        defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(14.0f);
        g.setFont(defaultFont);
        g.drawString("Well It's all gone to shit.", 15, 100);
        g.drawString("Not two days into our survey of this toilet of a system a swarm of asteroids ", 15, 125);
        g.drawString("decides to hit us. With that kind of welcome, we'll be getting right back out ", 15, 150);
        g.drawString("of here.", 15, 175);

        g.drawString("Problem is, the fist wave of rocks wiped out all but one of our ships and we've ", 15, 225);
        g.drawString("still got most of our crew stationed out there.", 15, 250);
        g.drawString("We need you to make good on our one remaining ship. You must navigate this ", 15, 275);
        g.drawString("minefield and rescue our people.", 15, 300);

        g.drawString("Just remember, we've got limited time. Our scans show us that the next wave of ", 15, 350);
        g.drawString("asteroids is going to put us all through the shredder. Not only that but you'll ", 15, 375);
        g.drawString("have limited fuel for this mission.", 15, 400);
        g.drawString("With any luck our bases should be able to top you up and do some repairs as well.", 15, 425);

        g.drawString("Don't forget you have a handy navigation map you can use to survey the whole mess.", 15, 475);
        g.drawString("Press and hold <TAB> to bring it up -good luck!", 15, 500);

        g.drawString("<ESC> Back", 15, 760);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 27) { // esc, close application
            this.director.popScene();
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
