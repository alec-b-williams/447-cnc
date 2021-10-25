package cnc;

import cnc.Crop;

public class Jewel extends Crop {

    public static float sproutHealth = 2;
    public static int cost = 20;
    public static float value = 40;
    public static float timeToMaturity = 180000;

    public Jewel(float _x, float _y, CropGame game) {
        super(_x, _y, CropGame.JEWEL_SPROUT_IMG_RSC, sproutHealth, timeToMaturity, cost, game);
    }

    public void update(int delta) {
        super.update(delta);

        if (this.hasMatured(delta)) {
            this.setSprite(CropGame.JEWEL_IMG_RSC);
            this.setY(this.getY());
            this.setValue(value);
            cg.cropMatured();
        }
    }
}
