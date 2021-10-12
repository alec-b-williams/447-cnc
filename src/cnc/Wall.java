package cnc;

import jig.ResourceManager;

public class Wall extends Tile {
    public Wall (float x, float y, CropGame game) {
        super(x, y, true, 5, game);

        this.setSprite(CropGame.WALL_IMG_RSC);
    }
}
