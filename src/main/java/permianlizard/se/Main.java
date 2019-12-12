package permianlizard.se;

import permianlizard.se.scene.Director;
import permianlizard.se.scene.GameScene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Main implements Runnable {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final String FRAME_TITLE = "System Evacuation";
    private static final int FPS = 60;

    BufferStrategy bufferStrategy;
    Canvas canvas;
    Director director;

    private void render() {
        Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        g.clearRect(0, 0, width, height);
        director.render(g, width, height);

        g.dispose();
        bufferStrategy.show();
    }

    private void update(double delta) {
        director.update(delta);
    }

    @Override
    public void run() {

        //System.setProperty("sun.java2d.opengl", "true");

        try {
            ImageResource.loadImages();
            ImageResource.createAnimationSequences();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            FontResource.loadFonts();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (FontFormatException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // setup UI
        JFrame frame = new JFrame();
        frame.setTitle(FRAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);
        canvas.setBackground(Color.BLACK);
        panel.add(canvas);

        frame.pack();
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        canvas.setFocusTraversalKeysEnabled(false);

        director = new Director(WIDTH, HEIGHT);
        canvas.addKeyListener(director);

        GameScene gameScene = new GameScene();
        director.pushScene(gameScene);

        long lastLoopTime = System.nanoTime();
        final long desiredDeltaLoop =  (1000 * 1000 * 1000) / FPS;
        long lastFpsTime = 0;

        while(true){
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) desiredDeltaLoop);

            lastFpsTime += updateLength;
            if(lastFpsTime >= 1000000000){
                lastFpsTime = 0;
            }

            update(delta);
            render();

            try{
                long delay = (lastLoopTime - System.nanoTime() + desiredDeltaLoop) / 1000000;
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            }catch(Exception e){
                e.printStackTrace();
                // TODO
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        new Thread(main).start();
    }
}
