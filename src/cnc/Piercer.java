package cnc;

import jig.ConvexPolygon;
import jig.Vector;

public class Piercer extends Crop {
    public static final float sproutHealth = 1;
    public static final float matureHealth = 10;
    public static final float timeToMature = 60000;
    public static final int cost = 20;
    public static final float value = 30;
    public static final float cooldown = 2000;
    public static final float attackRadius = 4.5f;
    private float currCD = 0;

    public Piercer (float _x, float _y, CropGame game) {
        super(_x, _y, CropGame.PIERCER_SPROUT_IMG_RSC, sproutHealth, timeToMature, cost, game);
        addShape(new ConvexPolygon(attackRadius * CropGame._TILESIZE), new Vector(0, 32));
    }

    public void update(int delta) {
        super.update(delta);

        if (this.hasMatured(delta)) {
            this.setSprite(CropGame.PIERCER_IMG_RSC);
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
                    cg.piercerBullets.add(new PiercerBullet(this.getX(), this.getY()-40,
                            new Vector(enemy.getX() - getX(), enemy.getY() - (this.getY()-40)),
                            cg));
                    currCD = cooldown;
                }
            }
        }
    }
}
