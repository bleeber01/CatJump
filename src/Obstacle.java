import java.awt.*;

public class Obstacle {
    int width;
    int height;
    int dx = 0;
    int position;
    boolean passed = false;

    Obstacle(int i) {
        this.width = 60;
        this.height = 75;
        this.position = i;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public void setdx(int i) {
        this.dx = i;
    }

    public void passed() {
        passed = true;
    }

    public boolean isPassed() {
        return passed;
    }
}
