import java.awt.Image;

public class Wall extends Entity {
    public Wall(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }

    @Override
    protected void updateVelocity() {
        // Wall doesn't move
    }
}