package permianlizard.se.game;

import permianlizard.se.ImageResource;

public class Base extends GameObject {

    private float collectionRadius;

    public Base(double x, double y) {
        super(ImageResource.getImage(ImageResource.BASE), x, y);
        setCollisionRadius(getImage().getWidth() / 2);
        setCollectionRadius(getImage().getWidth() * 2f);
    }

    public void onCollide(GameObject other) {

    }

    public float getCollectionRadius() {
        return collectionRadius;
    }

    public void setCollectionRadius(float collectionRadius) {
        this.collectionRadius = collectionRadius;
    }
}
