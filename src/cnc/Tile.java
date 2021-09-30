package cnc;

import jig.Entity;

public abstract class Tile extends Entity {
    public static boolean traversable;
    public static String sprite;
    public int health;

    public Tile(float x, float y) {
        super(x, y);
    }
}
