package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class Smoke extends AbstractGameObject
{
	/**
	 * Declare variables
	 */
	private TextureRegion regSmoke;
	
	/**
	 * Goal function
	 */
	public Smoke()
	{
		init();
	}
	
	/**
	 * Init function
	 */
	private void init()
	{
		dimension.set(2.0f, 2.0f);
		regSmoke = Assets.instance.levelDecoration.smoke;
		//Set bounding box for collision detection
		bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
		origin.set(dimension.x / 2.0f, 0.0f);
	}
	
	/**
	 * Render function
	 */
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regSmoke;
		batch.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}