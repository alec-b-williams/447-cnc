package cnc;

public class Imp extends Enemy {
    public Imp(float _x, float _y, Dijkstra _pathing) {
        super(_x, _y, 5, CropGame.IMP_ENEMY_IMG_RSC, _pathing);
    }
}
