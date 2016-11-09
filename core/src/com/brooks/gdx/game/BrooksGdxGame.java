package com.brooks.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.brooks.gdx.game.Assets;
import com.badlogic.gdx.Game;
import com.brooks.gdx.game.screens.MenuScreen;
import com.brooks.gdx.game.util.AudioManager;
import com.brooks.gdx.game.util.GamePreferences;

/**
 * Created by: Becky Brooks
 */
public class BrooksGdxGame extends Game
{
	/**
	 * Create method
	 */
	@Override
	public void create ()
	{
		//Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Load assets
		Assets.instance.init(new AssetManager());
		//Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		//Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}