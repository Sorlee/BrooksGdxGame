package com.brooks.gdx.game.objects;
 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brooks.gdx.game.Assets;
import com.badlogic.gdx.math.Vector2;
 
/**
 * Created by: Becky Brooks
 */
public class City extends AbstractGameObject
{
 	//Declare variables
 	private TextureRegion regCity;
 	private int length;
 	
 	/**
 	 * City method
 	 * @param length
 	 */
 	public City (int length)
 	{
 		this.length = length;
 		init();
 	}
 	
 	/**
 	 * Initialize method
 	 */
 	public void init()
 	{
 		dimension.set(8, 2);
 		regCity = Assets.instance.levelDecoration.city;
 
 		//Shift city and extend length
 		origin.x = -dimension.x * 2;
 		length += dimension.x * 2;
 	}
 	
 	/**
 	 * DrawCity method
 	 * @param batch
 	 * @param offsetX
 	 * @param offsetY
 	 * @param tintColor
 	 * @param parallaxSpeedX
 	 */
 	private void drawCity (SpriteBatch batch, float offsetX, float offsetY, float tintColor, float parallaxSpeedX)
 	{
 		TextureRegion reg = null;
 		batch.setColor(tintColor, tintColor, tintColor, 1);
 		float xRel = dimension.x * offsetX;
 		float yRel = dimension.y * offsetY;
 
 		//City spans the whole level
 		int cityLength = 0;
 		cityLength += MathUtils.ceil(length / dimension.x * (1 - parallaxSpeedX));
 		cityLength += MathUtils.ceil(0.5f + offsetX);
 		for (int i = 0; i < cityLength; i++)
 		{
 			//Get the city
 			reg = regCity;
 			batch.draw(reg.getTexture(), origin.x + xRel + position.x * parallaxSpeedX, position.y + origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 			xRel += dimension.x;
 		}
 		//Reset color to white
 		batch.setColor(1, 1, 1, 1);
 	}
 	
 	/**
 	 * Render method
 	 */
 	@Override
 	public void render (SpriteBatch batch)
 	{
 		//Distant mountains (dark gray)
 		drawCity(batch, 0.50f, 0.50f, 0.50f, 0.8f);
 		//Distant mountains (gray)
 		drawCity(batch, 0.35f, 0.35f, 0.35f, 0.5f);
 		//Distant mountains (light gray)
 		drawCity(batch, 0.25f, 0.25f, 0.25f, 0.3f);
 	}
 	
 	/**
 	 * UpdateScrollPosition method
 	 * @param camPosition
 	 */
 	public void updateScrollPosition (Vector2 camPosition)
 	{
 		position.set(camPosition.x, position.y);
 	}
}