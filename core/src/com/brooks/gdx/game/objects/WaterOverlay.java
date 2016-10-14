package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class WaterOverlay extends AbstractGameObject
{
	//Declare variables
	private TextureRegion regWaterOverlay;
	private float length;
	
	/**
	 * WaterOverlay
	 * @param length
	 */
	public WaterOverlay (float length)
	{
		this.length = length;
		init();
	}
	
	/**
	 * Init method
	 */
	private void init()
	{
		dimension.set(length * 10, 3);
		regWaterOverlay = Assets.instance.levelDecoration.waterOverlay;
		origin.x = -dimension.x / 2;
	}
	
	/**
	 * Render method
	 */
	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regWaterOverlay;
		batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}