package cnc;

import jig.Vector;

import java.util.ArrayList;

public class Levels {
    public static final int level1[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    public static final int[][][] levelList = {level1};
    public static final Vector[] levelBaseLocation = {new Vector(9.0f, 13.0f)};
    public static final Vector[] enemySpawnLocation = {new Vector(14.0f, 4.0f)};
    public static final float[][][] enemySpawnTimes = {
            //level 1
            {   //wave 1
                {1, 1.5f, 2, 2.5f, 3},
                //wave 2
                {1, 1.5f, 2, 2.5f, 3, 6, 6.5f, 7, 7.5f, 8},
            }
    };

    public static ArrayList<Tile> generateField(int level[][], CropGame cg) {
        ArrayList<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                switch (level[i][j]) {
                    case (0):
                        tiles.add(new Boundary((CropGame._TILESIZE * j)+(CropGame._TILESIZE/2),
                                               (CropGame._TILESIZE * i)+(CropGame._TILESIZE/2), cg));
                        break;
                    case (1):
                        tiles.add(new Soil((CropGame._TILESIZE * j)+(CropGame._TILESIZE/2),
                                           (CropGame._TILESIZE * i)+(CropGame._TILESIZE/2), cg));
                        break;
                    case (2):
                        tiles.add(new Wall((CropGame._TILESIZE * j)+(CropGame._TILESIZE/2),
                                           (CropGame._TILESIZE * i)+(CropGame._TILESIZE/2), cg));
                        break;
                    default:
                }
            }
        }

        return tiles;
    }

}
