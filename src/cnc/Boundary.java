package cnc;

import jig.ResourceManager;

public class Boundary extends Tile {

    public Boundary(float x, float y) {
        super(x, y);

        sprite = CropGame.BOUNDARY_IMG_RSC;
        addImageWithBoundingBox(ResourceManager.getImage(sprite));
        traversable = false;
        health = 0;
    }
}
