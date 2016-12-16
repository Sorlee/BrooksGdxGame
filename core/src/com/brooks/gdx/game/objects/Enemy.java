package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class Enemy extends AbstractGameObject
{
 	//Declare variables
 	public boolean collected;
 	
 	/**
 	 * Enemy method
 	 */
 	public Enemy()
 	{
 		init();
 	}
 	
 	/**
 	 * Initialize method
 	 */
 	private void init ()
 	{
 		dimension.set(1.0f, 1.0f);
 		int random;
 		random = (int)(Math.random()*2);
 		if (random == 0)
 			setAnimation(Assets.instance.enemy1.idle);
 		if (random == 1)
 			setAnimation(Assets.instance.enemy2.idle);
 		stateTime = MathUtils.random(0.0f, 1.0f);
 		//Set bounding box for collision detection
 		bounds.set(0, 0, dimension.x, dimension.y);
 		collected = false;
 	}
 	
 	/**
 	 * Render method
 	 */
 	public void render (SpriteBatch batch)
 	{
 		if (collected)
 			return;
 		TextureRegion reg = null;
 		reg = animation.getKeyFrame(stateTime, true);
 		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionHeight(), reg.getRegionWidth(), false, false);
 	}
 	
 	/**
 	 * GetScore method
 	 * @return
 	 */
 	public int getScore()
 	{
 		return 50;
 	}
}