import java.awt.Image;
import java.util.Random;

public class Ghost extends Entity {
    private static final int TILE_SIZE = 32;
    private char[] directions = {'U', 'D', 'L', 'R'};
    private Random random = new Random();
    private Image normalImage;
    private Image scaredImage;
    private char desiredDirection;
    private boolean scared = false;
    private boolean eaten = false;
    private int eatenTimer = 0;
    private static final int EATEN_DURATION = 100; // frames

    public Ghost(Image normalImage, Image scaredImage, int x, int y, int width, int height) {
        super(normalImage, x, y, width, height);
        this.normalImage = normalImage;
        this.scaredImage = scaredImage;
        this.desiredDirection = this.direction;
    }

    public char getDesiredDirection() {
        return desiredDirection;
    }

    public void setDesiredDirection(char desiredDirection) {
        this.desiredDirection = desiredDirection;
    }

    public boolean isAligned(int tileSize) {
        return this.x % tileSize == 0 && this.y % tileSize == 0;
    }

    @Override
    protected void updateVelocity() {
        switch (this.direction) {
            case 'U' -> {
                this.velocityX = 0;
                this.velocityY = -TILE_SIZE / 4;
            }
            case 'D' -> {
                this.velocityX = 0;
                this.velocityY = TILE_SIZE / 4;
            }
            case 'L' -> {
                this.velocityX = -TILE_SIZE / 4;
                this.velocityY = 0;
            }
            case 'R' -> {
                this.velocityX = TILE_SIZE / 4;
                this.velocityY = 0;
            }
            default -> {
            }
        }
    }

    public void move() {
        this.x += this.velocityX;
        this.y += this.velocityY;
    }

    public void changeDirectionRandomly() {
        char newDirection = directions[random.nextInt(4)];
        updateDirection(newDirection);
    }

    public void setScared(boolean scared) {
        this.scared = scared;
        setImage(scared ? scaredImage : normalImage);
    }

    public boolean isScared() {
        return scared;
    }

    public void eat() {
        eaten = true;
        eatenTimer = EATEN_DURATION;
        reset();
    }

    public void updateEaten() {
        if (eaten && eatenTimer > 0) {
            eatenTimer--;
            if (eatenTimer == 0) {
                eaten = false;
                changeDirectionRandomly();
            }
        }
    }

    public boolean isEaten() {
        return eaten;
    }

    public void resetEaten() {
        eaten = false;
        eatenTimer = 0;
    }
}