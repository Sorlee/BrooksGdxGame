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
	
	public AbstractGameScreen (Game game)
	{
		this.game = game;
	}
	
	//Render function
	public abstract void render(float deltaTime);
	
	//Resize function
	public abstract void resize(int width, int height);
	
	//Show function
	public abstract void show ();
	
	//Hide function
	public abstract void hide ();
	
	//Pause function
	public abstract void pause ();
	
	//Resume function
	public void resume ()
	{
		Assets.instance.init(new AssetManager());
	}
	
	//Dispose function
	public void dispose ()
	{
		Assets.instance.dispose();
	}
}
