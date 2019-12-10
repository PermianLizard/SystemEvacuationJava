package permianlizard.se.game;

import java.awt.image.BufferedImage;

public abstract class Construct extends GameObject {
    private int crewMax;
    private int crew;

    public Construct(BufferedImage frame, double x, double y) {
        super(frame, x, y);
    }

    public int getCrewMax() {
        return crewMax;
    }

    public void setCrewMax(int crewMax) {
        this.crewMax = crewMax;
    }

    public int getCrew() {
        return crew;
    }

    public void setCrew(int crew) {
        this.crew = crew;
    }

    public void addCrew(int amount) {
        this.crew += amount;
    }

    public boolean hasCrew() {
        return crew > 0;
    }
}
