package permianlizard.se.scene;

import permianlizard.se.FontResource;
import permianlizard.se.ImageResource;
import permianlizard.se.MathUtil;
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

public class GameScene extends Scene {

    int screenCenterX;
    int screenCenterY;
    double cameraX;
    double cameraY;

    Ship ship;
    Sprite thrustSprite;
    AnimatedSprite explosionSprite;
    Asteroid asteroid;
    MoonA moonA;
    PlanetA planetA;
    PlanetB planetB;
    Sun sun;

    Starfield starfield;

    java.util.List<Sprite> explosionList;
    java.util.List<Asteroid> asteroidList;
    java.util.List<Base> baseList;
    java.util.List<Planet> planetList;

    java.util.List<GameObject> objectList;

    java.util.List<TimedLabel> timedLabelList;

    public GameScene(String name) {
        super(name);
    }

    public void onEnter() {

        Director director = getDirector();

        screenCenterX = director.getWidth() / 2;
        screenCenterY = director.getHeight() / 2;

        explosionList = new ArrayList<>();
        asteroidList = new ArrayList<>();
        baseList = new ArrayList<>();
        planetList = new ArrayList<>();

        objectList = new ArrayList<>();

        timedLabelList = new ArrayList<>();

        ship = new Ship(500, 150);
        objectList.add(ship);

        BufferedImage[] anim = ImageResource.getAnimation(ImageResource.THRUST);
        thrustSprite = new AnimatedSprite(anim, 494, 144, 2, true);
        thrustSprite.setVisible(false);

        anim = ImageResource.getAnimation(ImageResource.EXPLOSION);
        explosionSprite = new AnimatedSprite(anim, 400, 150, 8, true);
        explosionSprite.setRotation(45.0f);
        explosionList.add(explosionSprite);

        asteroid = new Asteroid(300, 150);
        asteroidList.add(asteroid);
        objectList.add(asteroid);

        Base base = new Base(80, 25);
        baseList.add(base);
        objectList.add(base);

        base = new Base(200, 25);
        baseList.add(base);
        objectList.add(base);

        base = new Base(80, 150);
        baseList.add(base);
        objectList.add(base);

        base = new Base(200, 150);
        baseList.add(base);
        objectList.add(base);

        moonA = new MoonA(600, 250);
        planetList.add(moonA);
        objectList.add(moonA);

        planetA = new PlanetA(400, 250);
        planetList.add(planetA);
        objectList.add(planetA);

        planetB = new PlanetB(200, 250);
        planetList.add(planetB);
        objectList.add(planetB);

        sun = new Sun(200, 450);
        objectList.add(sun);

        // FIXME
        cameraX = ship.getX();
        cameraY = ship.getY();

        starfield = new Starfield(180, director.getWidth(), director.getHeight());
        starfield.generate();
    }

    public void onExit() {

    }

