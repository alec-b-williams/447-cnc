package cnc;

import jig.ResourceManager;

public class Wall extends Tile {
    public static final int cost = 2;
    public static final int health = 5;

    public Wall (float x, float y, CropGame game) {
        super(x, y, true, health, game);

        this.setSprite(CropGame.WALL_IMG_RSC);
    }
}
