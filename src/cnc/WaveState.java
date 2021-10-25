package cnc;

import jig.ResourceManager;
import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;


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
class WaveState extends BasicGameState {
	Vector mouseTile = new Vector(0,0);
	float timeElapsed;
	int spawnIndex;
	float[] spawnTimers;
	float titleDuration;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		CropGame cg = (CropGame)game;
		cg.setTimer(CropGame._WAVELENGTH);
		timeElapsed = 0;
		cg.deltaMult = 1;
		spawnIndex = 0;
		spawnTimers = Levels.enemySpawnTimes[cg.level][cg.wave];
		titleDuration = CropGame._TITLEDURATION;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		CropGame cg = (CropGame)game;
		Input input = container.getInput();

		for (Tile tile : cg.tiles) {
			tile.render(g);
		}

		g.drawImage(ResourceManager.getImage(CropGame.HORIZON_IMG_RSC), 0, 1);

		for (Crop crop : cg.crops) {
			crop.render(g);
		}

		for (Enemy enemy : cg.enemies) {
			enemy.render(g);
		}

		for (Bullet bullet : cg.bullets) {
			bullet.render(g);
		}

		for (PiercerBullet bullet : cg.piercerBullets) {
			bullet.render(g);
		}

		cg.base.render(g);

		cg.ui.renderUI(cg, g, mouseTile, titleDuration);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		CropGame cg = (CropGame)game;
		Input input = container.getInput();
		mouseTile = Tile.getTileCoordFromPixPos(input.getMouseX() , input.getMouseY());
		Vector mousePos = new Vector(input.getMouseX(), input.getMouseY());

		titleDuration -= delta;

		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			//check if user pressing UI button
			if (cg.buttonCD <= 0) {
				if (UI.mouseClickedFF(mousePos)) {
					if (cg.deltaMult == 1) {
						cg.deltaMult = CropGame._FFMULT;
					} else {
						cg.deltaMult = 1;
					}

					cg.buttonCD = CropGame._BUTTONCD;
				}
			}
		}

		if (input.isKeyPressed(Input.KEY_P)) {
			cg.debug = !cg.debug;
		}

		cg.buttonCD -= delta;
		cg.ui.update(delta);

		delta = (int)(delta * cg.deltaMult);

		cg.setTimer(cg.getTimer() - delta);
		timeElapsed += delta;


		for (Crop crop : cg.crops) {
			crop.update(delta);
		}

		ArrayList<Enemy> enemyKillList = new ArrayList<>();
		ArrayList<Bullet> bulletKillList = new ArrayList<>();
		ArrayList<PiercerBullet> pBulletKillList = new ArrayList<>();

		//time to spawn enemy
		if ((spawnIndex != -1) && (timeElapsed > (spawnTimers[spawnIndex] * 1000))) {
			int oldSpawnIndex = spawnIndex;
			if (spawnIndex >= (spawnTimers.length-1)) {
				spawnIndex = -1;
			} else {
				spawnIndex++;
			}
			spawnEnemy(cg, (spawnTimers[oldSpawnIndex] * 1000) % 2 == 0);
		}

		for (Bullet bullet : cg.bullets) {
			bullet.update(delta);
			if (bullet.awaitingRemoval)
				bulletKillList.add(bullet);
		}

		for (PiercerBullet bullet : cg.piercerBullets) {
			bullet.update(delta);
			if (bullet.awaitingRemoval)
				pBulletKillList.add(bullet);
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

		for (PiercerBullet bullet : pBulletKillList) {
			cg.piercerBullets.remove(bullet);
		}

		if ((spawnIndex <= -1) && (cg.enemies.isEmpty())) {
			if (cg.wave >= Levels.enemySpawnTimes[cg.level].length - 1) {
				cg.level++;
				cg.wave = 0;
				if (cg.level >= Levels.levelList.length) {
					cg.enterState(CropGame.GAMEOVERSTATE);
					return;
				}
				else {
					cg.changeLevel();
				}
			} else {
				cg.wave++;
			}

			if (cg.base.getHealth() <= 0)
				cg.enterState(CropGame.GAMEOVERSTATE);
			else
				cg.enterState(CropGame.BUILDSTATE);
		}
	}

	private void spawnEnemy(CropGame cg, boolean biggie) {
		for (int i = 0; i < Levels.enemySpawnLocation[cg.level].length; i++) {
			Tile spawnTile = cg.tiles.get(Tile.getTileIndexFromTilePos(Levels.enemySpawnLocation[cg.level][i]));
			if (biggie)
				cg.enemies.add(new Imp(spawnTile.getX()+getRandOffset(), spawnTile.getY()+getRandOffset(), cg));
			else
				cg.enemies.add(new Biggie(spawnTile.getX()+getRandOffset(), spawnTile.getY()+getRandOffset(), cg));
		}
	}

	//generates a random pixel offset between [-5, 5]
	private float getRandOffset() {
		float rand = (float)(Math.random() * 10);

		if (Math.random() >= 0.5) {
			return rand;
		} else {
			return rand * -1;
		}
	}

	@Override
	public int getID() {
		return CropGame.WAVESTATE;
	}
}