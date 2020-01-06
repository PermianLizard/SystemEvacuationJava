package permianlizard.se.game;

public interface GameEventListener {

    void onBaseVisit(Base base);
    void onDestroyObject(GameObject object);
}
