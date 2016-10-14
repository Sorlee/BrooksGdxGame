package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brooks.gdx.game.Assets;
import com.brooks.gdx.game.util.Constants;
import com.brooks.gdx.game.util.CharacterSkin;
import com.brooks.gdx.game.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Created by: Becky Brooks
 */
public class Knight extends AbstractGameObject
{
	//Declare variables
	public static final String TAG = Knight.class.getName();
	private final float JUMP_TIME_MAX = 1.0f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
	public ParticleEffect dustParticles = new ParticleEffect();
	
	public enum VIEW_DIRECTION
	{
		LEFT,
		RIGHT
	}
	
	public enum JUMP_STATE
	{
		GROUNDED,
		FALLING,
		JUMP_RISING,
 		JUMP_FALLING
 	}
 	
 	private TextureRegion regHead;
 	public VIEW_DIRECTION viewDirection;
 	public float timeJumping;
 	public JUMP_STATE jumpState;
 	public boolean hasPotionPowerup;
 	public float timeLeftPotionPowerup;
 	
 	/**
 	 * Knight method
 	 */
 	public Knight()
 	{
 		init();
 	}
 	
 	/**
 	 * Initialize method
 	 */
 	public void init()
 	{
 		dimension.set(0.75f, 1.25f);
 		regHead = Assets.instance.knight.knight;
 		//Center image on game object
 		origin.set(dimension.x / 2, dimension.y / 2);
 		//Bounding box for collision detection
 		bounds.set(0, 0, dimension.x, dimension.y);
 		//Set physics values
 		terminalVelocity.set(3.0f, 4.0f);
 		friction.set(12.0f, 0.0f);
 		acceleration.set(0.0f, -25.0f);
 		//View direction
 		viewDirection = VIEW_DIRECTION.RIGHT;
 		//Jump state
 		jumpState = JUMP_STATE.FALLING;
 		timeJumping = 0;
 		//Powerups
 		hasPotionPowerup = false;
 		timeLeftPotionPowerup = 0;
 		//Particles
 		dustParticles.load(Gdx.files.internal("particles/dust"), Gdx.files.internal("particles"));
 	}
 	
 	/**
 	 * SetJumping method
 	 * @param jumpKeyPressed
 	 */
 	public void setJumping(boolean jumpKeyPressed)
 	{
 		switch(jumpState)
 		{
 			case GROUNDED: //Character is standing on a platform
 				if (jumpKeyPressed)
 				{
 					//Start counting jump time from the beginning
 					timeJumping = 0;
 					jumpState = JUMP_STATE.JUMP_RISING;
 				}
 				break;
 			case JUMP_RISING: //Rising in the air
 				if (!jumpKeyPressed)
 				{
 					jumpState = JUMP_STATE.JUMP_FALLING;
 				}
 				break;
 			case FALLING: //Falling down
 			case JUMP_FALLING: //Falling down after jump
 				if (jumpKeyPressed && hasPotionPowerup)
 				{
 					timeJumping = JUMP_TIME_OFFSET_FLYING;
 					jumpState = JUMP_STATE.JUMP_RISING;
 				}
 				break;
 		}
 	}
 	
 	/**
 	 * SetPotionPowerup method
 	 * @param pickedUp
 	 */
 	public void setPotionPowerup(boolean pickedUp)
 	{
 		hasPotionPowerup = pickedUp;
 		if (pickedUp)
 		{
 			timeLeftPotionPowerup = Constants.ITEM_POTION_POWERUP_DURATION;
 			terminalVelocity.set(4.0f, 4.0f);
 		}
 		else
 			terminalVelocity.set(3.0f, 4.0f);
 	}
 	
 	/**
 	 * HasPotionPowerup method
 	 * @return
 	 */
 	public boolean hasPotionPowerup()
 	{
 		return hasPotionPowerup && timeLeftPotionPowerup > 0;
 	}
 	
 	/**
 	 * Update method
 	 */
 	@Override
 	public void update (float deltaTime)
 	{
 		super.update(deltaTime);
 		if (velocity.x != 0)
 		{
 			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
 		}
 		if (timeLeftPotionPowerup > 0)
 		{
 			timeLeftPotionPowerup -= deltaTime;
 			if (timeLeftPotionPowerup < 0)
 			{
 				//Disable power-up
 				timeLeftPotionPowerup = 0;
 				setPotionPowerup(false);
 			}
 		}
 		dustParticles.update(deltaTime);
 	}
 	
 	/**
 	 * UpdateMotionY method
 	 */
 	@Override
 	protected void updateMotionY (float deltaTime)
 	{
 		switch (jumpState)
 		{
 			case GROUNDED:
 				jumpState = JUMP_STATE.FALLING;
 				if (velocity.x != 0)
 				{
 					dustParticles.setPosition(position.x + dimension.x / 2, position.y);
 					dustParticles.start();
 				}
 				break;
 			case JUMP_RISING:
 				//Keep track of jump time
 				timeJumping += deltaTime;
 				//Jump time left?
 				if (timeJumping <= JUMP_TIME_MAX)
 				{
 					//Still jumping
 					velocity.y = terminalVelocity.y;
 				}
 				break;
 			case FALLING:
 				break;
 			case JUMP_FALLING:
 				//Add delta time to track jump time
 				timeJumping += deltaTime;
 				//Jump to minimal height if jump key was pressed too short
 				if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
 				{
 					//Still jumping
 					velocity.y = terminalVelocity.y;
 				}
 		}
 		if (jumpState != JUMP_STATE.GROUNDED)
 		{
 			dustParticles.allowCompletion();
 			super.updateMotionY(deltaTime);
 		}
 	}
 	
 	/**
 	 * Render method
 	 */
 	@Override
 	public void render (SpriteBatch batch)
 	{
 		TextureRegion reg = null;
 		
 		//Draw Particles
 		dustParticles.draw(batch);
 		
 		//Apply Skin Color
 		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());
 		
 		//Set special color when game object has a potion powerup
 		if (hasPotionPowerup)
 		{
 			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
 		}
 		//Draw image
 		reg = regHead;
 		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);
 		//Reset color to white
 		batch.setColor(1, 1, 1, 1);
 	}
 }