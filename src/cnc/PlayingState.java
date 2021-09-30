package cnc;

import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the cnc counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	int bounces;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
	}

	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		CropGame cg = (CropGame)game;
		Input input = container.getInput();
		Vector mouseTile = new Vector(input.getMouseX()/64 , input.getMouseY()/64);

		for (Tile tile : cg.tiles) {
			tile.render(g);
		}

		g.drawString("MouseX: " + mouseTile.getX() + ", MouseY: " + mouseTile.getY(), 0, 20);
		g.drawImage(ResourceManager.getImage(CropGame.MOUSE_IMG_RSC), mouseTile.getX()*64, mouseTile.getY()*64);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
	}

	@Override
	public int getID() {
		return CropGame.PLAYINGSTATE;
	}
	
}