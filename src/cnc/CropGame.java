package cnc;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.sql.Array;
import java.util.ArrayList;

/**
 * A Simple Game of Bounce.
 *
 * The game has three states: StartUp, Playing, and GameOver, the game
 * progresses through these states based on the user's input and the events that
 * occur. Each state is modestly different in terms of what is displayed and
 * what input is accepted.
 *
 * In the playing state, our game displays a moving rectangular "ball" that
 * bounces off the sides of the game container. The ball can be controlled by
 * input from the user.
 *
 * When the ball bounces, it appears broken for a short time afterwards and an
 * explosion animation is played at the impact site to add a bit of eye-candy
 * additionally, we play a short explosion sound effect when the game is
 * actively being played.
 *
 * Our game also tracks the number of bounces and syncs the game update loop
 * with the monitor's refresh rate.
 *
 * Graphics resources courtesy of qubodup:
 * http://opengameart.org/content/bomb-explosion-animation
 *
 * Sound resources courtesy of DJ Chronos:
 * http://www.freesound.org/people/DJ%20Chronos/sounds/123236/
 *
 *
 * @author wallaces
 *
 */
public class CropGame extends StateBasedGame {

	//States
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;

	//Resources
	public static final String BOUNDARY_IMG_RSC = "cnc/resource/boundary.png";
	public static final String SOIL_IMG_RSC = "cnc/resource/soil.png";
	public static final String WALL_IMG_RSC = "cnc/resource/wall.png";

	//public static final String tiles[] = {BOUNDARY_IMG_RSC, SOIL_IMG_RSC, WALL_IMG_RSC};

	//Game vars
	public final int ScreenWidth;
	public final int ScreenHeight;

	public int level;
	public ArrayList<Tile> tiles = new ArrayList<Tile>();

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

		level = 1;

		tiles = Levels.generateField(Levels.levelList[level-1]);
	}


	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new CropGame("Bounce!", 1280, 1024));
			app.setDisplayMode(1280, 1024, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}




}
