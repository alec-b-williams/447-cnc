package cnc;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
	public static final int _BUILDLENGTH = 120000;
	public static final int _WAVELENGTH = 60000;
	public static final int _BUTTONCD = 100;
	public static final int _FFMULT = 3;
	public static final float _TITLEDURATION = 2000;

	//States
	public static final int STARTUPSTATE = 0;
	public static final int BUILDSTATE = 1;
	public static final int WAVESTATE = 2;
	public static final int GAMEOVERSTATE = 3;

	//Resources
	public static final String BOUNDARY_IMG_RSC = "cnc/resource/boundary.png";
	public static final String SOIL_IMG_RSC = "cnc/resource/soil.png";
	public static final String WALL_IMG_RSC = "cnc/resource/wall.png";
	public static final String MOUSE_IMG_RSC = "cnc/resource/mouse.png";
	public static final String SUNFLOWER_SPROUT_IMG_RSC = "cnc/resource/sprout.png";
	public static final String SUNFLOWER_IMG_RSC = "cnc/resource/sunflower.png";
	public static final String IMP_ENEMY_IMG_RSC = "cnc/resource/imp.png";
	public static final String BASE_IMG_RSC = "cnc/resource/base.png";
	public static final String BULLET_ANIM_RSC = "cnc/resource/bullet.png";
	public static final String FIRING_RAD_IMG_RSC = "cnc/resource/firing_radius_transparent.png";
	public static final String FF_IMG_RSC = "cnc/resource/fast_forward.png";
	public static final String SKIP_IMG_RSC = "cnc/resource/skip.png";
	public static final String HORIZON_IMG_RSC = "cnc/resource/horizon.png";
	public static final String MELON_SPROUT_IMG_RSC = "cnc/resource/watermelon_sprout.png";
	public static final String MELON_IMG_RSC = "cnc/resource/watermelon.png";
	public static final String EXPLOSION_IMG_RSC = "cnc/resource/explosion.png";
	public static final String OCEAN_IMG_RSC = "cnc/resource/ocean.png";
	public static final String OCEAN_ALT_IMG_RSC = "cnc/resource/ocean_2.png";
	public static final String BOUNDARY_ALT_IMG_RSC = "cnc/resource/boundary_2.png";
	public static final String JEWEL_SPROUT_IMG_RSC = "cnc/resource/jewel_sprout.png";
	public static final String JEWEL_IMG_RSC = "cnc/resource/jewel.png";
	public static final String PIERCER_BULLET_IMG_RSC = "cnc/resource/piercer_bullet.png";
	public static final String PIERCER_SPROUT_IMG_RSC = "cnc/resource/piercer_sprout.png";
	public static final String PIERCER_IMG_RSC = "cnc/resource/piercer.png";
	public static final String BIGGIE_IMG_RSC = "cnc/resource/biggie.png";
	public static final String TITLE_IMG_RSC = "cnc/resource/title.png";
	public static final String MONEY_IMG_RSC = "cnc/resource/money.png";
	public static final String SHOP_IMG_RSC = "cnc/resource/shop.png";
	public static final String TIME_IMG_RSC = "cnc/resource/time.png";
	public static final String HEALTH_IMG_RSC = "cnc/resource/health.png";

	//public static final String tiles[] = {BOUNDARY_IMG_RSC, SOIL_IMG_RSC, WALL_IMG_RSC};

	//Game vars
	public final int ScreenWidth;
	public final int ScreenHeight;

	public int shopIndex;
	public int level;
	public int wave;
	public ArrayList<Tile> tiles;
	public ArrayList<Crop> crops;
	public ArrayList<Enemy> enemies;
	public ArrayList<Bullet> bullets;
	public ArrayList<PiercerBullet> piercerBullets;
	public Base base;
	public Dijkstra pathing;
	public UI ui;
	public boolean debug = false;
	private float timer;
	public float deltaMult;
	public float buttonCD;
	public int playerCash;

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
		addState(new BuildState());
		addState(new WaveState());
		addState(new GameOverState());

		// load sound

		// load images
		ResourceManager.loadImage(BOUNDARY_IMG_RSC);
		ResourceManager.loadImage(SOIL_IMG_RSC);
		ResourceManager.loadImage(WALL_IMG_RSC);
		ResourceManager.loadImage(MOUSE_IMG_RSC);
		ResourceManager.loadImage(SUNFLOWER_SPROUT_IMG_RSC);
		ResourceManager.loadImage(SUNFLOWER_IMG_RSC);
		ResourceManager.loadImage(IMP_ENEMY_IMG_RSC);
		ResourceManager.loadImage(BASE_IMG_RSC);
		ResourceManager.loadImage(BULLET_ANIM_RSC);
		ResourceManager.loadImage(FIRING_RAD_IMG_RSC);
		ResourceManager.loadImage(FF_IMG_RSC);
		ResourceManager.loadImage(SKIP_IMG_RSC);
		ResourceManager.loadImage(HORIZON_IMG_RSC);
		ResourceManager.loadImage(MELON_SPROUT_IMG_RSC);
		ResourceManager.loadImage(MELON_IMG_RSC);
		ResourceManager.loadImage(EXPLOSION_IMG_RSC);
		ResourceManager.loadImage(OCEAN_IMG_RSC);
		ResourceManager.loadImage(OCEAN_ALT_IMG_RSC);
		ResourceManager.loadImage(BOUNDARY_ALT_IMG_RSC);
		ResourceManager.loadImage(JEWEL_SPROUT_IMG_RSC);
		ResourceManager.loadImage(JEWEL_IMG_RSC);
		ResourceManager.loadImage(PIERCER_BULLET_IMG_RSC);
		ResourceManager.loadImage(PIERCER_SPROUT_IMG_RSC);
		ResourceManager.loadImage(PIERCER_IMG_RSC);
		ResourceManager.loadImage(BIGGIE_IMG_RSC);
		ResourceManager.loadImage(TITLE_IMG_RSC);
		ResourceManager.loadImage(MONEY_IMG_RSC);
		ResourceManager.loadImage(SHOP_IMG_RSC);
		ResourceManager.loadImage(TIME_IMG_RSC);
		ResourceManager.loadImage(HEALTH_IMG_RSC);
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

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
		if (timer <= 0) {
			if (getCurrentState().getID() == BUILDSTATE) {
				enterState(WAVESTATE);
			} else {
				this.enemies.clear();
				enterState(BUILDSTATE);
			}
		}
	}

	public void changeLevel() {

		if (this.level < Levels.levelList.length) {
			this.playerCash = (this.level+1) * 10;

			this.tiles = Levels.generateField(Levels.levelList[this.level], this);

			Tile baseTile = this.tiles.get(Tile.getTileIndexFromTilePos(Levels.levelBaseLocation[this.level]));
			this.base = new Base(baseTile.getX(), baseTile.getY(), this);
			baseTile.setBase(this.base);

			this.crops = new ArrayList<Crop>();
			this.enemies = new ArrayList<Enemy>();
			this.bullets = new ArrayList<>();

			this.pathing.generateNodeList(this);

			this.deltaMult = 1;
		}
	}

	//TODO: sort over ALL entities, not just crops
	public void sortEntitiesForRender() {
		//sort crops by y-position so that sprites overlap correctly when rendered
		//src: https://stackoverflow.com/questions/2784514/
		Collections.sort(this.crops, new Comparator<Crop>() {
			@Override
			public int compare(Crop o1, Crop o2) {
				return (int)(o1.getY() - o2.getY());
			}
		});
	}

	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new CropGame("Crops & Crossbows", _SCREENWIDTH, _SCREENHEIGHT));
			app.setDisplayMode(_SCREENWIDTH, _SCREENHEIGHT, true);
			app.setVSync(true);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
