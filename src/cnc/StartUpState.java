package cnc;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the cnc counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(false);
		CropGame cg = (CropGame)game;
		cg.level = 0;
		cg.wave = 0;
		cg.shopIndex = 0;

		cg.tiles = new ArrayList<Tile>();
		cg.tiles = Levels.generateField(Levels.levelList[cg.level], cg);

		Tile baseTile = cg.tiles.get(Tile.getTileIndexFromTilePos(Levels.levelBaseLocation[cg.level]));
		cg.base = new Base(baseTile.getX(), baseTile.getY(), cg);
		baseTile.setBase(cg.base);

		cg.crops = new ArrayList<Crop>();
		cg.enemies = new ArrayList<Enemy>();

		cg.pathing = Dijkstra.getInstance(cg);

		cg.bullets = new ArrayList<>();
		cg.deltaMult = 1;
		cg.playerCash = 10;
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		g.drawString("Press space to start.", 0, 30);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		Input input = container.getInput();
		CropGame cg = (CropGame)game;

		if (input.isKeyPressed(Input.KEY_SPACE)) {
			cg.enterState(CropGame.BUILDSTATE);
		}
	}

	@Override
	public int getID() {
		return CropGame.STARTUPSTATE;
	}
	
}