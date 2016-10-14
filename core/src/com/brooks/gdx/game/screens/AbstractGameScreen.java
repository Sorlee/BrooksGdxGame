package com.brooks.gdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public abstract class AbstractGameScreen implements Screen
{
	//Declare variables
	protected Game game;
	
	/**
	 * AbstractGameScreen method
	 * @param game
	 */
	public AbstractGameScreen (Game game)
	{
		this.game = game;
	}
	
	/**
	 * Render method
	 */
	public abstract void render(float deltaTime);
	
	/**
	 * Resize method
	 */
	public abstract void resize(int width, int height);
	
	/**
	 * Show method
	 */
	public abstract void show ();
	
	/**
	 * Hide method
	 */
	public abstract void hide ();
	
	/**
	 * Pause method
	 */
	public abstract void pause ();
	
	/**
	 * Resume method
	 */
	public void resume ()
	{
		Assets.instance.init(new AssetManager());
	}
	
	/**
	 * Dispose method
	 */
	public void dispose ()
	{
		Assets.instance.dispose();
	}
}