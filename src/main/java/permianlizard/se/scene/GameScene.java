package permianlizard.se.scene;

import permianlizard.se.FontResource;
import permianlizard.se.ImageResource;
import permianlizard.se.Starfield;
import permianlizard.se.game.*;
import permianlizard.se.sprite.AnimatedSprite;
import permianlizard.se.sprite.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class TimedLabel {
    private String text;
    private double x, y;
    private int duration;
    private boolean done;

    public TimedLabel(String text, double x, double y, int duration) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.done = false;
    }

    public void update() {
        if (!done) {
            duration--;
            if (duration <= 0) {
                done = true;
            }
        }
    }

    public void render(Graphics2D g, double xOffset, double yOffset) {
        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(12.0f).deriveFont(Font.PLAIN);
        g.setFont(defaultFont);
        g.setColor(Color.GREEN);
        //Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
        g.drawString(text, (int) (x - xOffset), (int) (y - yOffset));
    }

    public boolean isDone() {
        return done;
    }
}

public class GameScene extends Scene implements GameEventListener {

    int screenCenterX;
    int screenCenterY;
    double cameraX;
    double cameraY;

    private Sprite thrustSprite;
    private AnimatedSprite explosionSprite;

    private Starfield starfield;

    private java.util.List<AnimatedSprite> explosionList;
    private java.util.List<TimedLabel> timedLabelList;

    private MapScene mapScene;

    public GameScene() {
        super("GAME_SCENE");

        mapScene = new MapScene();
    }

    public void onEnter() {

        Game game = Game.getInstance();
        game.newGame();

        game.addEventListener(this);

        Director director = getDirector();
        screenCenterX = director.getWidth() / 2;
        screenCenterY = director.getHeight() / 2;

        explosionList = new ArrayList<>();
        timedLabelList = new ArrayList<>();

        BufferedImage[] anim = ImageResource.getAnimation(ImageResource.THRUST);
        thrustSprite = new AnimatedSprite(anim, 0, 0, 2, true);
        thrustSprite.setVisible(false);

        Ship ship = game.getShip();

        // FIXME
        cameraX = ship.getX();
        cameraY = ship.getY();

        starfield = new Starfield(200, director.getWidth(), director.getHeight());
        starfield.generate();
    }

