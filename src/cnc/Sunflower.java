package cnc;

import cnc.Crop;
import cnc.CropGame;
import jig.ConvexPolygon;
import jig.ResourceManager;
import jig.Shape;
import jig.Vector;
import org.newdawn.slick.geom.Transform;
import org.pushingpixels.substance.api.colorscheme.OliveColorScheme;

public class Sunflower extends Crop {
    public final static float attackRadius = 3.5f;
    public final static float cooldown = 500;
    public final static float bulletDmg = 1f;
    public float currCD = 0;

    public Sunflower (float x, float y, CropGame game) {
        super(x, y, CropGame.SPROUT_IMG_RSC, 1, 5000, 2, 2, game);
        addShape(new ConvexPolygon(attackRadius * CropGame._TILESIZE), new Vector(0, 32));
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
                if ((this.collides(enemy) != null) && (currCD <= 0)) {
                    cg.bullets.add(new Bullet(this.getX(), this.getY()-40, enemy, bulletDmg, cg));
                    currCD = cooldown;
                }
            }
        }
    }
}
