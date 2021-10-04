package cnc;

import cnc.Crop;
import cnc.CropGame;
import jig.ResourceManager;

public class Sunflower extends Crop {

    public Sunflower (float x, float y) {
        super(x, y, CropGame.SPROUT_IMG_RSC, 5, 5000, 2, 2);
    }

    public void update(int delta) {
        if (!this.isMature()) {
            this.setAge(this.getAge() + delta);

            if (this.isMature()) {
                this.setSprite(CropGame.SUNFLOWER_IMG_RSC);
                this.setY(this.getY()-43);
                this.setValue(6);
            }
        }
    }
}
