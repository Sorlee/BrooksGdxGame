package com.brooks.gdx.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by: Becky Brooks
 */
public class CameraHelper
{
  private static final String TAG = CameraHelper.class.getName();
  
  private final float MAX_ZOOM_IN = 0.25f;
  private final float MAX_ZOOM_OUT = 10.0f;
  
  private Vector2 position;
  private float zoom;
  private Sprite target;
  
  //Positions the camera
  public CameraHelper ()
  {
    position = new Vector2();
    zoom = 1.0f;
  }
  
  //Updates based on how much gametime has passed
  public void update (float deltaTime)
  {
    if (!hasTarget())
      return;
    
    position.x = target.getX() + target.getOriginX();
    position.y = target.getY() + target.getOriginY();
  }
  
  //Sets the position of an object to the given coordinates
  public void setPosition (float x, float y)
  {
    this.position.set(x, y);
  }
  
  //Returns the position of an object
  public Vector2 getPosition()
  {
    return position;
  }
  
  //Zooms in
  public void addZoom (float amount)
  {
    setZoom(zoom + amount);
  }
  
  //Sets the zoom amount
  public void setZoom (float zoom)
  {
    this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
  }
  
  //Returns the zoom amount
  public float getZoom ()
  {
    return zoom;
  }
  
  //Changes the target to the given sprite
  public void setTarget (Sprite target)
  {
    this.target = target;
  }
  
  //Returns the sprite that is currently targetted
  public Sprite getTarget ()
  {
    return target;
  }
  
  //Checks to see if there is a target
  public boolean hasTarget()
  {
    return target != null;
  }
  
  //Checks to see if the specific sprite is being targetted
  public boolean hasTarget (Sprite target)
  {
    return hasTarget() && this.target.equals(target);
  }
  
  //Changes the camera's position
  public void applyTo (OrthographicCamera camera)
  {
    camera.position.x = position.x;
    camera.position.y = position.y;
    camera.zoom = zoom;
    camera.update();
  }
}