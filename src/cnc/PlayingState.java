package cnc;

import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Collections;
import java.util.Comparator;


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
	Vector mouseTile;
	
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
		//Vector mouseTile = new Vector(input.getMouseX()/64 , input.getMouseY()/64);

		for (Tile tile : cg.tiles) {
			tile.render(g);
		}

		for (Crop crop : cg.crops) {
			crop.render(g);
		}

		g.drawString("MouseX: " + mouseTile.getX() + ", MouseY: " + mouseTile.getY(), 10, 30);
		g.drawImage(ResourceManager.getImage(CropGame.MOUSE_IMG_RSC), mouseTile.getX()*64, mouseTile.getY()*64);

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			g.drawString("MOUSE DOWN", 10, 50);
		}

		for (int i = 0; i < cg.crops.size(); i++) {
			g.drawString("crop", 10, 50 + (i * 20));
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		CropGame cg = (CropGame)game;
		Input input = container.getInput();
		mouseTile = new Vector(input.getMouseX()/64 , input.getMouseY()/64);

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			System.out.println("Mouse pressed");
			System.out.println("Has crop: " + cg.tiles.get(Levels.getTileIndexFromPos(mouseTile.getX(), mouseTile.getY())).hasCrop());

			int tileIndex = Levels.getTileIndexFromPos(mouseTile.getX(), mouseTile.getY());

			if (!cg.tiles.get(tileIndex).hasCrop() && (cg.tiles.get(tileIndex) instanceof Soil)) {
				Sunflower crop = new Sunflower((64 * mouseTile.getX()) + 32, (64 * mouseTile.getY()) + 32);
				cg.crops.add(crop);
				cg.tiles.get(Levels.getTileIndexFromPos(mouseTile.getX(), mouseTile.getY())).setCrop(crop);

				//sort crops by y-position so that sprites overlap correctly when rendered
				//src: https://stackoverflow.com/questions/2784514/
				Collections.sort(cg.crops, new Comparator<Crop>() {
					@Override
					public int compare(Crop o1, Crop o2) {
						return (int)(o1.getY() - o2.getY());
					}
				});

				System.out.println("adding crop at pos "+ mouseTile.getX()+", "+mouseTile.getY());
			}
		}

		for (Crop crop : cg.crops) {
			((Sunflower)crop).update(delta);
		}
	}

	@Override
	public int getID() {
		return CropGame.PLAYINGSTATE;
	}
	
}