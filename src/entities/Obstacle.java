package entities;

public class Obstacle {
    private final int width;
    private final int height;
    private int position;
    private boolean passed = false;

    public Obstacle(int i) {
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

    public void passed() {
        passed = true;
    }

    public boolean isPassed() {
        return passed;
    }
}
