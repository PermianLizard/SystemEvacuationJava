package permianlizard.se;

import java.awt.*;
import java.util.Random;

class Star {
    double x, y;
    float depth;
    Color color;

    public Star(double x, double y, float depth, Color color) {
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.color = color;
    }
}

public class Starfield {

    private int size;
    private int width;
    private int height;


    private Star[] starsArr;

    public Starfield(int size, int width, int height) {
        this.size = size;
        this.width = width;
        this.height = height;

        generate();
    }

    public void update(double delay) {

    }

    public void render(Graphics2D g, double offsetX, double offsetY) {
        g.setColor(Color.WHITE);

        for (Star star : starsArr) {
            double x = star.x;
            double y = star.y;
            float depth = star.depth;

            if (x < offsetX) {
                int multiple = (int)(offsetX / width) + 1;
                x = x + width * multiple;
            }

            if (y < offsetY) {
                int multiple = (int)(offsetY / height) + 1;
                y = y + height * multiple;
            }

            x = (x - offsetX * depth) % width ;
            y = (y - offsetY * depth) % height;

            g.setColor(star.color);
            g.drawLine((int)x, (int)y, (int)x, (int)y);
        }

        g.fillOval(10, 10, 5, 5);
    }

    private void generate() {
        starsArr = new Star[size];

        Random rand = new Random();

        for (int i=0; i < size; i++) {

            double x = Math.random() * width;
            double y = Math.random() * height;
            float depth = 0.5f + rand.nextFloat() * (1.0f - 0.5f);

            int red = rand.nextInt((255 - 200) + 1) + 200;
            int green = rand.nextInt((255 - 200) + 1) + 200;
            int blue = rand.nextInt((255 - 200) + 1) + 200;

            Color color = new Color(red, green, blue, 255);

            Star star = new Star(x, y, depth, color);
            starsArr[i] = star;
        }
    }
}
