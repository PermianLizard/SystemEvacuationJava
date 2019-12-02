package permianlizard.se.scene;

import permianlizard.se.*;
import permianlizard.se.game.Asteroid;
import permianlizard.se.game.Base;
import permianlizard.se.game.GameObject;
import permianlizard.se.game.Ship;
import permianlizard.se.sprite.AnimatedSprite;
import permianlizard.se.sprite.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameScene extends Scene {

    Ship ship;
    Sprite thrustSprite;
    AnimatedSprite explosionSprite;
    Asteroid asteroid;
    Base baseSprite;
    Sprite moon1Sprite;
    Sprite planet1Sprite;
    Sprite planet2Sprite;
    Sprite sunSprite;
    java.util.List<Sprite> spriteList;

    public GameScene(String name) {
        super(name);
    }

    public void onEnter() {
        spriteList = new ArrayList<>();

        ship = new Ship(500, 150);
        spriteList.add(ship);

        BufferedImage[] anim = ImageResource.getAnimation(ImageResource.THRUST);
        thrustSprite = new AnimatedSprite(anim, 494, 144, 2, true);
        thrustSprite.setVisible(false);
        spriteList.add(thrustSprite);

        anim = ImageResource.getAnimation(ImageResource.EXPLOSION);
        explosionSprite = new AnimatedSprite(anim, 400, 150, 8, true);
        explosionSprite.setRotation(45.0f);
        spriteList.add(explosionSprite);

        asteroid = new Asteroid(300, 150);
        spriteList.add(asteroid);

        baseSprite = new Base(200, 150);
        spriteList.add(baseSprite);

        BufferedImage image;

        image = ImageResource.getImage(ImageResource.MOON_48_1);
        moon1Sprite = new Sprite(image, 600, 250);
        spriteList.add(moon1Sprite);

        image = ImageResource.getImage(ImageResource.PLANET_64_1);
        planet1Sprite = new Sprite(image, 400, 250);
        spriteList.add(planet1Sprite);

        image = ImageResource.getImage(ImageResource.PLANET_64_2);
        planet2Sprite = new Sprite(image, 200, 250);
        spriteList.add(planet2Sprite);

        image = ImageResource.getImage(ImageResource.SUN);
        sunSprite = new Sprite(image, 200, 450);
        spriteList.add(sunSprite);
    }

    public void onExit() {

    }

    @Override
    public void update() {
        for (Sprite sprite : spriteList) {
            sprite.update();
        }

        // check for collisions
        for (int a = 0; a < spriteList.size() - 1; a++) {
            for (int b = a + 1; b < spriteList.size(); b++) {
                Sprite spriteA = spriteList.get(a);
                Sprite spriteB = spriteList.get(b);

                if (spriteA instanceof GameObject && spriteB instanceof GameObject) {

                    GameObject objectA = (GameObject)spriteA;
                    GameObject objectB = (GameObject)spriteB;

                    if (objectA.collidesWith(objectB)) {
                        // handle collision
                    }
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        int screenCenterX = width / 2;
        int screenCenterY = height / 2;

        double cameraX = ship.getX() - screenCenterX;
        double cameraY = ship.getY() - screenCenterY;

        for (Sprite sprite : spriteList) {
            if (!sprite.isVisible()) {
                continue;
            }

            double spriteX = sprite.getX();
            double spriteY = sprite.getY();

            // check if sprite in in view area
            if (spriteX + sprite.getWidth() < cameraX) {
                continue;
            }

            if (spriteY + sprite.getHeight() < cameraY) {
                continue;
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

            if (sprite instanceof GameObject) {
                GameObject object = (GameObject)sprite;

                g.setColor(Color.BLUE);
                g.drawOval((int) Math.rint(object.getX() - cameraX), (int) Math.rint(object.getY() - cameraY), (int) (object.getCollisionRadius() * 2), (int) (object.getCollisionRadius() * 2));
            }
        }

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
            ship.addRotation(-6);
            thrustSprite.addRotation(-6);
        } else if (keyCode == 68) { // d
            ship.addRotation(6);
            thrustSprite.addRotation(6);
        } else if (keyCode == 87) { // w

            float rotationInDegrees = ship.getRotation();
            double rotationInRadians = Math.toRadians(rotationInDegrees);
            double tx = Math.cos(rotationInRadians) * 5;
            double ty = Math.sin(rotationInRadians) * 5;

            ship.translate(tx, ty);
            thrustSprite.translate(tx, ty);

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
}
