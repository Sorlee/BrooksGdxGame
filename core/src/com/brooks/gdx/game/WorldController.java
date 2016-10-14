package com.brooks.gdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.brooks.gdx.game.util.CameraHelper;
import com.brooks.gdx.game.objects.Rock;
import com.brooks.gdx.game.util.Constants;
import com.badlogic.gdx.math.Rectangle;
import com.brooks.gdx.game.objects.Knight;
import com.brooks.gdx.game.objects.Knight.JUMP_STATE;
import com.brooks.gdx.game.objects.Potion;
import com.brooks.gdx.game.objects.Orange;
import com.brooks.gdx.game.objects.Enemy;
import com.badlogic.gdx.Game;
import com.brooks.gdx.game.screens.MenuScreen;

/**
 * Created by: Becky Brooks
 */
public class WorldController extends InputAdapter
{
	//Declare variables
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private float timeLeftGameOverDelay;
	private Game game;
	public float livesVisual;
	public float scoreVisual;
	
	/**
	 * Knight <-> Rock collisions
	 * @param rock
	 */
	private void onCollisionKnightWithRock(Rock rock)
	{
		Knight knight = level.knight;
		float heightDifference = Math.abs(knight.position.y - (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f)
		{
			boolean hitRightEdge = knight.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge)
				knight.position.x = rock.position.x + rock.bounds.width;
			else
				knight.position.x = rock.position.x - knight.bounds.width;
			return;
		}
		
		switch (knight.jumpState)
		{
			case GROUNDED:
				break;
			case FALLING:
			case JUMP_FALLING:
				knight.position.y = rock.position.y + rock.dimension.y;
				knight.jumpState = JUMP_STATE.GROUNDED;
				break;
			case JUMP_RISING:
				knight.position.y = rock.position.y + rock.dimension.y;
				break;
		}
	}
	
	/**
	 * Knight <-> Orange collisions
	 * @param orange
	 */
	private void onCollisionKnightWithOrange(Orange orange)
	{
		orange.collected = true;
		score += orange.getScore();
		if (score < 0)
			score = 0;
		Gdx.app.log(TAG, "Orange collected");
	}
	
	/**
	 * Knight <-> Potion collisions
	 * @param potion
	 */
	private void onCollisionKnightWithPotion(Potion potion)
	{
		potion.collected = true;
		score += potion.getScore();
		if (score < 0)
			score = 0;
		level.knight.setPotionPowerup(true);
		Gdx.app.log(TAG, "Potion collected");
	}
	
	/**
	 * Knight <-> Enemy collisions
	 * @param enemy
	 */
	private void onCollisionKnightWithEnemy(Enemy enemy)
	{
		enemy.collected = true;
		score -= enemy.getScore();
		if (score < 0)
			score = 0;
		Gdx.app.log(TAG, "Enemy hit!");
	}
	
	/**
	 * Initialize the first level
	 */
	private void initLevel()
	{
		score = 0;
		scoreVisual = score;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.knight);
	}

	/**
	 * Initializes the WorldController
	 * @param game
	 */
	public WorldController (Game game)
	{
		this.game = game;
		init();
	}

	/**
	 * Initialize method
	 */
	private void init ()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	/**
	 * Update method
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);
		if (isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0)
				backToMenu();
			else
				handleInputGame(deltaTime);
		}
		handleInputGame(deltaTime);
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInGoo())
		{
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		level.city.updateScrollPosition(cameraHelper.getPosition());
		if (livesVisual > lives)
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
		if (scoreVisual < score)
			scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
		if (scoreVisual > score)
			scoreVisual = Math.max(score, scoreVisual - 250 * deltaTime);
	}

	/**
	 * Handler for the camera movement in order to debug
	 * @param deltaTime
	 */
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;

		if (!cameraHelper.hasTarget(level.knight))
		{
			//Camera controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP))
				moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0, 0);
		}

		//Camera controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
	}

	/**
	 * Move the camera
	 * @param x
	 * @param y
	 */
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	/**
	 * Reset the game world
	 */
	@Override
	public boolean keyUp (int keycode)
	{
		//Reset game world
		if (keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		//Toggle camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.knight);
			Gdx.app.log(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		//Back to menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
			backToMenu();
		return false;
	}
	
	/**
	 * TestCollisions method
	 */
	private void testCollisions()
	{
		r1.set(level.knight.position.x, level.knight.position.y, level.knight.bounds.width, level.knight.bounds.height);
	
		//Test collision: Knight <-> Rocks
		for (Rock rock : level.rocks)
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionKnightWithRock(rock);
			//IMPORTANT: Must do all collisions for valid edge testing on rocks
		}
		
		//Test collision: Knight <-> Oranges
		for (Orange orange : level.oranges)
		{
			if (orange.collected)
				continue;
			r2.set(orange.position.x, orange.position.y, orange.bounds.width, orange.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionKnightWithOrange(orange);
			break;
		}
		
		//Test collision: Knight <-> Potions
		for (Potion potion : level.potions)
		{
			if (potion.collected) continue;
			r2.set(potion.position.x, potion.position.y, potion.bounds.width, potion.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionKnightWithPotion(potion);
			break;
		}
		
		//Test collision: Knight <-> Enemies
		for (Enemy enemy : level.enemies)
		{
			if (enemy.collected) continue;
			r2.set(enemy.position.x, enemy.position.y, enemy.bounds.width, enemy.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionKnightWithEnemy(enemy);
			break;
		}
	}
	
	/**
	 * HandleInputGame method
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime)
	{
		if (cameraHelper.hasTarget(level.knight))
		{
			//Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				level.knight.velocity.x = -level.knight.terminalVelocity.x;
			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
				level.knight.velocity.x = level.knight.terminalVelocity.x;
			else
			{
				//Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop)
					level.knight.velocity.x = level.knight.terminalVelocity.x;
			}
			
			//Knight Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
				level.knight.setJumping(true);
			else
				level.knight.setJumping(false);
		}
	}
	
	/**
	 * IsGameOver method
	 * @return
	 */
	public boolean isGameOver()
	{
		return lives < 0;
	}
	
	/**
	 * IsPlayerInGoo method
	 * @return
	 */
	public boolean isPlayerInGoo()
	{
		return level.knight.position.y < -5;
	}
	
	/**
	 * BackToMenu method
	 */
	private void backToMenu()
	{
		//Switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
}