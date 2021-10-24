package cnc;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import java.util.ArrayList;

public class PiercerBullet extends Entity {
    private static final float speedScale = 2;
    private static final float damage = 3;
    private final Vector velocity;
    private final ArrayList<Enemy> consumed;
    public boolean awaitingRemoval = false;
    private final CropGame cg;


    public PiercerBullet(float x, float y, Vector _velocity, CropGame game) {
        super(x, y);
        addImageWithBoundingBox(ResourceManager.getImage(CropGame.PIERCER_BULLET_IMG_RSC));

        velocity = (_velocity.unit());
        consumed = new ArrayList<>();
        cg = game;
    }

    public void update(int delta) {
        this.setX((this.getX() + (velocity.getX() * (delta/speedScale))));
        this.setY((this.getY() + (velocity.getY() * (delta/speedScale))));

        for (Enemy enemy : cg.enemies) {
            if (!consumed.contains(enemy) && (this.collides(enemy) != null)) {
                System.out.println("Damaging enemy");
                enemy.setHealth(enemy.getHealth() - damage);
                consumed.add(enemy);
            }
        }

        if (Bullet.offScreen(this)) {
            this.awaitingRemoval = true;
        }
    }
}
