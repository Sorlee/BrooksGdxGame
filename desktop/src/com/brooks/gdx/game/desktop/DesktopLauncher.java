package com.brooks.gdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brooks.gdx.game.BrooksGdxGame;
import com.badlogic.gdx.tools.texturepacker.*;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher
{
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;

	public static void main(String[] args)
	{
		if (rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "../core/assets-raw/images", "../core/assets/images", "brooksgdxgame.pack");
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Dimension Warp";
		cfg.width = 800;
		cfg.height = 480;
		new LwjglApplication(new BrooksGdxGame(), cfg);
	}
}
