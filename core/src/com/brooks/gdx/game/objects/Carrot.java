package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class Carrot extends AbstractGameObject
{
	/**
	 * Declare variables
	 */
	private TextureRegion regCarrot;
	
	/**
	 * Carrot function
	 */
	public Carrot()
	{
		init();
	}
	
	/**
	 * Init function
	 */
	private void init()
	{
		dimension.set(0.25f, 0.5f);
		regCarrot = Assets.instance.levelDecoration.carrot;
		//Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		origin.set(dimension.x / 2, dimension.y / 2);
	}
	
	/**
	 * Render function
	 */
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regCarrot;
		batch.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}
