package com.brooks.gdx.game.objects;
 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
 
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
 	private Vector2 floatTargetPosition;
 	
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
 		dimension.set(0.75f, 1f);
 		regLeftEdge = Assets.instance.rock.leftEdge;
 		regRightEdge = Assets.instance.rock.rightEdge;
 		regMiddle = Assets.instance.rock.middle;
 		//Start length of this rock
 		setLength(1);
 		floatingDownwards = false;
 		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
 		floatTargetPosition = null;
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
 		float relX = 0;
 		float relY = 0;
 
 		//Draw left edge
 		reg = regLeftEdge;
 		relX -= dimension.x / 4;
 		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 
 		//Draw middle
 		relX = 0;
 		reg = regMiddle;
 		for (int i = 0; i < length; i++)
 		{
 			batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 			relX += dimension.x;
 		}
 
 		//Draw right edge
 		reg = regRightEdge;
 		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 	}
 	
 	/**
 	 * Update method
 	 */
 	@Override
 	public void update(float deltaTime)
 	{
 		super.update(deltaTime);
 		floatCycleTimeLeft -= deltaTime;
 		if (floatTargetPosition == null)
 			floatTargetPosition = new Vector2(position);
 		if (floatCycleTimeLeft <= 0)
 		{
 			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
 			floatingDownwards = !floatingDownwards;
 			floatTargetPosition.y += FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1);
 		}
 		position.lerp(floatTargetPosition, deltaTime);
 	}
}