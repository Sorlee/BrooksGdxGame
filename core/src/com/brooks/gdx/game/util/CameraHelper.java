package com.brooks.gdx.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.brooks.gdx.game.objects.AbstractGameObject;

/**
 * Created by: Becky Brooks
 */
public class CameraHelper
{
	//Declare variables
	private static final String TAG = CameraHelper.class.getName();
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	private Vector2 position;
	private float zoom;
	private AbstractGameObject target;
	private final float FOLLOW_SPEED = 4.0f;
	
	/**
	 * Positions the camera
	 */
	public CameraHelper ()
	{
		position = new Vector2();
		zoom = 1.0f;
	}
	
	/**
	 * Updates based on how much game time had passed
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		if (!hasTarget())
			return;
		
		position.lerp(target.position, FOLLOW_SPEED * deltaTime);
		
		//Prevent camera from moving down too far
		position.y = Math.max(-1f, position.y);
	}
	
	/**
	 * Sets the position of an object to the given coordinates
	 * @param x
	 * @param y
	 */
	public void setPosition (float x, float y)
	{
		this.position.set(x, y);
	}
	
	/**
	 * Returns the position of an object
	 * @return
	 */
	public Vector2 getPosition()
	{
		return position;
	}
	
	/**
	 * Zooms in
	 * @param amount
	 */
	public void addZoom (float amount)
	{
		setZoom(zoom + amount);
	}
	
	/**
	 * Sets the zoom amount
	 * @param zoom
	 */
	public void setZoom (float zoom)
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	
	/**
	 * Returns the zoom amount
	 * @return
	 */
	public float getZoom ()
	{
		return zoom;
	}
	
	/**
	 * Changes the target to the given sprite
	 * @param target
	 */
	public void setTarget (AbstractGameObject target)
	{
		this.target = target;
	}
	
	/**
	 * Returns the sprite that is currently targeted
	 * @return
	 */
	public AbstractGameObject getTarget ()
	{
		return target;
	}
	
	/**
	 * Checks to see if there is a target
	 * @return
	 */
	public boolean hasTarget()
	{
		return target != null;
	}
	
	/**
	 * Checks to see if the specific sprite is being targeted
	 * @param target
	 * @return
	 */
	public boolean hasTarget (AbstractGameObject target)
	{
		return hasTarget() && this.target.equals(target);
	}
	
	/**
	 * Changes the camera's position
	 * @param camera
	 */
	public void applyTo (OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
}