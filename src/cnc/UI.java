package cnc;

import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class UI {
    public static final float FFminX = CropGame._SCREENWIDTH - 220;
    public static final float FFmaxX = CropGame._SCREENWIDTH - 120;
    public static final float FFminY = 20;
    public static final float FFmaxY = 80;

    public static final float SkipMinX = CropGame._SCREENWIDTH - 110;
    public static final float SkipMaxX = CropGame._SCREENWIDTH - 10;
    public static final float SkipMinY = 20;
    public static final float SkipMaxY = 80;

    public static void renderUI(CropGame cg, Graphics g, Vector mouseTile) {
        g.drawImage(ResourceManager.getImage(CropGame.MOUSE_IMG_RSC),
                mouseTile.getX()*CropGame._TILESIZE, mouseTile.getY()*CropGame._TILESIZE);

        g.drawImage(ResourceManager.getImage(CropGame.FF_IMG_RSC), cg.ScreenWidth - 220, 20);
        g.drawImage(ResourceManager.getImage(CropGame.SKIP_IMG_RSC), cg.ScreenWidth - 110, 20);

        ArrayList<String> shop = new ArrayList<>();
        shop.add("[1] Sunflower, Cost: " + Sunflower.cost);
        shop.add("[2] Wall, Cost: " + Wall.cost);

        shop.set(cg.shopIndex, "**" + shop.get(cg.shopIndex) + "**");

        g.drawString("SHOP: ", 10, 70);
        for (int i = 0; i < shop.size(); i++) {
            g.drawString(shop.get(i), 10, 90 + (i * 20));
        }

        g.drawString("Base Health: " + cg.base.getHealth(), 10, 200);

        int minutes = (int)((cg.getTimer() / 1000)  / 60);
        String seconds = String.format("%02d", (int)((cg.getTimer() / 1000) % 60));

        g.drawString("Time left - " + minutes + ":" + seconds, 500, 10);
        g.drawString("Player Cash: " + cg.playerCash, 10, 50);

        Tile activeTile = cg.tiles.get(Tile.getTileIndexFromTilePos(mouseTile));

        if (activeTile.hasCrop() && activeTile.getCrop() instanceof Sunflower) {
            g.drawImage(ResourceManager.getImage(CropGame.FIRING_RAD_IMG_RSC),
                    activeTile.getX()-((Sunflower.attackRadius * CropGame._TILESIZE)),
                    activeTile.getY()-((Sunflower.attackRadius * CropGame._TILESIZE)));
        }

        if (cg.debug) {
            g.drawString("MouseX: " + mouseTile.getX() + ", MouseY: " + mouseTile.getY(), 10, 30);

            /*for (Tile tile : cg.tiles) {
                if (tile.hasCrop()) {
                    g.drawImage(ResourceManager.getImage(CropGame.FIRING_RAD_IMG_RSC),
                            tile.getX()-((Sunflower.attackRadius * CropGame._TILESIZE)),
                            tile.getY()-((Sunflower.attackRadius * CropGame._TILESIZE)));
                }
            }*/

            cg.pathing.nodeList.forEach((key, node) -> {
                if (node.distance < 100)
                    g.drawString("" + Math.round(node.distance * 100.0) / 100.0, node.xPos-10,  node.yPos-10);
            });

            Dijkstra.Node currentNode = cg.pathing.nodeList.get(Tile.getTileIndexFromTilePos(mouseTile.getX(), mouseTile.getY()));

            while (currentNode != null) {
                g.drawImage(ResourceManager.getImage(CropGame.MOUSE_IMG_RSC), currentNode.xPos-CropGame._TILESIZE/2, currentNode.yPos-CropGame._TILESIZE/2);
                currentNode = cg.pathing.nodeList.get(currentNode.nextTileIndex);
            }
        }
    }

    public static boolean mouseClickedFF(Vector mousePos) {
        float x = mousePos.getX();
        float y = mousePos.getY();

        return x >= FFminX &&
                x <= FFmaxX &&
                y >= FFminY &&
                y <= FFmaxY;
    }

    public static boolean mouseClickedSkip(Vector mousePos) {
        float x = mousePos.getX();
        float y = mousePos.getY();

        return x >= SkipMinX &&
                x <= SkipMaxX &&
                y >= SkipMinY &&
                y <= SkipMaxY;
    }
}
