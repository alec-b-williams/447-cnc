package cnc;

import jig.ResourceManager;
import org.newdawn.slick.Animation;

public class Boundary extends Tile {

    public Boundary(float x, float y, CropGame game) {
        super(x, y, false, 0, game);

        double rand = Math.random();

        if (rand <= 0.5) {
            this.setSprite(CropGame.BOUNDARY_ALT_IMG_RSC);
        } else if ((rand > 0.5) && (rand <= 0.75)) {
            Animation ocean = new Animation(ResourceManager.getSpriteSheet(CropGame.OCEAN_IMG_RSC, 64, 64),
                    0, 0, 7, 0, true, 300, true);
            addAnimation(ocean);
            ocean.setLooping(true);
        } else {
            Animation ocean = new Animation(ResourceManager.getSpriteSheet(CropGame.OCEAN_ALT_IMG_RSC, 64, 64),
                    0, 0, 7, 0, true, 300, true);
            addAnimation(ocean);
            ocean.setLooping(true);
        }

    }
}
