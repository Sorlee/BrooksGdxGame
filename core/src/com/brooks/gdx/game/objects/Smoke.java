package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class Smoke extends AbstractGameObject
{
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
		setAnimation(Assets.instance.smoke.smokeSwirl);
		stateTime = MathUtils.random(0.0f, 1.0f);
		//Set bounding box for collision detection
		bounds.set(dimension.x/2, dimension.y/2, dimension.x/2, dimension.y/2);
		origin.set(dimension.x / 2.0f, 0.0f);
	}
	
	/**
	 * Render function
	 */
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = animation.getKeyFrame(stateTime, true);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}