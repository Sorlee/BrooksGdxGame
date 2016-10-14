package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class GooOverlay extends AbstractGameObject
{
 	//Declare variables
 	private TextureRegion regGooOverlay;
 	private float length;
 	
 	/**
 	 * GooOverlay method
 	 * @param length
 	 */
 	public GooOverlay (float length)
 	{
 		this.length = length;
 		init();
 	}
 	
 	/**
 	 * Initialize method
 	 */
 	private void init()
 	{
 		dimension.set(length * 10, 3);
 		regGooOverlay = Assets.instance.levelDecoration.gooOverlay;
 		origin.x = -dimension.x / 2;
 	}
 	
 	/**
 	 * Render method
 	 */
 	@Override
 	public void render (SpriteBatch batch)
 	{
 		TextureRegion reg = null;
 		reg = regGooOverlay;
 		batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 	}
}