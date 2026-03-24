import java.awt.Image;

public class Food extends Entity {
    public Food(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }

    @Override
    protected void updateVelocity() {
        // Food doesn't move
    }
}