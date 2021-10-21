package cnc;

import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
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
class BuildState extends BasicGameState {
	Vector mouseTile = new Vector(0,0);
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		CropGame cg = (CropGame)game;
		cg.setTimer(CropGame._BUILDLENGTH);
		cg.deltaMult = 1;
	}

	//TODO: show ghost of sprout and firing radius when placing object when not hovering over existing crop/wall

	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		CropGame cg = (CropGame)game;

		//draw all tiles
		for (Tile tile : cg.tiles) {
			tile.render(g);
		}

		for (Crop crop : cg.crops) {
			crop.render(g);
		}

		cg.base.render(g);

		UI.renderUI(cg, g, mouseTile);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		CropGame cg = (CropGame)game;
		Input input = container.getInput();
		Vector mousePos = new Vector(input.getMouseX(), input.getMouseY());
		mouseTile = Tile.getTileCoordFromPixPos(mousePos.getX() , mousePos.getY());
		int tileIndex = Tile.getTileIndexFromTilePos(mouseTile.getX(), mouseTile.getY());

		//check num keys for new tile selection
		if (input.isKeyPressed(Input.KEY_1))
			cg.shopIndex = 0;
		else if (input.isKeyPressed(Input.KEY_2))
			cg.shopIndex = 1;

		//placing tile
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			//check if user pressing UI button
			if (cg.buttonCD <= 0) {
				if (UI.mouseClickedFF(mousePos)) {
					cg.deltaMult = CropGame._FFMULT;
					cg.buttonCD = CropGame._BUTTONCD;
				} else if (UI.mouseClickedSkip(mousePos)) {
					float timeRemaining = cg.getTimer();
					cg.setTimer(0);
					cg.buttonCD = CropGame._BUTTONCD;
					for (Crop crop : cg.crops) {
						crop.update((int)timeRemaining);
					}
				}
			}


			//attempting to place new tile/entity
			if (!cg.tiles.get(tileIndex).hasCrop()
					&& (cg.tiles.get(tileIndex) instanceof Soil)) {
				//check if a wall or a crop should be placed
				if (cg.shopIndex == 1) {
					if (cg.playerCash >= Wall.cost) {
						cg.playerCash -= Wall.cost;
						cg.tiles.set(tileIndex, new Wall((CropGame._TILESIZE * mouseTile.getX())+(CropGame._TILESIZE/2),
								(CropGame._TILESIZE * mouseTile.getY())+(CropGame._TILESIZE/2), cg));
						cg.pathing.generateNodeList(cg);
					}
				} else {
					createCrop(cg);
					cg.pathing.generateNodeList(cg);
				}
			}
		}

		//removing tile
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			//if crop
			if (cg.tiles.get(tileIndex).hasCrop()) {
				cg.playerCash += cg.tiles.get(tileIndex).getCrop().getValue();
				cg.crops.remove(cg.tiles.get(tileIndex).getCrop());
				cg.tiles.get(tileIndex).setCrop(null);
				cg.pathing.generateNodeList(cg);
			}
			//if wall
			else if (cg.tiles.get(tileIndex) instanceof Wall) {
				cg.playerCash += Wall.cost;
				cg.tiles.set(tileIndex, new Soil(cg.tiles.get(tileIndex).getX(), cg.tiles.get(tileIndex).getY(), cg));
				cg.pathing.generateNodeList(cg);
			}
		}

		if (input.isKeyPressed(Input.KEY_P)) {
			cg.debug = !cg.debug;
		}

		cg.buttonCD -= delta;

		delta = (int)(delta * cg.deltaMult);

		cg.setTimer(cg.getTimer() - delta);

		for (Crop crop : cg.crops) {
			crop.update(delta);
		}

		ArrayList<Enemy> enemyKillList = new ArrayList<>();
		ArrayList<Bullet> bulletKillList = new ArrayList<>();

		for (Bullet bullet : cg.bullets) {
			bullet.update(delta);
			if (bullet.awaitingRemoval)
				bulletKillList.add(bullet);
		}

		for (Enemy enemy : cg.enemies) {
			enemy.update(delta);
			if (enemy.isAwaitingDeath())
				enemyKillList.add(enemy);
		}

		for (Enemy enemy : enemyKillList) {
			for (Bullet bullet : cg.bullets) {
				bullet.setTarget(null);
			}
			cg.enemies.remove(enemy);
		}

		for (Bullet bullet : bulletKillList) {
			cg.bullets.remove(bullet);
		}
	}

	@Override
	public int getID() {
		return CropGame.BUILDSTATE;
	}

	private void createCrop(CropGame cg) {
		//TODO: switch based on shop index to create different crops
		if (cg.playerCash >= Sunflower.cost) {
			cg.playerCash -= Sunflower.cost;
			Sunflower crop = new Sunflower((CropGame._TILESIZE * mouseTile.getX()) + (CropGame._TILESIZE/2.0f),
					(CropGame._TILESIZE * mouseTile.getY()) + (CropGame._TILESIZE/2.0f), cg);

			cg.crops.add(crop);
			cg.tiles.get(Tile.getTileIndexFromTilePos(mouseTile.getX(), mouseTile.getY())).setCrop(crop);

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
}