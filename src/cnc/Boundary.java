package cnc;

import jig.ResourceManager;

public class Boundary extends Tile {

    public Boundary(float x, float y) {
        super(x, y, false, 0);

        this.setSprite(CropGame.BOUNDARY_IMG_RSC);
        addImageWithBoundingBox(ResourceManager.getImage(this.getSprite()));
    }
}
