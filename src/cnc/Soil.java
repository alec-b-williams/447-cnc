package cnc;

import jig.ResourceManager;

public class Soil extends Tile {

    public Soil(float x, float y) {
        super(x, y);

        sprite = CropGame.SOIL_IMG_RSC;
        addImageWithBoundingBox(ResourceManager.getImage(sprite));
        traversable = true;
        health = 0;
    }
}
