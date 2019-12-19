package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Base extends Construct {
    private float collectionRadius;
    private boolean visited;

    public Base(double x, double y) {
        super(ImageResource.getImage(ImageResource.BASE), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setCollectionRadius(getImage().getWidth() * 2.5f);
        setMass(400);
        setDestructible(true);
        setImpactResistance(2.8f);
        setHealthMax(15);
        setHealth(getHealthMax());
        setCrewMax(30);
        setCrew(30);
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
}
