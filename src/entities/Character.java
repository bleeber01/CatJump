package entities;
public class Character {
    private int characterX;
    private int characterY;
    private final int width;
    private final int height;
    private int velocityY;
    private final int velocityX;


    public Character(int x, int y, int w, int h, int dx) {
        this.characterX = x;
        this.characterY = y;
        this.width = w;
        this.height = h;
        this.velocityX = dx;
    }

    public void move(int gravity) {
        setVelocityY(velocityY - gravity); // applies gravity to the velocity of the character
        setCharacterY(characterY - velocityY);// applies velocity to character position
        characterY = Math.min(characterY, 462); // floor
    }

    // jumps if the cat is currently on the ground
    public void jump() {
        if (characterY == 462) {
            setVelocityY(15);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCharacterX() {
        return characterX;
    }

    public int getCharacterY() {
        return characterY;
    }
    public int getVelocityX() {
        return velocityX;
    }

    public void setCharacterX(int i) {
        this.characterX = i;
    }

    public void setCharacterY(int i) {
        this.characterY = i;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int i) {
        velocityY = i;
    }
}
