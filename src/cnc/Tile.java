package cnc;

import jig.Entity;

public abstract class Tile extends Entity {
    private final boolean traversable;
    private float health;
    private String sprite;
    private Crop crop = null;

    public Tile(float x, float y, boolean _traversable, int _health) {
        super(x, y);
        traversable = _traversable;
        health = _health;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public String getSprite() {
        return sprite;
    }

    public boolean hasCrop() {
        return (crop != null);
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }
}