    public void onExit() {
        Game game = Game.getInstance();
        game.cleanup();
        game.removeEventListener(this);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onUnpause() {

    }

    @Override
    public void update(double delta) {

        Game game = Game.getInstance();
        game.update(delta);

        if (!game.isGameOver()) {
            thrustSprite.update(delta);
        }

        for (Sprite explosion : explosionList) {
            explosion.update(delta);
        }

        List<AnimatedSprite> explosionsToRemove = new ArrayList<>(explosionList.size());
        for (AnimatedSprite explosion : explosionList) {
            if (explosion.isDone()) {
                explosionsToRemove.add(explosion);
            }
        }
        for (AnimatedSprite explosion : explosionsToRemove) {
            explosionList.remove(explosion);
        }

        // timed labels
        java.util.List<TimedLabel> labelsToRemove = new ArrayList<>(timedLabelList.size());
        for (TimedLabel timedLabel : timedLabelList) {
            timedLabel.update();
            if (timedLabel.isDone()) {
                labelsToRemove.add(timedLabel);
            }
        }
        for (TimedLabel timedLabel : labelsToRemove) {
            timedLabelList.remove(timedLabel);
        }
    }

    @Override
    public void render(Graphics2D g, int width, int height) {

        Game game = Game.getInstance();

        double shipX = cameraX;
        double shipY = cameraY;

        if (!game.isGameOver()) {
            Ship ship = game.getShip();

            shipX = ship.getX();
            shipY = ship.getY();

            cameraX = shipX - screenCenterX;
            cameraY = shipY - screenCenterY;
        }

        starfield.render(g, cameraX, cameraY);

        drawSprite(g, game.getSun(), shipX, shipY, width, height);

        for (Planet planet : game.getPlanetList()) {
            drawSprite(g, planet, shipX, shipY, width, height);
        }

        for (Moon moon : game.getMoonList()) {
            drawSprite(g, moon, shipX, shipY, width, height);
        }

        for (Base base : game.getBaseList()) {
            drawSprite(g, base, shipX, shipY, width, height);
        }

        for (Asteroid asteroid : game.getAsteroidList()) {
            drawSprite(g, asteroid, shipX, shipY, width, height);
        }

        for (AnimatedSprite explosion : explosionList) {
            drawSprite(g, explosion, shipX, shipY, width, height);
        }

        if (!game.isGameOver()) {
            drawShip(g);
        }

        for (TimedLabel timedLabel : timedLabelList) {
            timedLabel.render(g, cameraX, cameraY);
        }

        /*for (Sprite sprite : spriteList) {
            drawSprite(g, sprite, shipX, shipY, width, height);
        }*/

        // crosshairs at the center of the screen
        /*g.setColor(Color.GREEN);
        g.drawLine(screenCenterX - 25, screenCenterY, screenCenterX + 25, screenCenterY);
        g.drawLine(screenCenterX, screenCenterY - 25, screenCenterX, screenCenterY + 25);*/

        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(16.0f).deriveFont(Font.BOLD);
        g.setFont(defaultFont);
        g.setColor(Color.CYAN);
        String text = "TEXT";
        Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
        g.drawString(text, 20, 20);
    }

    @Override
    public void onBaseVisit(Base base) {
        timedLabelList.add(new TimedLabel("Fuel 000", base.getX() + 36, base.getY(), 200));
    }

    @Override
    public void onDestroyObject(GameObject object) {
        addExplosion(object.getX(), object.getY());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Game game = Game.getInstance();
        Ship ship = game.getShip();

        int keyCode = e.getKeyCode();

        if (keyCode == 65) { // a

            if (!game.isGameOver()) {
                ship.rotateLeft();
            }
        } else if (keyCode == 68) { // d
            if (!game.isGameOver()) {
                ship.rotateRight();
            }
        } else if (keyCode == 87) { // w

            if (!game.isGameOver()) {
                float rotationInDegrees = ship.getRotation();
                float thrustForce = ship.getThrustForce();
                double rotationInRadians = Math.toRadians(rotationInDegrees);
                double tx = Math.cos(rotationInRadians) * thrustForce;
                double ty = Math.sin(rotationInRadians) * thrustForce;

                ship.applyForce(tx, ty);
                //ship.translate(tx, ty);
                //thrustSprite.translate(tx, ty);
                thrustSprite.setVisible(true);
            }
        } else if (keyCode == 83) { // s

        } else if (keyCode == 16) { // shift
            this.director.pushScene(mapScene);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Game game = Game.getInstance();

        int keyCode = e.getKeyCode();
        if (keyCode == 87) { // w
            if (!game.isGameOver()) {
                thrustSprite.setVisible(false);
            }
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

    private void addExplosion(double x, double y) {
        BufferedImage[] anim = ImageResource.getAnimation(ImageResource.EXPLOSION);
        explosionSprite = new AnimatedSprite(anim, x, y, 8, false);
        explosionSprite.setRotation(45.0f);
        explosionList.add(explosionSprite);
    }

    private void drawSprite(Graphics2D g, Sprite sprite, double x, double y, int width, int height) {
        if (!sprite.isVisible()) {
            return;
        }

        double spriteX = sprite.getX();
        double spriteY = sprite.getY();

        // draw gravity radius
        if (sprite instanceof GameObject) {
            GameObject object = (GameObject) sprite;

            float gravityRadius = object.getGravityRadius();

            if (gravityRadius > 0) {
                double centerX = object.getX() + object.getAnchorX();
                double centerY = object.getY() + object.getAnchorY();

                g.setColor(Color.GREEN);
                g.drawOval((int) Math.rint(centerX - gravityRadius - cameraX), (int) Math.rint(centerY - gravityRadius - cameraY), (int) (gravityRadius * 2), (int) (gravityRadius * 2));
            }
        }

        // draw collection circles
        if (sprite instanceof Base) {
            Base base = (Base) sprite;

            float collectionRadius = base.getCollectionRadius();

            // only works if anchorX is guaranteed to be at the center
            double centerX = base.getX() + base.getAnchorX();
            double centerY = base.getY() + base.getAnchorY();

            g.setColor(Color.RED);
            g.drawOval((int) Math.rint(centerX - collectionRadius - cameraX), (int) Math.rint(centerY - collectionRadius - cameraY), (int) (collectionRadius * 2), (int) (collectionRadius * 2));
        }

        // draw collision circles
        /*if (sprite instanceof GameObject) {
            GameObject object = (GameObject) sprite;

            float collisionRadius = object.getCollisionRadius();
            double centerX = object.getX() + object.getAnchorX();
            double centerY = object.getY() + object.getAnchorY();

            g.setColor(Color.BLUE);
            g.drawOval((int) Math.rint(centerX - collisionRadius - cameraX), (int) Math.rint(centerY - collisionRadius - cameraY), (int) (collisionRadius * 2), (int) (collisionRadius * 2));
        }*/

        // check if sprite in in view area
        if (spriteX + sprite.getWidth() - 1 < cameraX) {
            return;
        }

        if (spriteX > cameraX + width) {
            return;
        }

        if (spriteY + sprite.getHeight() - 1 < cameraY) {
            return;
        }

        if (spriteY > cameraY + height) {
            return;
        }

        // move to center of object "anchor point"
        AffineTransform translateToRotate = AffineTransform.getTranslateInstance(-sprite.getAnchorX(), -sprite.getAnchorY());
        // rotate the object around the anchor point
        AffineTransform rotateObject = AffineTransform.getRotateInstance(Math.toRadians(sprite.getRotation()), 0, 0);
        // move the object back to origin
        AffineTransform translateToDraw = AffineTransform.getTranslateInstance(sprite.getAnchorX(), sprite.getAnchorY());
        // translate the object to its position in the scene
        AffineTransform translateWorld = AffineTransform.getTranslateInstance(spriteX - cameraX, spriteY - cameraY);

        rotateObject.concatenate(translateToRotate);
        translateToDraw.concatenate(rotateObject);
        translateWorld.concatenate(translateToDraw);

        AffineTransformOp op = new AffineTransformOp(translateWorld, AffineTransformOp.TYPE_BILINEAR);

        g.drawImage(op.filter(sprite.getImage(), null), 0, 0, null);
    }

    private void drawShip(Graphics2D g) {
        Game game = Game.getInstance();
        Ship ship = game.getShip();

        if (!ship.isVisible()) {
            return;
        }

        // Ship
        AffineTransform translateToRotate = AffineTransform.getTranslateInstance(-ship.getAnchorX(), -ship.getAnchorY());
        AffineTransform rotateObject = AffineTransform.getRotateInstance(Math.toRadians(ship.getRotation()), 0, 0);
        AffineTransform translateToDraw = AffineTransform.getTranslateInstance(ship.getAnchorX(), ship.getAnchorY());
        AffineTransform translateWorld = AffineTransform.getTranslateInstance(screenCenterX, screenCenterY);

        rotateObject.concatenate(translateToRotate);
        translateToDraw.concatenate(rotateObject);
        translateWorld.concatenate(translateToDraw);

        AffineTransformOp op = new AffineTransformOp(translateWorld, AffineTransformOp.TYPE_BILINEAR);
        g.drawImage(op.filter(ship.getImage(), null), 0, 0, null);

        // Thrust
        if (thrustSprite.isVisible()) {

            translateToRotate = AffineTransform.getTranslateInstance(-thrustSprite.getAnchorX(), -thrustSprite.getAnchorY());
            rotateObject = AffineTransform.getRotateInstance(Math.toRadians(ship.getRotation()), 0, 0);
            translateToDraw = AffineTransform.getTranslateInstance(thrustSprite.getAnchorX(), thrustSprite.getAnchorY());
            translateWorld = AffineTransform.getTranslateInstance(screenCenterX - 5, screenCenterY - 5); // FIXME

            rotateObject.concatenate(translateToRotate);
            translateToDraw.concatenate(rotateObject);
            translateWorld.concatenate(translateToDraw);

            op = new AffineTransformOp(translateWorld, AffineTransformOp.TYPE_BILINEAR);
            g.drawImage(op.filter(thrustSprite.getImage(), null), 0, 0, null);
        }

        // draw collision circle
        /*g.setColor(Color.BLUE);
        float collisionRadius = ship.getCollisionRadius();
        double centerX = screenCenterX + ship.getAnchorX();
        double centerY = screenCenterY + ship.getAnchorY();

        g.setColor(Color.BLUE);
        g.drawOval((int) Math.rint(centerX - collisionRadius), (int) Math.rint(centerY - collisionRadius), (int) (collisionRadius * 2), (int) (collisionRadius * 2));*/
    }
}
