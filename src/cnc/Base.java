package cnc;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Base extends Entity {
    private float health;
    private CropGame cg;

    public Base (float _x, float _y, CropGame game) {
        super(_x+31, _y+31);
        health = 20;
        addImageWithBoundingBox(ResourceManager.getImage(CropGame.BASE_IMG_RSC));
        cg = game;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
        if (this.health <= 0) {
            cg.baseDestroyed();
        }
    }

    public boolean insideBase(Tile tile) {
        Vector basePos = Tile.getTileCoordFromPixPos(getX(), getY());
        Vector nodePos = Tile.getTileCoordFromPixPos(tile.getX(), tile.getY());

        return (basePos.getX() == nodePos.getX() ||
                basePos.getX() + 1 == nodePos.getX()) &&
                (basePos.getY() == nodePos.getY() ||
                        basePos.getY() + 1 == nodePos.getY());
    }
}
