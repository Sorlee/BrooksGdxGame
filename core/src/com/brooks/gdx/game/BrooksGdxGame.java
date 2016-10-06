package com.brooks.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.brooks.gdx.game.Assets;
import com.badlogic.gdx.Game;
import com.brooks.gdx.game.screens.MenuScreen;

/**
 * Created by: Becky Brooks
 */
public class BrooksGdxGame extends Game
{
	//Create function
	@Override
	public void create ()
	{
		//Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Load assets
		Assets.instance.init(new AssetManager());
		//Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}