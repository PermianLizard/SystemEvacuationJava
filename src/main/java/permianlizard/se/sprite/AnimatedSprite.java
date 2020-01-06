package permianlizard.se.sprite;

import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite {

    private final BufferedImage[] frames;
    private int currentFrame;
    private final int period;
    private int currentPeriod;
    private boolean playing;
    private boolean loop;
    private boolean done;

    // single frame
    public AnimatedSprite(BufferedImage frame, double x, double y) {
        super(frame, x, y);
        this.frames = new BufferedImage[1];
        this.frames[0] = frame;
        this.period = 0;
        this.playing = false;
        setFrame(0);
    }

    public AnimatedSprite(BufferedImage frame, double x, double y, double anchorX, double anchorY) {
        super(frame, x, y, anchorX, anchorY);
        this.frames = new BufferedImage[1];
        this.frames[0] = frame;
        this.period = 0;
        this.playing = false;
        setFrame(0);
    }

    public AnimatedSprite(BufferedImage[] frames, double x, double y, int period) {
        super(frames[0], x, y);
        this.frames = frames;
        this.period = period;
        setFrame(0);
    }

    public AnimatedSprite(BufferedImage[] frames, double x, double y, int period, boolean loop) {
        this(frames, x, y, period);
        this.playing = true;
        this.loop = loop;
    }

    public void update(double delta) {
        if (playing) {
            if (currentPeriod > 0) {
                currentPeriod--;
            } else {
                if (currentFrame == frames.length - 1 && !loop) {
                    done = true;
                } else {
                    nextFrame();
                }
            }
        }
    }

    public BufferedImage getFrame(int frameIndex) {
        return frames[frameIndex];
    }

    public void setFrame(int frameIndex) {
        setImage(frames[frameIndex]);
        currentFrame = frameIndex;
        currentPeriod = period;
        done = false;
    }

    public void nextFrame() {
        int len = frames.length;
        if (currentFrame >= len - 1) {
            if (loop) {
                currentFrame = 0;
            } else {
                currentFrame = len - 1;
            }
        } else {
            currentFrame++;
        }
        setFrame(currentFrame);
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
        setFrame(this.currentFrame);
    }

    public int getPeriod() {
        return period;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isDone() {
        return done;
    }
}
