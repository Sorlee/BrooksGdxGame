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
	
	//Initialize the first level
	private void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
	}

	//Initializes the WorldController
	public WorldController ()
	{
		init();
	}
	
	//Initialize function
	private void init ()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
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
		//Draw a cyan-colore border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
	
	//Update function
	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);
		cameraHelper.update(deltaTime);
	}
	
	//Handler for the camera movement in order to debug
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;

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
	
	Reset the game world
	@Override
	public boolean keyUp (int keycode)
	{
		//Reset game world
		if (keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		return false;
	}
}