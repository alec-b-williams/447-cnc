package cnc;

import jig.Entity;
import jig.ResourceManager;

public class Base extends Entity {
    private float health;
    private CropGame cg;

    public Base (float _x, float _y, CropGame game) {
        super(_x+32, _y+32);
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
}
