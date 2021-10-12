package cnc;

import jig.ResourceManager;

public class Boundary extends Tile {

    public Boundary(float x, float y, CropGame game) {
        super(x, y, false, 0, game);

        this.setSprite(CropGame.BOUNDARY_IMG_RSC);
        addImageWithBoundingBox(ResourceManager.getImage(this.getSprite()));
    }
}
