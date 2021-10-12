package cnc;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public abstract class Enemy extends Entity {
    private int health;
    private String sprite;
    private int distToDest;
    private final CropGame cg;
    private final Dijkstra pathing;
    private Dijkstra.Node src;
    private Dijkstra.Node destination;
    private boolean awaitingDeath;
    private float damageValue;

    public Enemy (float _x, float _y, int _health, String _sprite, CropGame game) {
        super(_x, _y);
        health = _health;
        sprite = _sprite;
        addImageWithBoundingBox(ResourceManager.getImage(_sprite));
        distToDest = 0;
        cg = game;
        pathing = cg.pathing;
        src = pathing.nodeList.get(Tile.getTileIndexFromPixPos(_x, _y));
        destination = pathing.nodeList.get(src.nextTileIndex);
        distToDest = CropGame._TRAVELTIME;
        awaitingDeath = false;
        damageValue = 1;
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
        if (destination != null && distToDest < 0) {
            src = destination;
            destination = pathing.nodeList.get(destination.nextTileIndex);
            distToDest = CropGame._TRAVELTIME;
        }

        if (destination != null) {
            Tile destTile = cg.tiles.get(destination.index);

            if (destTile.hasCrop() || destTile instanceof Wall) {
                destTile.damage(damageValue * ((float)delta/CropGame._TRAVELTIME));
            } else {
                translate(new Vector(src.xPos, src.yPos), new Vector(destination.xPos, destination.yPos), delta);
            }

        } else {
            //TODO: ACCOUNT FOR WELL ONCE IMPLEMENTED
            Tile srcTile = cg.tiles.get(src.index);
            if (srcTile.hasBase()) {
                awaitingDeath = true;
                srcTile.getBase().setHealth(srcTile.getBase().getHealth() - 1);
            }
            //awaitingDeath = true;
        }
    }

    private void translate(Vector oldLoc, Vector newLoc, int delta) {
        float relX = newLoc.getX() - oldLoc.getX();
        float relY = newLoc.getY() - oldLoc.getY();

        float xTrans = delta * (relX / CropGame._TRAVELTIME);
        float yTrans = delta * (relY / CropGame._TRAVELTIME);

        this.setX(this.getX() + xTrans);
        this.setY(this.getY() + yTrans);

        distToDest -= delta;
    }

    public boolean isAwaitingDeath() {
        return awaitingDeath;
    }
}
