import java.awt.Image;

public abstract class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;
    protected int startX;
    protected int startY;
    protected char direction = 'U'; // U D L R
    protected int velocityX = 0;
    protected int velocityY = 0;

    public Entity(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    public void updateDirection(char direction) {
        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();
    }

    protected abstract void updateVelocity();

    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Image getImage() { return image; }
    public char getDirection() { return direction; }
    public int getVelocityX() { return velocityX; }
    public int getVelocityY() { return velocityY; }

    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setImage(Image image) { this.image = image; }
}