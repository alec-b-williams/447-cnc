package cnc;

import jig.ResourceManager;

public class Wall extends Tile {
    public Wall (float x, float y) {
        super(x, y);

        sprite = CropGame.WALL_IMG_RSC;
        addImageWithBoundingBox(ResourceManager.getImage(sprite));
        traversable = true;
        health = 5;
    }
}
