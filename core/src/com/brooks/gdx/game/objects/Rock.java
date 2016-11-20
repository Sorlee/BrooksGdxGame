package com.brooks.gdx.game.objects;
 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;
import com.badlogic.gdx.math.MathUtils;
 
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
 	private final float FLOAT_CYCLE_TIME = 2.0f;
 	private final float FLOAT_AMPLITUDE = 0.25f;
 	private float floatCycleTimeLeft;
 	private boolean floatingDownwards;
 	
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
 		dimension.set(0.5f, 0.5f);
 		regLeftEdge = Assets.instance.rock.leftEdge;
 		regRightEdge = Assets.instance.rock.rightEdge;
 		regMiddle = Assets.instance.rock.middle;
 		//Start length of this rock
 		setLength(1);
 		floatingDownwards = false;
 		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
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
 		floatCycleTimeLeft -= deltaTime;
 		if (floatCycleTimeLeft <= 0)
 		{
 			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
 			floatingDownwards = !floatingDownwards;
 			body.setLinearVelocity(0, FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1));
 		}
 		else
 		{
 			body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
 		}
 	}
}