package com.brooks.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.brooks.gdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

/**
 * Created by: Becky Brooks
 */
public class Assets implements Disposable, AssetErrorListener
{
	//Declare variables
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	public AssetKnight knight;
	public AssetRock rock;
	public AssetOrange orange;
	public AssetPotion potion;
	public AssetEnemy enemy;
	public AssetSmoke smoke;
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	public AssetSounds sounds;
	public AssetMusic music;
	
	/**
	 * Assets method
	 * singleton: prevent instantiation from other classes 
	 */
	private Assets()
	{
	}
	
	/**
	 * Class for AssetFonts
	 */
	public class AssetFonts
	{
		//Declare variables
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts()
		{
			//Create three fonts using Arial 15px bitmap font
			defaultSmall = new BitmapFont (Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont (Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont (Gdx.files.internal("images/arial-15.fnt"), true);
			//Set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			//Enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	
	/**
	 * Init method
	 * @param assetManager
	 */
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//set asset manager error handler
		assetManager.setErrorListener(this);
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		//load sounds
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost.wav", Sound.class);
		//load music
		assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);
		//start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		//enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		//create game resource objects
		fonts = new AssetFonts();
		knight = new AssetKnight(atlas);
		rock = new AssetRock(atlas);
		orange = new AssetOrange(atlas);
		potion = new AssetPotion(atlas);
		enemy = new AssetEnemy(atlas);
		smoke = new AssetSmoke(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}
	
	/**
	 * Dispose method
	 */
	@Override
	public void dispose()
	{
		//Dispose of the asset manager and the fonts
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}
	
	/**
	 * Error handler when an asset can't be loaded
	 * @param filename
	 * @param type
	 * @param throwable
	 */
	public void error(String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}
	
	/**
	 * Error handler when an asset can't be loaded
	 */
	@Override
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}
	
	/**
	 * Class for the knight
	 */
	public class AssetKnight
	{
		public final AtlasRegion knight;
		public final Animation idle;
		public final Animation walk;
		public final Animation jump;
		public final Animation jumpEnd;
		
		public AssetKnight (TextureAtlas atlas)
		{
			knight = atlas.findRegion("Knight");
			Array<AtlasRegion> regions = null;
			//Animation: Knight idle
			regions = atlas.findRegions("Knight");
			idle = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
			//Animation: Knight walk
			regions = atlas.findRegions("KnightWalk");
			walk = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
			//Animation: Knight jump
			regions = atlas.findRegions("KnightJump");
			jump = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.NORMAL);
			//Animation: Knight jump end
			regions = new Array<AtlasRegion>();
			regions.add(atlas.findRegion("KnightJump", 7));
			regions.add(atlas.findRegion("KnightJump", 6));
			jumpEnd = new Animation(1.0f / 5.0f, regions);
		}
	}
	
	/**
	 * Class for the rock (left, middle, and right)
	 */
	public class AssetRock
	{
		public final AtlasRegion leftEdge;
		public final AtlasRegion rightEdge;
		public final AtlasRegion middle;
		
		public AssetRock (TextureAtlas atlas)
		{
			leftEdge = atlas.findRegion("LeftEdge");
			middle = atlas.findRegion("Middle");
			rightEdge = atlas.findRegion("RightEdge");
		}
	}
	
	/**
	 * Class for the orange
	 */
	public class AssetOrange
	{
		public final AtlasRegion orange;
		
		public AssetOrange (TextureAtlas atlas)
		{
			orange = atlas.findRegion("Orange");
		}
	}
	
	/**
	 * Class for the potion
	 */
	public class AssetPotion
	{
		public final AtlasRegion potion;
		
		public AssetPotion (TextureAtlas atlas)
		{
			potion = atlas.findRegion("Potion");
		}
	}
	
	/**
	 * Class for the enemy
	 */
	public class AssetEnemy
	{
		public final AtlasRegion enemy;
		public final Animation idle;
		
		public AssetEnemy (TextureAtlas atlas)
		{
				enemy = atlas.findRegion("Enemy1");
				Array<AtlasRegion> regions = null;
				//Animation: Enemies idle
				regions = atlas.findRegions("Enemy1");
				idle = new Animation(1.0f / 7.0f, regions, Animation.PlayMode.LOOP);
		}
	}
	
	/**
	 * Class for the end portal
	 */
	public class AssetSmoke
	{
		public final AtlasRegion smoke;
		public final Animation smokeSwirl;
		
		public AssetSmoke (TextureAtlas atlas)
		{
			smoke = atlas.findRegion("smoke_1");
			Array<AtlasRegion> regions = null;
			regions = atlas.findRegions("smoke");
			smokeSwirl = new Animation(1.0f / 8.0f, regions, Animation.PlayMode.LOOP);
		}
	}
	
	/**
	 * Class for the clouds, the city, and the goo overlay
	 */
	public class AssetLevelDecoration
	{
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion city;
		public final AtlasRegion gooOverlay;
		
		public AssetLevelDecoration (TextureAtlas atlas)
		{
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			city = atlas.findRegion("City");
			gooOverlay = atlas.findRegion("GooOverlay");
		}
	}
	
	/**
	 * Class for the Sound manager
	 */
	public class AssetSounds
	{
		public final Sound jump;
		public final Sound jumpWithFeather;
		public final Sound pickupCoin;
		public final Sound pickupFeather;
		public final Sound liveLost;
		
		public AssetSounds (AssetManager am)
		{
			jump = am.get("sounds/jump.wav", Sound.class);
			jumpWithFeather = am.get("sounds/jump_with_feather.wav", Sound.class);
			pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupFeather = am.get("sounds/pickup_feather.wav", Sound.class);
			liveLost = am.get("sounds/live_lost.wav", Sound.class);
		}
	}
		
	/**
	 * Class for the Music manager
	 */
	public class AssetMusic
	{
		public final Music song01;
		
		public AssetMusic (AssetManager am)
		{
			song01 = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
		}
	}
}