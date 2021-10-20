package cnc;

import jig.*;
import org.newdawn.slick.Animation;

public class Bullet extends Entity {
    //private final static float bulletSpeed = (10 * CropGame._TILESIZE);
    private final static float speedScale = 2.0f;
    private final Animation bulletAnim;
    private Enemy target;
    private final float damage;
    private  final CropGame cg;
    public boolean awaitingRemoval;
    private Vector dirVec;

    public Bullet (float x, float y, Enemy _target, float _damage, CropGame _game) {
        super(x, y);

        bulletAnim = new Animation(ResourceManager.getSpriteSheet(CropGame.BULLET_ANIM_RSC, 20, 20),
                0, 0, 1, 0, true, 50, true);
        addAnimation(bulletAnim);
        bulletAnim.setLooping(true);

        target = _target;
        System.out.println("Creating bullet w/ target @" + target.getX() + ", " + target.getY());

        damage = _damage;
        cg = _game;
        awaitingRemoval = false;

        this.addShape(new ConvexPolygon(20.0f, 20.0f));
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public void update(int delta) {
        if (target != null) {
            //moving towards target at
            dirVec = new Vector(target.getX() - getX(), target.getY() - getY());
            dirVec = dirVec.unit();
            this.setX((float)(this.getX() + (dirVec.getX() * (delta/speedScale))));
            this.setY((float)(this.getY() + (dirVec.getY() * (delta/speedScale))));

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

        if (getX() < 0 ||
            getX() > CropGame._SCREENWIDTH ||
            getY() < 0 ||
            getY() > CropGame._SCREENHEIGHT) {
            awaitingRemoval = true;
        }
    }
}
