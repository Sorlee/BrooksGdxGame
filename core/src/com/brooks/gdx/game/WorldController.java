package com.brooks.gdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.brooks.gdx.game.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.brooks.gdx.game.objects.Rock;
import com.brooks.gdx.game.util.Constants;
import com.badlogic.gdx.math.Rectangle;
import com.brooks.gdx.game.objects.Knight;
import com.brooks.gdx.game.objects.Knight.JUMP_STATE;
import com.brooks.gdx.game.objects.Potion;
import com.brooks.gdx.game.objects.Orange;
import com.brooks.gdx.game.objects.Enemy;
import com.brooks.gdx.game.objects.Rock;

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
	
	//Knight <-> Rock collisions
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
	
	//Knight <-> Orange collisions
	private void onCollisionKnightWithOrange(Orange orange)
	{
		orange.collected = true;
		score += orange.getScore();
		if (score < 0)
			score = 0;
		Gdx.app.log(TAG, "Orange collected");
	}
	
	//Knight <-> Potion collisions
	private void onCollisionKnightWithPotion(Potion potion)
	{
		potion.collected = true;
		score += potion.getScore();
		if (score < 0)
			score = 0;
		level.knight.setPotionPowerup(true);
		Gdx.app.log(TAG, "Potion collected");
	}
	
	//Knight <-> Enemy collisions
	private void onCollisionKnightWithEnemy(Enemy enemy)
	{
		enemy.collected = true;
		score -= enemy.getScore();
		if (score < 0)
			score = 0;
		Gdx.app.log(TAG, "Enemy hit!");
	}
	
	//Initialize the first level
	private void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.knight);
	}

	//Initializes the WorldController
	public WorldController ()
	{
		init();
	}

	//Init function
	private void init ()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	//Pixmap creator
	private Pixmap createProceduralPixmap (int width, int height)
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		//Fill square with a red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		//Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		//Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	//Update function
	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);
		if (isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0)
				init();
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
	}

	//Handler for the camera movement in order to debug
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

	//Move the camera
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	//Reset the game world
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
		return false;
	}
	
	//TestCollisions function
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
	
	//HandleInputGame function
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
	
	//IsGameOver function
	public boolean isGameOver()
	{
		return lives < 0;
	}
	
	//isPlayerInGoo function
	public boolean isPlayerInGoo()
	{
		return level.knight.position.y < -5;
	}
}