package cnc;

import jig.ResourceManager;

public class Soil extends Tile {

    public Soil(float x, float y, CropGame game) {
        super(x, y, true, 0, game);

        this.setSprite(CropGame.SOIL_IMG_RSC);
    }
}
