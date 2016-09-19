package com.brooks.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Application;
import com.brooks.gdx.game.WorldController;
import com.brooks.gdx.game.WorldRenderer;
import com.badlogic.gdx.assets.AssetManager;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class BrooksGdxGame extends ApplicationAdapter
{
	private static final String TAG = BrooksGdxGame.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;

	@Override
	public void create ()
	{
		//Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Load assets
		Assets.instance.init(new AssetManager());
		//Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
	}

	@Override
	public void render ()
	{
		//Update game world by the time that has passed since last rendered frame.
		worldController.update(Gdx.graphics.getDeltaTime());
		//Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		//Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Render game world to screen
		worldRenderer.render();
	}

	@Override
	public void resize (int width, int height)
	{
		worldRenderer.resize(width, height);
	}

	@Override
	public void dispose ()
	{
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
