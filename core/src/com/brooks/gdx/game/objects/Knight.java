package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brooks.gdx.game.Assets;
import com.brooks.gdx.game.util.Constants;
import com.brooks.gdx.game.util.CharacterSkin;
import com.brooks.gdx.game.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.brooks.gdx.game.util.AudioManager;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by: Becky Brooks
 */
public class Knight extends AbstractGameObject
{
	//Declare variables
	public static final String TAG = Knight.class.getName();
	private final float JUMP_TIME_MAX = 0.8f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
	public ParticleEffect dustParticles = new ParticleEffect();
	public ParticleEffect enemyDust = new ParticleEffect();
	private Animation idle;
	private Animation walk;
	private Animation jump;
	private Animation jumpEnd;
	
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
 	
 	public VIEW_DIRECTION viewDirection;
 	public float timeJumping;
 	public JUMP_STATE jumpState;
 	public boolean hasPotionPowerup;
 	public boolean damage;
 	public float timeLeftPotionPowerup;
 	public float timeLeftDamage;
 	public boolean check = false;
 	public int enemyNum;
 	
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
 		dimension.set(0.75f, 1.125f);
 		scale.set(1, 1);
 		idle = Assets.instance.knight.idle;
 		walk = Assets.instance.knight.walk;
 		jump = Assets.instance.knight.jump;
 		jumpEnd = Assets.instance.knight.jumpEnd;
 		setAnimation(idle);
 		stateTime = 0.0f;
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
 		damage = false;
 		timeLeftPotionPowerup = 0;
 		timeLeftDamage = 0;
 		//Particles
 		dustParticles.load(Gdx.files.internal("particles/dust"), Gdx.files.internal("particles"));
 		enemyDust.load(Gdx.files.internal("particles/enemyDust"), Gdx.files.internal("particles"));
		enemyDust.scaleEffect(0.01f);
		enemyNum = 0;
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
 				check = true;
 				if (jumpKeyPressed)
 				{
 					AudioManager.instance.play(Assets.instance.sounds.jump);
 					//Start counting jump time from the beginning
 					timeJumping = 0;
 					jumpState = JUMP_STATE.JUMP_RISING;
 				}
 				break;
 			case JUMP_RISING: //Rising in the air
 				check = false;
 				if (!jumpKeyPressed)
 					jumpState = JUMP_STATE.JUMP_FALLING;
 				break;
 			case FALLING: //Falling down
 				check = false;
 			case JUMP_FALLING: //Falling down after jump
 				check = false;
 				if (jumpKeyPressed && hasPotionPowerup)
 				{
 					AudioManager.instance.play(Assets.instance.sounds.jumpWithFeather, 1, MathUtils.random(1.0f, 1.1f));
 					timeJumping = JUMP_TIME_OFFSET_FLYING;
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
 		if (timeLeftDamage > 0)
 		{
 			timeLeftDamage -= deltaTime;
 			if (timeLeftDamage < 0)
 			{
 				//Disable power-up
 				timeLeftDamage = 0;
 				setDamage(false);
 			}
 		}
 		dustParticles.update(deltaTime);
 		enemyDust.update(deltaTime);
 		
 		
 		if (animation == idle)
 		{
 			//Set to jumping animation
 			if (jumpState == JUMP_STATE.JUMP_RISING)
 				setAnimation(jump);
 			//Set to jumping end animation
 			if (jumpState == JUMP_STATE.JUMP_FALLING)
 				setAnimation(jumpEnd);
 			//Set to walking animation
 			else if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)) && !Gdx.input.isKeyPressed(Keys.SPACE))
 				setAnimation(walk);
 		}
 		else if (animation == walk)
 		{
 			//Set to jumping animation
 			if (jumpState == JUMP_STATE.JUMP_RISING)
 				setAnimation(jump);
 			//Set to jumping end animation
 			if (jumpState == JUMP_STATE.JUMP_FALLING)
 				setAnimation(jumpEnd);
 			//Set to idle
 			else if (!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.SPACE))
 				setAnimation(idle);
 		}
 		else if (animation == jump)
 		{
 			//Set to jumping end animation
 			if (animation.isAnimationFinished(stateTime))
 				setAnimation(jumpEnd);
 			//Set to walking animation
 			else if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)) && !Gdx.input.isKeyPressed(Keys.SPACE))
 				setAnimation(walk);
 			//Set to idle
 			else if (!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.SPACE))
 				setAnimation(idle);
 		}
 		else if (animation == jumpEnd)
 		{
 			//Set to walking animation
 			if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)) && !Gdx.input.isKeyPressed(Keys.SPACE) && check)
 				setAnimation(walk);
 			//Set to idle
 			else if (check)
 			{
 				setAnimation(idle);
 			}
 		}
 	}
 	
 	/**
 	 * UpdateMotionY method
 	 */
 	@Override
 	protected void updateMotionY (float deltaTime)
 	{
 		if (!hasPotionPowerup)
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
 		}
 		else
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
 					if (timeJumping <= (JUMP_TIME_MAX + 0.2f))
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
 					if (timeJumping > 0 && timeJumping <= (JUMP_TIME_MIN + 0.2f))
 					{
 						//Still jumping
 						velocity.y = terminalVelocity.y;
 					}
 			}
 		}
 		if (jumpState != JUMP_STATE.GROUNDED)
 		{
 			dustParticles.allowCompletion();
 			super.updateMotionY(deltaTime);
 		}
 		if (checkOne)
		{
 			enemyDust.setPosition(position.x, position.y);
			enemyDust.start();
			enemyDust.allowCompletion();
			enemyDust.reset();
			checkOne = false;
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
 		enemyDust.draw(batch);
 		
 		//Apply Skin Color
 		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());
 		
 		//Set special color when game object has a potion powerup
 		if (hasPotionPowerup)
 		{
 			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
 		}
 		if (damage)
 		{
 			batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
 			if (enemyNum == 1)
 			{
 				acceleration.x = -1000f;
 			} else
 			{
 				acceleration.x = -1000f;
 				acceleration.y = 1000f;
 			}
 		} else
 			acceleration.set(0.0f, -25.0f);
 		
 		//Draw image
 		reg = animation.getKeyFrame(stateTime, true);
 		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);
 		//Reset color to white
 		batch.setColor(1, 1, 1, 1);
 	}
 	
 	public void setCheck(boolean times)
 	{
 		checkOne = times;
 	}

	public void setDamage(boolean dam)
	{
		damage = dam;
		if (dam)
 		{
 			timeLeftDamage = Constants.ENEMY_DAMAGE;
 		}
	}
	
	public boolean hasDamage()
 	{
 		return damage && timeLeftDamage > 0;
 	}

	public void setEnemy(int num) {
		enemyNum = num;
	}
 }