package com.brooks.gdx.game.objects;
 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brooks.gdx.game.Assets;
 
/**
 * Created by: Becky Brooks
 */
public class City extends AbstractGameObject
{
 	//Declare variables
 	private TextureRegion regCity;
 	private int length;
 	
 	//Mountains
 	public City (int length)
 	{
 		this.length = length;
 		init();
 	}
 	
 	//Init function
 	public void init()
 	{
 		dimension.set(10, 2);
 		regCity = Assets.instance.levelDecoration.city;
 
 		//Shift city and extend length
 		origin.x = -dimension.x * 2;
 		length += dimension.x * 2;
 	}
 	
 	//DrawCity function
 	private void drawCity (SpriteBatch batch, float offsetX, float offsetY, float tintColor)
 	{
 		TextureRegion reg = null;
 		batch.setColor(tintColor, tintColor, tintColor, 1);
 		float xRel = dimension.x * offsetX;
 		float yRel = dimension.y * offsetY;
 
 		//City spans the whole level
 		int cityLength = 0;
 		cityLength += MathUtils.ceil(length / (dimension.x));
 		cityLength += MathUtils.ceil(0.5f + offsetX);
 		for (int i = 0; i < cityLength; i++)
 		{
 			//Get the city
 			reg = regCity;
 			batch.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 			xRel += dimension.x;
 		}
 		//Reset color to white
 		batch.setColor(1, 1, 1, 1);
 	}
 	
 	//Render function
 	@Override
 	public void render (SpriteBatch batch)
 	{
 		//Distant mountains (dark gray)
 		drawCity(batch, 0.50f, 0.50f, 0.50f);
 		//Distant mountains (gray)
 		drawCity(batch, 0.35f, 0.35f, 0.35f);
 		//Distant mountains (light gray)
 		drawCity(batch, 0.25f, 0.25f, 0.25f);
 	}
}