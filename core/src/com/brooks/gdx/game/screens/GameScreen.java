package com.brooks.gdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.brooks.gdx.game.WorldController;
import com.brooks.gdx.game.WorldRenderer;
import com.brooks.gdx.game.util.GamePreferences;

/**
 * Created by: Becky Brooks
 */
public class GameScreen extends AbstractGameScreen
{
	//Declare variables
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	
	/**
	 * GameScreen method
	 * @param game
	 */
	public GameScreen (Game game)
	{
		super(game);
	}
	
	/**
	 * Render method
	 */
	@Override
	public void render (float deltaTime)
	{
		//Do not update game world when paused
		if (!paused)
		{
			//Update game world by the time that has passed since the last rendered frame
			worldController.update(deltaTime);
		}
		//Set the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		//Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Render game world to screen
		worldRenderer.render();
	}
	
	/**
	 * Resize method
	 */
	@Override
	public void resize (int width, int height)
	{
		worldRenderer.resize(width, height);
	}
	
	/**
	 * Show method
	 */
	@Override
	public void show ()
	{
		GamePreferences.instance.load();
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}
	
	/**
	 * Hide method
	 */
	@Override
	public void hide ()
	{
		
	}
	
	/**
	 * Pause method
	 */
	@Override
	public void pause ()
	{
		paused = true;
	}
	
	/**
	 * Resume method
	 */
	@Override
	public void resume ()
	{
		super.resume();
		//Only called on Android
		paused = false;
	}
	
	/**
	 * Dispose method
	 */
	@Override
	public void dispose()
	{
		worldController.dispose();
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}
}