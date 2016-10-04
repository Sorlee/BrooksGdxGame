package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;

public class Potion extends AbstractGameObject
{
	//Declare variables
	private TextureRegion regPotion;
 	public boolean collected;
 	
 	public Potion()
 	{
 		init();
 	}
 	
 	//Init function
 	private void init()
 	{
 		dimension.set(0.5f, 0.5f);
 		regPotion = Assets.instance.potion.potion;
 		//Set bounding box for collision detection
 		bounds.set(0, 0, dimension.x, dimension.y);
 		collected = false;
 	}
 	
 	//Render function
 	public void render (SpriteBatch batch)
 	{
 		if (collected)
 			return;
 		TextureRegion reg = null;
 		reg = regPotion;
 		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 	}
 	
 	//GetScore function
 	public int getScore()
 	{
 		return 250;
 	}
}