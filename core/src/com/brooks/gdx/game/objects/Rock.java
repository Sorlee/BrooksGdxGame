package com.brooks.gdx.game.objects;
 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;
 
/**
 * Created by: Becky Brooks
 */
public class Rock extends AbstractGameObject
{
 	//Declare variables
 	private TextureRegion regLeftEdge;
 	private TextureRegion regRightEdge;
 	private TextureRegion regMiddle;
 	private int length;
 	
 	/**
 	 * Rock method
 	 */
 	public Rock()
 	{
 		init();
 	}
 	
 	/**
 	 * Initialize method
 	 */
 	private void init()
 	{
 		dimension.set(0.75f, 0.75f);
 		regLeftEdge = Assets.instance.rock.leftEdge;
 		regRightEdge = Assets.instance.rock.rightEdge;
 		regMiddle = Assets.instance.rock.middle;
 		//Start length of this rock
 		setLength(1);
 	}
 	
 	/**
 	 * SetLength method
 	 * @param length
 	 */
 	public void setLength(int length)
 	{
 		this.length = length;
 		//Update bounding box for collision detection
 		bounds.set(0, 0, dimension.x * length, dimension.y);
 	}
 	
 	/**
 	 * IncreaseLength method
 	 * @param amount
 	 */
 	public void increaseLength(int amount)
 	{
 		setLength(length + amount);
 	}
 	
 	/**
 	 * Render method
 	 */
 	@Override
 	public void render (SpriteBatch batch)
 	{
 		TextureRegion reg = null;
 		float relX = -dimension.x;
 
 		//Draw left edge
 		reg = regLeftEdge;
 		batch.draw(reg.getTexture(), position.x + relX, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 		relX += dimension.x;
 
 		//Draw middle
 		reg = regMiddle;
 		for (int i = 0; i < length; i++)
 		{
 			batch.draw(reg.getTexture(), position.x + relX, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 			relX += dimension.x;
 		}
 
 		//Draw right edge
 		reg = regRightEdge;
 		batch.draw(reg.getTexture(), position.x + relX, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 	}
 	
 	/**
 	 * Update method
 	 */
 	@Override
 	public void update(float deltaTime)
 	{
 		super.update(deltaTime);
 	}
}