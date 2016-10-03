package com.brooks.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by: Becky Brooks
 */
public abstract class AbstractGameObject
{
	//Declare variables
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	//AbstractGameObject
	public AbstractGameObject ()
	{
		position = new Vector2();
		dimension = new Vector2(1,1);
		origin = new Vector2();
		scale = new Vector2(1,1);
		rotation = 0;
	}
	
	//Update function
	public void update (float deltaTime)
	{
	}
	
	//Render
	public abstract void render (SpriteBatch batch);
}