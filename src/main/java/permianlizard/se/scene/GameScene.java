package permianlizard.se.scene;

import permianlizard.se.*;
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
        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(16.0f).deriveFont(Font.PLAIN);
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

    private int screenCenterX;
    private int screenCenterY;
    private double cameraX;
    private double cameraY;
    private boolean paused;

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

        paused = true;

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
        System.out.println("GameScene onExit()");
        Game game = Game.getInstance();
        //game.cleanup();
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

        if (paused || (game.isGameOver() && game.isVictory())) {
            return;
        }

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
        Ship ship = game.getShip();

        double shipX = cameraX;
        double shipY = cameraY;

        if (!game.isGameOver()) {

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

        if (!game.isGameOver() || game.isVictory()) {
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

        if (!game.isGameOver()) {

            // navigation widget
            int navOuterRadius = 330;
            int navInnerRadius = 80;
            int navOuterDiameter = navOuterRadius * 2;
            int navInnerDiameter = navInnerRadius * 2;
            int navCenterX = screenCenterX + (int)ship.getAnchorX();
            int navCenterY = screenCenterY + (int)ship.getAnchorY();
            int navOuterX = navCenterX - navOuterRadius;
            int navOuterY = navCenterY - navOuterRadius;
            int navInnerX = navCenterX - navInnerRadius;
            int navInnerY = navCenterY - navInnerRadius;

            g.setColor(new Color(255, 255, 0, 102));
            g.drawOval(navInnerX, navInnerY, navInnerDiameter, navInnerDiameter);
            g.drawOval(navOuterX, navOuterY, navOuterDiameter, navOuterDiameter);

            for (Base base : game.getBaseList()) {
                if (base.isVisited()) {
                    continue;
                }

                double distance = MathUtil.distance(ship.getX(), ship.getY(), base.getX(), base.getY()) / 50;

                if (distance > 1 && distance < navOuterRadius) {
                    Vector2D closestPointOutside = MathUtil.getCircleClosestPoint(base.getX(), base.getY(), ship.getX(), ship.getY(), navOuterRadius);
                    //Vector2D closestPointInside = MathUtil.getCircleClosestPoint(base.getX(), base.getY(), ship.getX(), ship.getY(), navOuterRadius - (int)distance);
                    Vector2D closestPointInside = MathUtil.getCircleClosestPoint(base.getX(), base.getY(), ship.getX(), ship.getY(), navInnerRadius + (int)distance);

                    int lineX1 = (int)closestPointInside.getX() - (int)cameraX + (int)ship.getAnchorX();
                    int lineY1 = (int)closestPointInside.getY() - (int)cameraY + (int)ship.getAnchorY();

                    int lineX2 = (int)closestPointOutside.getX() - (int)cameraX + (int)ship.getAnchorX();
                    int lineY2 = (int)closestPointOutside.getY() - (int)cameraY + (int)ship.getAnchorY();

                    g.drawLine(lineX1, lineY1, lineX2, lineY2);
                }
            }

            Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(16.0f).deriveFont(Font.BOLD);
            g.setFont(defaultFont);
            g.setColor(Color.WHITE);

            g.drawString("FUEL", 20, 30);
            drawBar(g, 88, 13, ship.getFuel(), ship.getFuelMax(), 100, 17, false);

            g.drawString("HULL CONDITION", 210, 30);
            drawBar(g, 425, 13, ship.getHealth(), ship.getHealthMax(), 100, 17, false);

            g.drawString("RESCUED", 550, 30);
            drawBar(g, 660, 13, ship.getCrew(), ship.getCrewMax(), 100, 17, false);

            g.drawString(String.format("Time: %.2f", Game.TIME_LIMIT - game.getTimeElapsed() / 100), 780, 30);

            Vector2D playerVel = new Vector2D(ship.getVelX(), ship.getVelY());
            g.drawString(String.format("Speed: %.2f", playerVel.getLength()), 780, 55);

            g.drawString("<ESC> Exit  <P> Pause  <TAB> Map", 250, 760);
        }

        boolean greyOut = false;
        String titleText = "";
        String contentText = "";
        String controlText = "";

        if (game.isGameOver()) {
            greyOut = true;
            if (game.isVictory()) {
                titleText = "VICTORY!";
            } else {
                titleText = "You've botched it!";
                contentText = game.getGameOverMessage();
            }
            controlText = "Press <ESC> to Continue";
        } else if (paused) {
            greyOut = true;
            titleText = "PAUSED";
            controlText = "Press <P> to Unpause";
        }

        if (greyOut) {
            g.setColor(new Color(51, 51, 51, 102));
            g.fillRect(0, 0, width, height);

            drawTitleText(g, titleText);
            drawContentText(g, contentText);
            drawControlText(g, controlText);
        }
    }

    @Override
    public void onBaseVisit(Base base) {
        Game game = Game.getInstance();
        Ship ship = game.getShip();

        int row = 0;

        int crew = base.getCrew();
        ship.awardCrew(crew);
        base.setCrew(0);
        timedLabelList.add(new TimedLabel("+ " + crew + " CREW", ship.getX() + 40, ship.getY() + 10, 200));

        int fuel = ship.getFuel();
        int fuelMax = ship.getFuelMax();
        int refuel = 0;
        if (fuel < fuelMax) {
            ship.setFuel(fuelMax);
            refuel = fuelMax - fuel;
        }
        timedLabelList.add(new TimedLabel("+ " + refuel + " FUEL", ship.getX() + 40, ship.getY() + 30, 220));

        float shipHealth = ship.getHealth();
        float shipHealthMax = ship.getHealthMax();
        float repairs = 0;
        if (shipHealth < shipHealthMax) {
            ship.setHealth(shipHealthMax);
            repairs = shipHealthMax - shipHealth;
            timedLabelList.add(new TimedLabel("REPAIRS", ship.getX() + 40, ship.getY() + 50, 240));
        }

        if (ship.getCrew() >= ship.getCrewMax()) {
            game.declareVictory();
        }
    }

    @Override
    public void onDestroyObject(GameObject object) {
        addExplosion(object.getX(), object.getY());

        if (object instanceof Base) {
            Base base = (Base) object;
            if (!base.isVisited()) {
                Game.getInstance().declareDefeat("We lost a crewed station");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Game game = Game.getInstance();
        Ship ship = game.getShip();

        int keyCode = e.getKeyCode();
        //System.out.println("keyCode: "+keyCode);

        if (!game.isGameOver() && !paused) {
            if (keyCode == 65 || keyCode == 37) { // a, left
                ship.rotateLeft();
            } else if (keyCode == 68 || keyCode == 39) { // d, right
                ship.rotateRight();
            } else if (keyCode == 87 || keyCode == 38) { // w, up
                if (ship.thrustForward()) {
                    thrustSprite.setVisible(true);
                }
            } else if (keyCode == 83) { // s

            } else if (keyCode == 9) { // tab
                this.director.pushScene(mapScene);
            }
        }

        if (!game.isGameOver()) {
            if (keyCode == 80) { // p
                paused = !paused;
            }
        }

        if (keyCode == 27) { // Esc
            this.director.popScene();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Game game = Game.getInstance();

        int keyCode = e.getKeyCode();
        if (keyCode == 87 || keyCode == 38) { // w
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

    private void drawTitleText(Graphics2D g, String text) {
        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(48.0f).deriveFont(Font.BOLD);
        g.setFont(defaultFont);
        g.setColor(Color.WHITE);

        Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
        int x = 100;
        int y = 400 - (int)(textBounds.getHeight() / 2);

        g.drawString(text, x, y);
    }

    private void drawContentText(Graphics2D g, String text) {
        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(24.0f).deriveFont(Font.BOLD);
        g.setFont(defaultFont);
        g.setColor(Color.WHITE);

        Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
        int x = 100;
        int y = 500 - (int)(textBounds.getHeight() / 2);

        g.drawString(text, x, y);
    }

    private void drawControlText(Graphics2D g, String text) {
        Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(24.0f).deriveFont(Font.BOLD);
        g.setFont(defaultFont);
        g.setColor(Color.WHITE);

        Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
        int x = 100;
        int y = 600 - (int)(textBounds.getHeight() / 2);

        g.drawString(text, x, y);
    }

    private void addExplosion(double x, double y) {
        BufferedImage[] anim = ImageResource.getAnimation(ImageResource.EXPLOSION);
        explosionSprite = new AnimatedSprite(anim, x, y, 8, false);
        explosionSprite.setRotation(45.0f);
        explosionList.add(explosionSprite);
    }

    private void drawBar(Graphics2D g, int x, int y, double amount, double amountMax, int width, int height, boolean progress) {
        double perc = amount / amountMax;
        double fillWidth = 0;
        int gv = 0;
        int rv = 0;
        if (progress) {
            fillWidth = width - (width * perc);
            gv = (int)(255 * (1 - perc));
            rv = (int)(255 * (perc));
        } else {
            fillWidth = width * perc;
            gv = (int)(255 * (perc));
            rv = (int)(255 * (1 - perc));
        }
        g.setColor(new Color(rv, gv, 0, 255));
        g.fillRect(x, y, (int)fillWidth, height);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
    }

    private void drawSprite(Graphics2D g, Sprite sprite, double x, double y, int width, int height) {
        if (!sprite.isVisible()) {
            return;
        }

        double spriteX = sprite.getX();
        double spriteY = sprite.getY();

        // draw gravity radius
        /*if (sprite instanceof GameObject) {
            GameObject object = (GameObject) sprite;

            float gravityRadius = object.getGravityRadius();

            if (gravityRadius > 0) {
                double centerX = object.getX() + object.getAnchorX();
                double centerY = object.getY() + object.getAnchorY();

                g.setColor(Color.GREEN);
                g.drawOval((int) Math.rint(centerX - gravityRadius - cameraX), (int) Math.rint(centerY - gravityRadius - cameraY), (int) (gravityRadius * 2), (int) (gravityRadius * 2));
            }
        }*/

        // draw collection circles
        if (sprite instanceof Base) {
            Base base = (Base) sprite;

            if (!base.isVisited()) {
                float collectionRadius = base.getCollectionRadius();

                // only works if anchorX is guaranteed to be at the center
                double centerX = base.getX() + base.getAnchorX();
                double centerY = base.getY() + base.getAnchorY();

                g.setColor(Color.RED);
                g.drawOval((int) Math.rint(centerX - collectionRadius - cameraX), (int) Math.rint(centerY - collectionRadius - cameraY), (int) (collectionRadius * 2), (int) (collectionRadius * 2));
            }
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

        if (sprite instanceof Planet) {
            Planet planet = (Planet)sprite;

            Font defaultFont = FontResource.getFont(FontResource.DEFAULT).deriveFont(16.0f).deriveFont(Font.PLAIN);
            g.setFont(defaultFont);
            g.setColor(Color.BLUE);
            String text = planet.getName();
            Rectangle textBounds = g.getFontMetrics(defaultFont).getStringBounds(text, g).getBounds();
            g.drawString(text, (int) (sprite.getX() + sprite.getAnchorX() - textBounds.getWidth() / 2 - cameraX), (int) (sprite.getY() + sprite.getAnchorY() - cameraY));
        }
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
