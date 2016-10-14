package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brooks.gdx.game.Assets;

/**
 * Created by: Becky Brooks
 */
public class Clouds extends AbstractGameObject
{
	//Declare variables
	private float length;
	private Array<TextureRegion> regClouds;
	private Array<Cloud> clouds;
	
	/**
	 * Cloud
	 */
	private class Cloud extends AbstractGameObject
	{
		private TextureRegion regCloud;

		public Cloud ()
		{
		}

		public void setRegion (TextureRegion region)
		{
			regCloud = region;
		}

		@Override
		public void render (SpriteBatch batch)
		{
			TextureRegion reg = regCloud;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		}
	}
	
	/**
	 * Clouds
	 * @param length
	 */
	public Clouds (float length)
	{
		this.length = length;
		init();
	}
	
	/**
	 * Init method
	 */
	private void init()
	{
		dimension.set(3.0f, 1.5f);
		regClouds = new Array<TextureRegion>();
		regClouds.add(Assets.instance.levelDecoration.cloud01);
		regClouds.add(Assets.instance.levelDecoration.cloud02);
		regClouds.add(Assets.instance.levelDecoration.cloud03);

		int distFac = 5;
		int numClouds = (int)(length / distFac);
		clouds = new Array<Cloud>(2 * numClouds);
		for (int i = 0; i < numClouds; i++)
		{
			Cloud cloud = spawnCloud();
			cloud.position.x = i * distFac;
			clouds.add(cloud);
		}
	}
	
	/**
	 * SpawnCloud method
	 * @return
	 */
	private Cloud spawnCloud()
	{
		Cloud cloud = new Cloud();
		cloud.dimension.set(dimension);
		//Select random cloud image
		cloud.setRegion(regClouds.random());
		//Position
		Vector2 pos = new Vector2();
		//Position after end of level
		pos.x = length + 10;
		//Base position
		pos.y += 1.75;
		//Random additional position
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1);
		cloud.position.set(pos);
		//Speed
		Vector2 speed = new Vector2();
		//Base speed
		speed.x += 0.5f;
		//Random additional speed
		speed.x += MathUtils.random(0.0f, 0.75f);
		cloud.terminalVelocity.set(speed);
		//Move left
		speed.x *= -1;
		cloud.velocity.set(speed);
		return cloud;
	}
	
	/**
	 * Render method
	 */
	@Override
	public void render (SpriteBatch batch)
	{
		for (Cloud cloud : clouds)
			cloud.render(batch);
	}
	
	/**
	 * Update method
	 */
	@Override
	public void update (float deltaTime)
	{
		for (int i = clouds.size - 1; i >= 0; i--)
		{
			Cloud cloud = clouds.get(i);
			cloud.update(deltaTime);
			if (cloud.position.x < -10)
			{
				//Cloud moved outside of world. Destroy and spawn new cloud at end of level
				clouds.removeIndex(i);
				clouds.add(spawnCloud());
			}
		}
	}
}