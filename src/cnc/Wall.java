package cnc;

import jig.ResourceManager;

public class Wall extends Tile {
    public Wall (float x, float y) {
        super(x, y, true, 5);

        this.setSprite(CropGame.WALL_IMG_RSC);
    }
}
