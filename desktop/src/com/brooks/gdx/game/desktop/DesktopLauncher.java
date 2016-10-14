package com.brooks.gdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brooks.gdx.game.BrooksGdxGame;
import com.badlogic.gdx.tools.texturepacker.*;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

/**
 * Created by: Becky Brooks
 */
public class DesktopLauncher
{
	//Declare variables
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args)
	{
		if (rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "../core/assets-raw/images", "../core/assets/images", "brooksgdxgame.pack");
			TexturePacker.process(settings, "../core/assets-raw/images-ui", "../core/assets/images", "brooksgdxgame-ui.pack");
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Dimension Warp";
		cfg.width = 800;
		cfg.height = 480;
		new LwjglApplication(new BrooksGdxGame(), cfg);
	}
}
