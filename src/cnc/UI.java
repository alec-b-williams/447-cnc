package cnc;

import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Map;

public class UI {
    public static final float FFminX = CropGame._SCREENWIDTH - 220;
    public static final float FFmaxX = CropGame._SCREENWIDTH - 120;
    public static final float FFminY = 20;
    public static final float FFmaxY = 80;

    public static final float SkipMinX = CropGame._SCREENWIDTH - 110;
    public static final float SkipMaxX = CropGame._SCREENWIDTH - 10;
    public static final float SkipMinY = 20;
    public static final float SkipMaxY = 80;

    private static UI singleton_instance = null;
    //private CropGame cg;
    public ArrayList<FloatingText> texts;

    private UI (CropGame game) {
        //cg = game;
        texts = new ArrayList<>();
    }

    //make Dijkstra a singleton
    //from: https://www.geeksforgeeks.org/singleton-class-java/
    public static UI getInstance(CropGame cg) {
        if (singleton_instance == null)
            singleton_instance = new UI(cg);

        return singleton_instance;
    }

    public void renderUI(CropGame cg, Graphics g, Vector mouseTile) {

        //TODO: display text over placed/harvested tiles indicating how much money was spent/gained
        g.drawImage(ResourceManager.getImage(CropGame.MOUSE_IMG_RSC),
                mouseTile.getX()*CropGame._TILESIZE, mouseTile.getY()*CropGame._TILESIZE);

        g.drawImage(ResourceManager.getImage(CropGame.FF_IMG_RSC), cg.ScreenWidth - 220, 20);
        g.drawImage(ResourceManager.getImage(CropGame.SKIP_IMG_RSC), cg.ScreenWidth - 110, 20);

        ArrayList<String> shop = new ArrayList<>();
        shop.add("[1] Wall, Cost: " + Wall.cost);
        shop.add("[2] Moonflower, Cost: " + Sunflower.cost);
        shop.add("[3] Bomb Melon, Cost: " + Melon.cost);

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


        if (mouseInWindow(mouseTile)) {
            Tile activeTile = cg.tiles.get(Tile.getTileIndexFromTilePos(mouseTile));

            if (activeTile.hasCrop() && activeTile.getCrop() instanceof Sunflower) {
                g.drawImage(ResourceManager.getImage(CropGame.FIRING_RAD_IMG_RSC),
                        activeTile.getX()-((Sunflower.attackRadius * CropGame._TILESIZE)),
                        activeTile.getY()-((Sunflower.attackRadius * CropGame._TILESIZE)));
            }
        }

        for (FloatingText text : texts) {
            text.render(g);
        }

        if (cg.debug) {
            g.drawString("MouseX: " + mouseTile.getX() + ", MouseY: " + mouseTile.getY(), 10, 30);

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

    public void update(int delta) {
        ArrayList<FloatingText> toBeRemoved = new ArrayList<>();

        for (FloatingText text : texts) {
            text.update(delta);
            if (text.lifetime >= FloatingText.maxLifetime)
                toBeRemoved.add(text);
        }

        for (FloatingText text : toBeRemoved) {
            texts.remove(text);
        }
    }

    public void addText(int _value, float _x, float _y) {
        this.texts.add(new FloatingText(_value, _x, _y));
    }

    public class FloatingText {
        public static final float maxLifetime = 750.0f;
        private final int value;
        private float lifetime = 0;
        private final float x;
        private final float y;

        public FloatingText(int _value, float _x, float _y) {
            value = _value;
            x = _x;
            y = _y;
        }

        public void update(int delta) {
            lifetime += delta;
        }

        public void render(Graphics g) {
            String prefix;
            if (value >= 0) {
                prefix = "+$";
                //g.setFont();
                g.setColor(new Color(0.0f, 1.0f, 0.0f, (maxLifetime - lifetime)/maxLifetime));
            } else {
                prefix = "-$";
                g.setColor(Color.red);
            }

            g.drawString(prefix + Math.abs(value), x, y - (lifetime/(maxLifetime/32.0f)));
            g.setColor(Color.white);
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

    private static boolean mouseInWindow(Vector mouseTile) {
        return (mouseTile.getX() >= 0 &&
                mouseTile.getX() < (CropGame._SCREENWIDTH/CropGame._TILESIZE) &&
                mouseTile.getY() >= 0 &&
                mouseTile.getY() < (CropGame._SCREENHEIGHT/CropGame._TILESIZE) );
    }
}
