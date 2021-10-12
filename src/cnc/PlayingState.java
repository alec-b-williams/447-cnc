package cnc;

import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
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
class PlayingState extends BasicGameState {
	Vector mouseTile = new Vector(0,0);
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		CropGame cg = (CropGame)game;
		cg.level = 0;
		cg.shopIndex = 0;

		cg.tiles = new ArrayList<Tile>();
		cg.tiles = Levels.generateField(Levels.levelList[cg.level], cg);

		Tile baseTile = cg.tiles.get(Tile.getTileIndexFromTilePos(Levels.levelWellLocation[cg.level]));
		cg.base = new Base(baseTile.getX(), baseTile.getY(), cg);
		baseTile.setBase(cg.base);

		cg.crops = new ArrayList<Crop>();
		cg.enemies = new ArrayList<Enemy>();

		cg.pathing = Dijkstra.getInstance(cg);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		CropGame cg = (CropGame)game;
		Input input = container.getInput();

		for (Tile tile : cg.tiles) {
			tile.render(g);
			//g.drawString("" + Levels.getTileIndexFromPixPos(tile.getX(), tile.getY()), tile.getX(), tile.getY());
		}

		for (Crop crop : cg.crops) {
			crop.render(g);
		}

		for (Enemy enemy : cg.enemies) {
			enemy.render(g);
		}

		cg.base.render(g);

		g.drawString("MouseX: " + mouseTile.getX() + ", MouseY: " + mouseTile.getY(), 10, 30);
		g.drawImage(ResourceManager.getImage(CropGame.MOUSE_IMG_RSC), mouseTile.getX()*64, mouseTile.getY()*64);

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			g.drawString("MOUSE DOWN", 10, 50);
		}

		ArrayList<String> shop = new ArrayList<>();
		shop.add("[1] Sunflower, Cost: 2");
		shop.add("[2] Wall, Cost: 2");
		shop.add("[3] Enemy, Cost: X");

		shop.set(cg.shopIndex, "**" + shop.get(cg.shopIndex) + "**");

		g.drawString("SHOP: ", 10, 70);
		for (int i = 0; i < shop.size(); i++) {
			g.drawString(shop.get(i), 10, 90 + (i * 20));
		}

		g.drawString("Base Health: " + cg.base.getHealth(), 10, 200);

		if (cg.debug) {
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

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		CropGame cg = (CropGame)game;
		Input input = container.getInput();
		mouseTile = Tile.getTileCoordFromPixPos(input.getMouseX() , input.getMouseY());
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

			//attempting to place new tile/entity
			if (!cg.tiles.get(tileIndex).hasCrop()
					&& (cg.tiles.get(tileIndex) instanceof Soil)) {
				//check if a wall or a crop should be placed
				if (cg.shopIndex == 1) {
					cg.tiles.set(tileIndex, new Wall((64 * mouseTile.getX())+32, (64 * mouseTile.getY())+32, cg));
					cg.pathing.generateNodeList(cg);
				} else {
					if (cg.shopIndex == 0)
					createCrop(cg);
					cg.pathing.generateNodeList(cg);
				}
			}
		}

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && cg.shopIndex == 2) {
			cg.enemies.add(new Imp(input.getMouseX(), input.getMouseY(), cg));
		}

		//removing tile
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			//if crop
			if (cg.tiles.get(tileIndex).hasCrop()) {
				cg.crops.remove(cg.tiles.get(tileIndex).getCrop());
				cg.tiles.get(tileIndex).setCrop(null);
				cg.pathing.generateNodeList(cg);
			}
			//if wall
			else if (cg.tiles.get(tileIndex) instanceof Wall) {
				cg.tiles.set(tileIndex, new Soil(cg.tiles.get(tileIndex).getX(), cg.tiles.get(tileIndex).getY(), cg));
				cg.pathing.generateNodeList(cg);
			}
		}

		if (input.isKeyPressed(Input.KEY_P)) {
			cg.debug = !cg.debug;
		}

		for (Crop crop : cg.crops) {
			crop.update(delta);
		}

		ArrayList<Enemy> killList = new ArrayList<>();

		for (Enemy enemy : cg.enemies) {
			enemy.update(delta);
			if (enemy.isAwaitingDeath())
				killList.add(enemy);
		}

		for (Enemy enemy : killList) {
			cg.enemies.remove(enemy);
		}
	}

	@Override
	public int getID() {
		return CropGame.PLAYINGSTATE;
	}

	private void createCrop(CropGame cg) {
		Sunflower crop = new Sunflower((64 * mouseTile.getX()) + 32, (64 * mouseTile.getY()) + 32);
		crop.setListener(cg);
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