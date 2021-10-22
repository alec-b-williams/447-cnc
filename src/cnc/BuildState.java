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

	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		CropGame cg = (CropGame)game;

		//draw all tiles
		for (Tile tile : cg.tiles) {
			tile.render(g);
		}

		g.drawImage(ResourceManager.getImage(CropGame.HORIZON_IMG_RSC), 0, 1);

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
		else if (input.isKeyPressed(Input.KEY_3))
			cg.shopIndex = 2;

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
				if (cg.shopIndex == 0) {
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
		switch (cg.shopIndex) {
			case (1):
				if (cg.playerCash >= Sunflower.cost) {
					cg.playerCash -= Sunflower.cost;
					Sunflower crop = new Sunflower((CropGame._TILESIZE * mouseTile.getX()) + (CropGame._TILESIZE/2.0f),
							(CropGame._TILESIZE * mouseTile.getY()) + (CropGame._TILESIZE/2.0f), cg);

					cg.crops.add(crop);
					cg.tiles.get(Tile.getTileIndexFromTilePos(mouseTile.getX(), mouseTile.getY())).setCrop(crop);
					cg.sortEntitiesForRender();
					break;
				}
			case (2):
				if (cg.playerCash >= Melon.cost) {
					cg.playerCash -= Melon.cost;
					Melon crop = new Melon((CropGame._TILESIZE * mouseTile.getX()) + (CropGame._TILESIZE/2.0f),
							(CropGame._TILESIZE * mouseTile.getY()) + (CropGame._TILESIZE/2.0f), cg);

					cg.crops.add(crop);
					cg.tiles.get(Tile.getTileIndexFromTilePos(mouseTile.getX(), mouseTile.getY())).setCrop(crop);
					cg.sortEntitiesForRender();
					break;
				}
		}


	}


}