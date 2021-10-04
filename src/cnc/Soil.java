package cnc;

import jig.ResourceManager;

public class Soil extends Tile {

    public Soil(float x, float y) {
        super(x, y, true, 0);

        this.setSprite(CropGame.SOIL_IMG_RSC);
        addImageWithBoundingBox(ResourceManager.getImage(this.getSprite()));
    }
}
