package cnc;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public abstract class Enemy extends Entity {
    private int health;
    private String sprite;
    private int distToDest;
    private final Dijkstra pathing;
    private Dijkstra.Node src;
    private Dijkstra.Node destination;

    public Enemy (float _x, float _y, int _health, String _sprite, Dijkstra _pathing) {
        super(_x, _y);
        health = _health;
        sprite = _sprite;
        addImageWithBoundingBox(ResourceManager.getImage(_sprite));
        distToDest = 0;
        pathing = _pathing;
        src = pathing.nodeList.get(Tile.getTileIndexFromPixPos(_x, _y));
        destination = pathing.nodeList.get(src.nextTileIndex);
    }

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public String getSprite() { return sprite; }

    public void setSprite(String sprite) {
        removeImage(ResourceManager.getImage(this.sprite));
        this.sprite = sprite;
        addImageWithBoundingBox(ResourceManager.getImage(this.sprite));
    }

    public void update(int delta) {
        //if standing still, get new destination or attack
        /*if (destination != null) {
            if (distToDest < 0) {
                src = destination;
                destination = pathing.nodeList.get(destination.nextTileIndex);
            }

            translate(new Vector(src.xPos, src.yPos), new Vector(destination.xPos, destination.yPos), delta);
        }*/
    }

    private void translate(Vector oldLoc, Vector newLoc, int delta) {
        /*float relX = newLoc.getX() - this.getX();
        float relY = newLoc.getY() - this.getY();

        float xTrans = delta * (relX / CropGame._TRAVELTIME);
        float yTrans = delta * (relY / CropGame._TRAVELTIME);

        this.setX(this.getX() + xTrans);
        this.setY(this.getY() + yTrans);

        distToDest -= delta;*/
    }
}
