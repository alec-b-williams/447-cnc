package cnc;

import jig.ConvexPolygon;
import jig.Vector;

public class Sunflower extends Crop {
    public final static float attackRadius = 3.5f;
    public final static float cooldown = 500;
    public final static float bulletDmg = 1f;
    public final static int sproutHealth = 1;
    public final static int matureHealth = 10;
    public final static int timeToMature = 60000;
    public final static int cost = 3;
    public final static int value = 6;
    public float currCD = 0;

    public Sunflower (float x, float y, CropGame game) {
        super(x, y, CropGame.SUNFLOWER_SPROUT_IMG_RSC, sproutHealth, timeToMature, cost, game);
        addShape(new ConvexPolygon(attackRadius * CropGame._TILESIZE), new Vector(0, 32));
    }

    public void update(int delta) {
        super.update(delta);

        if (this.hasMatured(delta)) {
            this.setSprite(CropGame.SUNFLOWER_IMG_RSC);
            this.setY(this.getY()-43);
            this.setValue(value);
            this.setHealth(matureHealth);
            cg.cropMatured();
        }

        currCD -= delta;
        fireBullets();
    }

    private void fireBullets() {
        if (this.isMature()) {
            for (Enemy enemy : cg.enemies) {
                if ((this.collides(enemy) != null) && (currCD <= 0)) {
                    cg.bullets.add(new Bullet(this.getX(), this.getY()-40, enemy, bulletDmg, cg));
                    currCD = cooldown;
                }
            }
        }
    }
}
