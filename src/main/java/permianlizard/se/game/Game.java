package permianlizard.se.game;

import permianlizard.se.MathUtil;
import permianlizard.se.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

    private static Game instance;

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    boolean gameOver;

    private Ship ship;
    private Sun sun;

    private List<GameObject> objectList;
    private List<Asteroid> asteroidList;
    private List<Base> baseList;
    private List<Planet> planetList;
    private List<Moon> moonList;

    private List<GameEventListener> eventListenersList;

    private Game() {
        eventListenersList = new ArrayList<>();
    }

    public void addEventListener(GameEventListener listener) {
        eventListenersList.add(listener);
    }

    public void removeEventListener(GameEventListener listener) {
        eventListenersList.remove(listener);
    }

    public void newGame() {
        gameOver = false;

        objectList = new ArrayList<>();
        asteroidList = new ArrayList<>();
        baseList = new ArrayList<>();
        planetList = new ArrayList<>();
        moonList = new ArrayList<>();

        generateSystem();
    }

    public void cleanup() {
        gameOver = true;

        objectList = null;
        asteroidList = null;
        baseList = null;
        planetList = null;
        moonList = null;

        ship = null;
        sun = null;
    }

    public void update(double delta) {
        sun.update(delta);

        for (Planet planet : planetList) {
            planet.update(delta);
        }

        for (Moon moon : moonList) {
            moon.update(delta);
        }

        for (Base base : baseList) {
            base.update(delta);
        }

        for (Asteroid asteroid : asteroidList) {
            asteroid.update(delta);
        }

        if (!gameOver) {
            ship.update(delta);

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

                    for (GameEventListener listener : eventListenersList) {
                        listener.onBaseVisit(base);
                    }
                }
            }
        }

        // apply gravity
        for (int a = 0; a < objectList.size() - 1; a++) {
            GameObject objectA = objectList.get(a);

            double aCenterPosX = objectA.getX() + objectA.getAnchorX();
            double aCenterPosY = objectA.getY() + objectA.getAnchorY();

            for (int b = a + 1; b < objectList.size(); b++) {
                GameObject objectB = objectList.get(b);
                float objectBCollisionRadius = objectB.getCollisionRadius();
                if (objectBCollisionRadius <= 0) {
                    continue;
                }
                double bCenterPosX = objectB.getX() + objectB.getAnchorX();
                double bCenterPosY = objectB.getY() + objectB.getAnchorY();

                double distance = MathUtil.distance(aCenterPosX, aCenterPosY, bCenterPosX, bCenterPosY);

                float aGravityRadius = objectA.getGravityRadius();
                float bGravityRadius = objectB.getGravityRadius();

                Vector2D aCenterPos = new Vector2D(aCenterPosX, aCenterPosY);
                Vector2D bCenterPos = new Vector2D(bCenterPosX, bCenterPosY);

                double gravForce = (MathUtil.GRAVITY_CONSTANT * objectA.getMass() * objectB.getMass()) / Math.pow(distance, 2);

                if (!objectA.isStaticObject() && distance < bGravityRadius) {
                    Vector2D gravVectorB = Vector2D.getUnit(Vector2D.sub(bCenterPos, aCenterPos));
                    gravVectorB = Vector2D.mult(Vector2D.mult(gravVectorB, gravForce), delta);
                    objectA.applyForce(gravVectorB.getX(), gravVectorB.getY());
                }

                if (!objectB.isStaticObject() && distance < aGravityRadius) {
                    Vector2D gravVectorA = Vector2D.getUnit(Vector2D.sub(aCenterPos, bCenterPos));
                    gravVectorA = Vector2D.mult(Vector2D.mult(gravVectorA, gravForce), delta);
                    objectB.applyForce(gravVectorA.getX(), gravVectorA.getY());
                }
            }
        }

        // resolve collisions
        for (int a = 0; a < objectList.size() - 1; a++) {
            GameObject objectA = objectList.get(a);
            float objectACollisionRadius = objectA.getCollisionRadius();
            if (objectACollisionRadius <= 0) {
                continue;
            }
            double aCenterPosX = objectA.getX() + objectA.getAnchorX();
            double aCenterPosY = objectA.getY() + objectA.getAnchorY();

            for (int b = a + 1; b < objectList.size(); b++) {
                GameObject objectB = objectList.get(b);
                float objectBCollisionRadius = objectB.getCollisionRadius();
                if (objectBCollisionRadius <= 0) {
                    continue;
                }
                double bCenterPosX = objectB.getX() + objectB.getAnchorX();
                double bCenterPosY = objectB.getY() + objectB.getAnchorY();

                double collisionDistance = objectACollisionRadius + objectBCollisionRadius;
                double distance = MathUtil.distance(aCenterPosX, aCenterPosY, bCenterPosX, bCenterPosY);

                if (distance < collisionDistance) {

                    double overlap = collisionDistance - distance;

                    // resolve collision
                    double dirX = bCenterPosX - aCenterPosX;
                    double dirY = bCenterPosY - aCenterPosY;
                    Vector2D dir = new Vector2D(dirX, dirY);

                    Vector2D aCenterPos = new Vector2D(aCenterPosX, aCenterPosY);
                    Vector2D bCenterPos = new Vector2D(bCenterPosX, bCenterPosY);

                    if (objectA.isStaticObject() && !objectB.isStaticObject()) {
                        dir = Vector2D.setLength(dir, overlap);
                        dir = Vector2D.mult(dir, 2);
                        bCenterPos = Vector2D.add(bCenterPos, dir);

                    } else if (!objectA.isStaticObject() && objectB.isStaticObject()) {
                        dir = Vector2D.setLength(dir, overlap);
                        dir = Vector2D.mult(dir, 2);
                        aCenterPos = Vector2D.sub(aCenterPos, dir);

                    } else {
                        dir = Vector2D.setLength(dir, overlap / 2);
                        dir = Vector2D.mult(dir, 2);
                        aCenterPos = Vector2D.sub(aCenterPos, dir);
                        bCenterPos = Vector2D.add(bCenterPos, dir);
                    }

                    objectA.setX(aCenterPos.getX() - objectA.getAnchorX());
                    objectA.setY(aCenterPos.getY() - objectA.getAnchorY());

                    objectB.setX(bCenterPos.getX() - objectB.getAnchorX());
                    objectB.setY(bCenterPos.getY() - objectB.getAnchorY());

                    Vector2D objectBContactPoint = MathUtil.getCircleClosestPoint(aCenterPos, bCenterPos, objectBCollisionRadius);
                    Vector2D normal = Vector2D.getUnit(Vector2D.sub(objectBContactPoint, bCenterPos));
                    //System.out.println("normal = " + normal);

                    Vector2D objectAIncidence = new Vector2D(objectA.getVelX(), objectA.getVelY());
                    Vector2D objectBIncidence = new Vector2D(objectB.getVelX(), objectB.getVelY());
                    Vector2D incidence = Vector2D.sub(objectAIncidence, objectBIncidence);

                    double massTotal = objectA.getMass() + objectB.getMass();
                    double c1 = objectA.getMass() / massTotal;
                    double c2 = objectB.getMass() / massTotal;

                    double dotProd = Vector2D.dot(incidence, normal);
                    Vector2D result =  Vector2D.sub(incidence, Vector2D.mult(normal, dotProd * 2));
                    //System.out.println("result: " + result);

                    double impactSize = Vector2D.sub(objectBIncidence, objectAIncidence).getLength();
                    System.out.println("impact size: " + impactSize);

                    double angle = Math.abs(Vector2D.getAngleBetween(result, normal));
                    //System.out.println("angle: " + angle);

                    Vector2D objectARefelection = Vector2D.mult(result, c2);
                    Vector2D objectBRefelection = Vector2D.mult(Vector2D.mult(result, -1), c1);
                    //System.out.println("objectARefelection: " + objectARefelection + " objectBRefelection: " + objectBRefelection);

                    if (!objectA.isStaticObject()) {
                        objectA.addVelX(objectARefelection.getX());
                        objectA.addVelY(objectARefelection.getY());
                    }

                    if (!objectB.isStaticObject()) {
                        objectB.addVelX(objectBRefelection.getX());
                        objectB.addVelY(objectBRefelection.getY());
                    }

                    GameObject[] objectsArr = {objectA, objectB};
                    for (GameObject object : objectsArr) {
                        if(object.isDestructible()) {
                            if (impactSize > object.getImpactResistance()) {
                                destroyObject(object);
                            } else {
                                damageObject(object, impactSize);
                            }
                        }
                    }
                }
            }
        }

        // update physics
        for (GameObject object : objectList) {

            if (object.isStaticObject()) {
                continue;
            }

            double velX = object.getVelX();
            double velY = object.getVelY();

            Vector2D vel = new Vector2D(velX, velY);
            if (vel.getLength() > MathUtil.SPEED_LIMIT) {
                vel = Vector2D.setLength(vel, MathUtil.SPEED_LIMIT);
            }

            vel = Vector2D.mult(vel, delta);

            object.translate(vel.getX(), vel.getY());
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Ship getShip() {
        return ship;
    }

    public Sun getSun() {
        return sun;
    }

    public List<GameObject> getObjectList() {
        return objectList;
    }

    public List<Asteroid> getAsteroidList() {
        return asteroidList;
    }

    public List<Base> getBaseList() {
        return baseList;
    }

    public List<Planet> getPlanetList() {
        return planetList;
    }

    public List<Moon> getMoonList() {
        return moonList;
    }

    private void setSun(Sun sun) {
        this.sun = sun;
        objectList.add(sun);
    }

    private void setShip(Ship ship) {
        this.ship = ship;
        objectList.add(ship);
    }

    private void addPlanet(Planet planet) {
        planetList.add(planet);
        objectList.add(planet);
    }

    private void addMoon(Moon moon) {
        moonList.add(moon);
        objectList.add(moon);
    }

    private void addAsteroid(Asteroid asteroid) {
        asteroidList.add(asteroid);
        objectList.add(asteroid);
    }

    private void addBase(Base base) {
        baseList.add(base);
        objectList.add(base);
    }

    private void damageObject(GameObject object, double amount) {
        System.out.println("damage: " + amount);
        float health = object.getHealth();
        health -= amount;
        if (health <= 0) {
            object.setHealth(0);
            destroyObject(object);
        } else {
            object.setHealth(health);
        }
    }

    private void destroyObject(GameObject object) {
        objectList.remove(object);

        if (object instanceof Ship) {
            gameOver = true;
            ship = null;
        } else if (object instanceof Asteroid) {
            asteroidList.remove(object);
        } else if (object instanceof Base) {
            baseList.remove(object);
        }

        for (GameEventListener listener : eventListenersList) {
            listener.onDestroyObject(object);
        }
    }

    private void setOrbit(GameObject objectA, GameObject objectB, double distance, double angleInDegrees, boolean clockwise) {

        double radians = Math.toRadians(angleInDegrees);
        double x = objectB.getX() + objectB.getAnchorX() - objectA.getAnchorX() + distance * Math.cos(radians);
        double y = objectB.getY() + objectB.getAnchorY() - objectA.getAnchorY() + distance * Math.sin(radians);

        objectA.setX(x);
        objectA.setY(y);

        double aCenterPosX = objectA.getX() + objectA.getAnchorX();
        double aCenterPosY = objectA.getY() + objectA.getAnchorY();

        double bCenterPosX = objectB.getX() + objectB.getAnchorX();
        double bCenterPosY = objectB.getY() + objectB.getAnchorY();

        Vector2D aCenterPos = new Vector2D(aCenterPosX, aCenterPosY);
        Vector2D bCenterPos = new Vector2D(bCenterPosX, bCenterPosY);

        Vector2D gravVec = Vector2D.getUnit(Vector2D.sub(aCenterPos, bCenterPos));
        Vector2D orbitVec = new Vector2D(gravVec.getY(), gravVec.getX());

        if (clockwise) {
            orbitVec = new Vector2D(-orbitVec.getX(), orbitVec.getY());
        } else {
            orbitVec = new Vector2D(orbitVec.getX(), -orbitVec.getY());
        }

        double mag = Math.sqrt((MathUtil.GRAVITY_CONSTANT * objectB.getMass()) / distance);
        orbitVec = Vector2D.mult(orbitVec, mag);

        objectA.setVelX(orbitVec.getX());
        objectA.setVelY(orbitVec.getY());
    }

    private void generateSystem() {

        Random random = new Random();

        Sun sun = new Sun(0, 0);
        sun.setX(-sun.getAnchorX());
        sun.setY(-sun.getAnchorY());
        setSun(sun);

        Ship ship = new Ship();
        setShip(ship);
        setOrbit(ship, sun, 375, 0, false);

        double memberPadding = 1200;
        double distance = 0;
        int numPlanets = 7;

        List<Integer> planetAngleList = new ArrayList<>();
        for (int angle = 0; angle < 360; angle += 40) {
            planetAngleList.add(angle);
        }

        List<Integer> satelliteAngleList = new ArrayList<>();
        satelliteAngleList.add(0);
        satelliteAngleList.add(90);
        satelliteAngleList.add(180);
        satelliteAngleList.add(270);

        int angle = 0;
        int index = 0;

        for (int i = 0; i < numPlanets; i++) {
            distance += memberPadding;

            Vector2D planetPos = new Vector2D(0, distance);
            angle = planetAngleList.get(random.nextInt(planetAngleList.size()));
            planetPos = Vector2D.rotate(planetPos, angle);

            Planet planet = null;
            if (random.nextBoolean()) {
                planet = new PlanetA(planetPos.getX(), planetPos.getY());
            } else {
                planet = new PlanetB(planetPos.getX(), planetPos.getY());
            }

            addPlanet(planet);

            List<Integer> angleOptions = new ArrayList<>(satelliteAngleList);
            index = random.nextInt(angleOptions.size());
            angle = angleOptions.get(index);
            angleOptions.remove(index);

            // base
            Base base = new Base(0, 0);
            addBase(base);
            setOrbit(base, planet, 375, angle, false);

            // moons
            // select 0-3 moons
            int numMoons = random.nextInt(4);
            for (int m=0; m < numMoons; m++) {
                Moon moonA = new MoonA(0, 0);
                addMoon(moonA);
                index = random.nextInt(angleOptions.size());
                angle = angleOptions.get(index);
                angleOptions.remove(index);
                setOrbit(moonA, planet, 375, angle, false);
            }
        }
    }
}
