package cnc;

import jig.ConvexPolygon;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

public class Melon extends Crop {
    public static final float attackRadius = 2.5f;
    public static final float timeToMature = 60000;
    private static final float fuseLength = 1500;
    public static final int cost = 10;
    public static final int value = 12;
    public static final float matureHealth = 5;
    private static final float damage = 3;
    private static final float cooldown = 3000;
    private float currCD = 0;
    private float fuseTimer = fuseLength;
    private Animation explosion;

    public Melon(float x, float y, CropGame game) {
        super(x, y, CropGame.MELON_SPROUT_IMG_RSC, 1, timeToMature, cost, game);
        addShape(new ConvexPolygon(attackRadius * CropGame._TILESIZE), new Vector(0, 0));
    }
    
    public void update(int delta) {
        super.update(delta);
        if (this.hasMatured(delta)) {
            this.setSprite(CropGame.MELON_IMG_RSC);
            this.setValue(value);
            this.setHealth(matureHealth);
            cg.cropMatured();
        }

        currCD -= delta;

        if (this.isMature() && (currCD <= 0))
            checkExplosion(delta);

        if ((explosion != null) && (explosion.isStopped())) {
            removeAnimation(explosion);
            explosion = null;
        }
    }

    private void checkExplosion(int delta) {
        //if enemy in radius & timer hasn't started, start timer
        if (fuseTimer == fuseLength) {
            for (Enemy enemy : cg.enemies) {
                if (this.collides(enemy) != null) {
                    fuseTimer -= delta;
                }
            }
        } else {
            fuseTimer -= delta;
            if (fuseTimer <= 0)
                explode();
        }
    }

    private void explode() {
        fuseTimer = fuseLength;
        currCD = cooldown;

        explosion = new Animation(ResourceManager.getSpriteSheet(CropGame.EXPLOSION_IMG_RSC, 320, 320),
                0, 0, 5, 0, true, 16, true);
        addAnimation(explosion);
        explosion.setLooping(false);

        for (Enemy enemy : cg.enemies) {
            if (this.collides(enemy) != null) {
                enemy.setHealth(enemy.getHealth() - damage);
            }
        }
    }
}
