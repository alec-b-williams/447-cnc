package cnc;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * Crops & Crossbows
 *
 * @author alec.b.williams
 *
 */
public class CropGame extends StateBasedGame {

	//Constants
	public static final int _SCREENWIDTH = 1280;
	public static final int _SCREENHEIGHT = 1024;
	public static final int _TILESIZE = 64;
	public static final int _TILEWIDTH = _SCREENWIDTH / _TILESIZE;
	public static final int _TILEHEIGHT = _SCREENHEIGHT / _TILESIZE;
	public static final int _TRAVELTIME = 1000;

	//States
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;

	//Resources
	public static final String BOUNDARY_IMG_RSC = "cnc/resource/boundary.png";
	public static final String SOIL_IMG_RSC = "cnc/resource/soil.png";
	public static final String WALL_IMG_RSC = "cnc/resource/wall.png";
	public static final String MOUSE_IMG_RSC = "cnc/resource/mouse.png";
	public static final String SPROUT_IMG_RSC = "cnc/resource/sprout.png";
	public static final String SUNFLOWER_IMG_RSC = "cnc/resource/sunflower.png";
	public static final String IMP_ENEMY_IMG_RSC = "cnc/resource/imp.png";
	public static final String BASE_IMG_RSC = "cnc/resource/base.png";
	public static final String BULLET_ANIM_RSC = "cnc/resource/bullet.png";
	public static final String FIRING_RAD_IMG_RSC = "cnc/resource/firing_radius_transparent.png";

	//public static final String tiles[] = {BOUNDARY_IMG_RSC, SOIL_IMG_RSC, WALL_IMG_RSC};

	//Game vars
	public final int ScreenWidth;
	public final int ScreenHeight;

	public int shopIndex;
	public int level;
	public ArrayList<Tile> tiles;
	public ArrayList<Crop> crops;
	public ArrayList<Enemy> enemies;
	public ArrayList<Bullet> bullets;
	public Base base;
	public Dijkstra pathing;
	public boolean debug = true;

	/**
	 * Create the BounceGame frame, saving the width and height for later use.
	 *
	 * @param title
	 *            the window's title
	 * @param width
	 *            the window's width
	 * @param height
	 *            the window's height
	 */
	public CropGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());

		// load sound

		// load images
		ResourceManager.loadImage(BOUNDARY_IMG_RSC);
		ResourceManager.loadImage(SOIL_IMG_RSC);
		ResourceManager.loadImage(WALL_IMG_RSC);
		ResourceManager.loadImage(MOUSE_IMG_RSC);
		ResourceManager.loadImage(SPROUT_IMG_RSC);
		ResourceManager.loadImage(SUNFLOWER_IMG_RSC);
		ResourceManager.loadImage(IMP_ENEMY_IMG_RSC);
		ResourceManager.loadImage(BASE_IMG_RSC);
		ResourceManager.loadImage(BULLET_ANIM_RSC);
		ResourceManager.loadImage(FIRING_RAD_IMG_RSC);
	}

	public void cropMatured() {
		this.pathing.generateNodeList(this);
	}

	public void removeCrop(Crop crop) {
		System.out.println("Removing crop");
		this.crops.remove(crop);
	}

	public void destroyTile(Tile tile) {
		Tile newTile = new Soil(tile.getX(), tile.getY(), this);
		tiles.set(Tile.getTileIndexFromPixPos(tile.getX(), tile.getY()), newTile);
	}

	public void baseDestroyed() {
		this.enterState(GAMEOVERSTATE);
	}

	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new CropGame("Crops & Crossbows", _SCREENWIDTH, _SCREENHEIGHT));
			app.setDisplayMode(_SCREENWIDTH, _SCREENHEIGHT, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
