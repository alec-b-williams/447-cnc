package cnc;

import jig.*;
import org.newdawn.slick.Animation;

public class Bullet extends Entity {
    //private final static float bulletSpeed = (10 * CropGame._TILESIZE);
    private final static float speedScale = 2;
    private Enemy target;
    private static final float damage = 1;
    private  final CropGame cg;
    public boolean awaitingRemoval;
    private Vector dirVec;

    public Bullet (float x, float y, Enemy _target, CropGame _game) {
        super(x, y);

        Animation bulletAnim = new Animation(ResourceManager.getSpriteSheet(CropGame.BULLET_ANIM_RSC, 20, 20),
                0, 0, 1, 0, true, 16, true);
        addAnimation(bulletAnim);
        bulletAnim.setLooping(true);

        target = _target;

        cg = _game;
        awaitingRemoval = false;

        this.addShape(new ConvexPolygon(20.0f, 20.0f));
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public void update(int delta) {
        if (target != null) {
            //moving towards target at
            dirVec = new Vector(target.getX() - getX(), target.getY() - getY());
            dirVec = dirVec.unit();
            this.setX((this.getX() + (dirVec.getX() * (delta/speedScale))));
            this.setY((this.getY() + (dirVec.getY() * (delta/speedScale))));

            if (this.collides(target) != null) {
                target.setHealth(target.getHealth() - damage);
                awaitingRemoval = true;
            }
        } else {
            for (Enemy enemy : cg.enemies) {
                if (this.collides(enemy) != null) {
                    enemy.setHealth(enemy.getHealth() - damage);
                    awaitingRemoval = true;
                }
            }

            if (dirVec != null) {
                this.setX((float)(this.getX() + (dirVec.getX() * (delta/speedScale))));
                this.setY((float)(this.getY() + (dirVec.getY() * (delta/speedScale))));
            } else {
                awaitingRemoval = true;
            }
        }

        if (offScreen(this)) {
            awaitingRemoval = true;
        }
    }

    public static boolean offScreen(Entity e) {
        return (e.getX() < 0 ||
                e.getX() > CropGame._SCREENWIDTH ||
                e.getY() < 0 ||
                e.getY() > CropGame._SCREENHEIGHT);
    }
}
