package cnc;

import jig.Entity;
import jig.ResourceManager;

public abstract class Crop extends Entity {
    private float health;
    private final float timeToMaturity;
    private float age;
    private String sprite;
    private float value;
    public final CropGame cg;

    public Crop (float x, float y, String _sprite, float _health, float ttm, float _value, CropGame game) {
        super(x, y);
        sprite = _sprite;
        addImageWithBoundingBox(ResourceManager.getImage(_sprite));
        health = _health;
        timeToMaturity = ttm;
        age = 0;
        value = _value;
        cg = game;
    }

    public void update(int delta) {
        this.setAge(this.getAge() + delta);
    }

    public float getHealth() { return health; }

    public void setHealth(float health) {
        this.health = health;
        if (this.health < 0) {
            cg.removeCrop(this);
        }
    }

    public void setSprite(String sprite) {
        removeImage(ResourceManager.getImage(this.sprite));
        this.sprite = sprite;
        addImageWithBoundingBox(ResourceManager.getImage(this.sprite));
    }

    public void setAge(float age) { this.age = age; }

    public float getAge() { return age; }

    public boolean isMature() { return (this.age > this.timeToMaturity); }

    public boolean hasMatured(int delta) {
        return (this.isMature() && (this.age - delta < this.timeToMaturity));
    }

    public void setValue(float value) { this.value = value; }

    public float getValue() { return value; }
}
