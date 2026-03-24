import java.awt.Image;

public class Player extends Entity {
    private static final int TILE_SIZE = 32;
    private char desiredDirection = 'R';

    public Player(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
        this.desiredDirection = this.direction;
    }

    public void setDesiredDirection(char desiredDirection) {
        this.desiredDirection = desiredDirection;
    }

    public char getDesiredDirection() {
        return desiredDirection;
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

    public void stop() {
        this.velocityX = 0;
        this.velocityY = 0;
    }
}