    @Override
    public void update(double delta) {
        /*for (Sprite sprite : spriteList) {
            sprite.update();
        }*/

        sun.update(delta);

        for (Planet planet : planetList) {
            planet.update(delta);
        }

        for (Base base : baseList) {
            base.update(delta);
        }

        for (Asteroid asteroid : asteroidList) {
            asteroid.update(delta);
        }

        for (Sprite explosion : explosionList) {
            explosion.update(delta);
        }

        thrustSprite.update(delta);
        ship.update(delta);

        // update physics
        for (GameObject object : objectList) {

            if (object.isStaticObject()) {
                continue;
            }

            double velX = object.getVelX();
            double velY = object.getVelY();

            object.translate(velX, velY);
        }

        // check base collection
        float shipCollisionRadius = ship.getCollisionRadius();
        for (Base base : baseList) {

            if (base.isVisited()) {
                continue;
            }

            float collectionRadius = base.getCollectionRadius();
            double distance = MathUtil.distance(ship.getX(), ship.getY(), base.getX(), base.getY());
            if (distance < shipCollisionRadius + collectionRadius) {
                base.visit();
                timedLabelList.add(new TimedLabel("Fuel 000", ship.getX() + 36, ship.getY(), 200));
            }
        }

        // check for collisions
        for (int a = 0; a < objectList.size() - 1; a++) {
            for (int b = a + 1; b < objectList.size(); b++) {

                GameObject objectA = objectList.get(a);
                GameObject objectB = objectList.get(b);

                if (objectA.collidesWith(objectB)) {
                    // handle collision
                    objectA.onCollide(objectB);
                    objectB.onCollide(objectA);
                }

            }
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
        double shipX = ship.getX();
        double shipY = ship.getY();

        cameraX = shipX - screenCenterX;
        cameraY = shipY - screenCenterY;

        starfield.render(g, cameraX, cameraY);

        drawSprite(g, sun, shipX, shipY, width, height);

        for (Planet planet : planetList) {
            drawSprite(g, planet, shipX, shipY, width, height);
        }

        for (Base base : baseList) {
            drawSprite(g, base, shipX, shipY, width, height);
        }

        for (Asteroid asteroid : asteroidList) {
            drawSprite(g, asteroid, shipX, shipY, width, height);
        }

        for (Sprite explosion : explosionList) {
            drawSprite(g, explosion, shipX, shipY, width, height);
        }

        drawShip(g);

        for (TimedLabel timedLabel : timedLabelList) {
            timedLabel.render(g, cameraX, cameraY);
        }

        /*for (Sprite sprite : spriteList) {
            drawSprite(g, sprite, shipX, shipY, width, height);
        }*/

        // crosshairs at the center of the screen
        g.setColor(Color.GREEN);
        g.drawLine(screenCenterX - 25, screenCenterY, screenCenterX + 25, screenCenterY);
        g.drawLine(screenCenterX, screenCenterY - 25, screenCenterX, screenCenterY + 25);

        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(16.0f).deriveFont(Font.BOLD);
        g.setFont(defaultFont);
        g.setColor(Color.CYAN);
        String text = "TEXT";
        Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
        g.drawString(text, 20, 20);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 65) { // a
            ship.rotateLeft();
            //thrustSprite.addRotation(-16);
        } else if (keyCode == 68) { // d
            ship.rotateRight();
            //thrustSprite.addRotation(16);
        } else if (keyCode == 87) { // w

            float rotationInDegrees = ship.getRotation();
            float thrustForce = ship.getThrustForce();
            double rotationInRadians = Math.toRadians(rotationInDegrees);
            double tx = Math.cos(rotationInRadians) * thrustForce;
            double ty = Math.sin(rotationInRadians) * thrustForce;

            ship.applyForce(tx, ty);
            //ship.translate(tx, ty);
            //thrustSprite.translate(tx, ty);

            thrustSprite.setVisible(true);
        } else if (keyCode == 83) { // s

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == 87) { // w
            thrustSprite.setVisible(false);
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

    private void drawSprite(Graphics2D g, Sprite sprite, double x, double y, int width, int height) {
        if (!sprite.isVisible()) {
            return;
        }

        double spriteX = sprite.getX();
        double spriteY = sprite.getY();

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

        // draw collision circles
        if (sprite instanceof GameObject) {
            GameObject object = (GameObject) sprite;

            float collisionRadius = object.getCollisionRadius();
            double centerX = object.getX() + object.getAnchorX();
            double centerY = object.getY() + object.getAnchorY();

            g.setColor(Color.BLUE);
            g.drawOval((int) Math.rint(centerX - collisionRadius - cameraX), (int) Math.rint(centerY - collisionRadius - cameraY), (int) (collisionRadius * 2), (int) (collisionRadius * 2));
        }

        // draw collision circles
        if (sprite instanceof Base) {
            Base base = (Base) sprite;

            float collectionRadius = base.getCollectionRadius();

            // only works if anchorX is guaranteed to be at the center
            double centerX = base.getX() + base.getAnchorX();
            double centerY = base.getY() + base.getAnchorY();

            g.setColor(Color.RED);
            g.drawOval((int) Math.rint(centerX - collectionRadius - cameraX), (int) Math.rint(centerY - collectionRadius - cameraY), (int) (collectionRadius * 2), (int) (collectionRadius * 2));
        }
    }

    private void drawShip(Graphics2D g) {
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
            translateWorld = AffineTransform.getTranslateInstance(screenCenterX - 6, screenCenterY - 6); // FIXME

            rotateObject.concatenate(translateToRotate);
            translateToDraw.concatenate(rotateObject);
            translateWorld.concatenate(translateToDraw);

            op = new AffineTransformOp(translateWorld, AffineTransformOp.TYPE_BILINEAR);
            g.drawImage(op.filter(thrustSprite.getImage(), null), 0, 0, null);
        }

        // draw collision circle
        g.setColor(Color.BLUE);
        float collisionRadius = ship.getCollisionRadius();
        double centerX = screenCenterX + ship.getAnchorX();
        double centerY = screenCenterY + ship.getAnchorY();

        g.setColor(Color.BLUE);
        g.drawOval((int) Math.rint(centerX - collisionRadius), (int) Math.rint(centerY - collisionRadius), (int) (collisionRadius * 2), (int) (collisionRadius * 2));
    }
}
