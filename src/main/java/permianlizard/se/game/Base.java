package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Base extends GameObject {

    private float collectionRadius;
    private boolean visited;
    private int crew;

    public Base(double x, double y) {
        super(ImageResource.getImage(ImageResource.BASE), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setCollectionRadius(getImage().getWidth() * 2.5f);
        setMass(200);
        setDestructible(true);
        setHealthMax(15);
        setHealth(getHealthMax());
        this.crew = 30;
    }

    public void onCollide(GameObject other) {

    }

    public float getCollectionRadius() {
        return collectionRadius;
    }

    public void setCollectionRadius(float collectionRadius) {
        this.collectionRadius = collectionRadius;
    }

    public void visit() {
        visited = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public int getCrew() {
        return crew;
    }

    public void setCrew(int crew) {
        this.crew = crew;
    }

    public boolean hasCrew() {
        return crew > 0;
    }
}
