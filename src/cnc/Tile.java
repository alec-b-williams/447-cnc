package cnc;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

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

    public static int getTileIndexFromTilePos(float x, float y) {
        if (x < 0 || y < 0) { return -1; }
        return (((int)(y * CropGame._TILEWIDTH)) + (int)x);
    }

    public static int getTileIndexFromPixPos(float x, float y) {
        Vector tileCoord = getTileCoordFromPixPos(x, y);
        return getTileIndexFromTilePos(tileCoord.getX(), tileCoord.getY());
    }

    public static Vector getTileCoordFromPixPos(float x, float y) {
        return (new Vector((int)(x/64) , (int)(y/64)));
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
        if (this.sprite != null) { removeImage(ResourceManager.getImage(this.sprite)); }
        this.sprite = sprite;
        addImageWithBoundingBox(ResourceManager.getImage(this.sprite));
    }

    public String getSprite() {
        return sprite;
    }

    public boolean hasCrop() {
        return (crop != null);
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }
}
