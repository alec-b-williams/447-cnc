package cnc;

import cnc.Crop;
import cnc.CropGame;
import jig.ResourceManager;
import jig.Vector;

public class Sunflower extends Crop {
    public final static float attackRadius = 3.5f;
    public final static float cooldown = 500;
    public float currCD = 0;

    public Sunflower (float x, float y, CropGame game) {
        super(x, y, CropGame.SPROUT_IMG_RSC, 1, 5000, 2, 2, game);

    }

    public void update(int delta) {
        super.update(delta);

        if (this.hasMatured(delta)) {
            this.setSprite(CropGame.SUNFLOWER_IMG_RSC);
            this.setY(this.getY()-43);
            this.setValue(6);
            this.setHealth(10);
            cg.cropMatured();
        }

        currCD -= delta;
        fireBullets();
    }

    private void fireBullets() {
        if (this.isMature()) {
            for (Enemy enemy : cg.enemies) {
                float distance = (float)(Math.sqrt((enemy.getX() - getX())*(enemy.getX() - getX()) +
                                                  (enemy.getY() - getY())*(enemy.getY() - getY()))/64.0);
                if ((distance < attackRadius) && (currCD <= 0)) {
                    cg.bullets.add(new Bullet(this.getX(), this.getY(), enemy, 1, cg));
                    currCD = cooldown;
                }
            }
        }
    }
}
