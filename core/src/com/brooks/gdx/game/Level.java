package com.brooks.gdx.game;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.brooks.gdx.game.objects.AbstractGameObject;
import com.brooks.gdx.game.objects.Clouds;
import com.brooks.gdx.game.objects.City;
import com.brooks.gdx.game.objects.Rock;
import com.brooks.gdx.game.objects.GooOverlay;
import com.brooks.gdx.game.objects.Knight;
import com.brooks.gdx.game.objects.Potion;
import com.brooks.gdx.game.objects.Orange;
import com.brooks.gdx.game.objects.Enemy;

/**
 * Created by: Becky Brooks
 */
public class Level
{
	//Declare variables
 	public static final String TAG = Level.class.getName();
 	public Knight knight;
 	//Objects
 	public Array<Orange> oranges;
 	public Array<Potion> potions;
 	public Array<Enemy> enemies;
 	public Array<Rock> rocks;
 	
 	//State the color pixel that represents each asset
 	public enum BLOCK_TYPE
 	{
 		EMPTY(0, 0, 0), //black
 		ROCK(255, 0, 0), //red
 		PLAYER_SPAWNPOINT(255, 255, 255), //white
 		ITEM_POTION(0, 255, 0), //green
 		ITEM_ORANGE(255, 255, 0), //yellow
 		ENEMY(0, 0, 255); //blue
 
 		private int color;
 		//Bit-shift RGB to use Hexidecimal
 		private BLOCK_TYPE (int r, int g, int b)
 		{
 			color = r << 24 | g << 16 | b << 8 | 0xff;
 		}
 
 		public boolean sameColor (int color)
 		{
 			return this.color == color;
 		}
 
 		public int getColor()
 		{
 			return color;
 		}
 	}
 
 	//Decoration
 	public Clouds clouds;
 	public City city;
 	public GooOverlay gooOverlay;
 
 	/**
 	 * Level method
 	 * @param filename
 	 */
 	public Level (String filename)
 	{
 		init(filename);
 	}
 
 	/**
 	 * Init method
 	 * @param filename
 	 */
 	private void init (String filename)
 	{
 		//Player character
 		knight = null;
 		//Objects
 		rocks = new Array<Rock>();
 		oranges = new Array<Orange>();
 		potions = new Array<Potion>();
 		enemies = new Array<Enemy>();
 
 		//Load image file that represents the level data
 		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
 		//Scan pixels from top-left to bottom-right
 		int lastPixel = -1;
 		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
 		{
 			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
 			{
 				AbstractGameObject obj = null;
 				float offsetHeight = 0;
 				//Height grows from bottom to top
 				float baseHeight = pixmap.getHeight() - pixelY;
 				//Get color of current pixel as 32-bit RGBA value
 				int currentPixel = pixmap.getPixel(pixelX, pixelY);
 				//Find matching color value to identify block type at (x,y) point and create the corresponding game object if there is a match
 
 				//Empty space
 				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
 				{
 					//do nothing
 				}
 				//Rock
 				else if (BLOCK_TYPE.ROCK.sameColor(currentPixel))
 				{
 					if (lastPixel != currentPixel)
 					{
 						obj = new Rock();
 						float heightIncreaseFactor = 0.25f;
 						offsetHeight = -2.5f;
 						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
 						rocks.add((Rock)obj);
 					}
 					else
 					{
 						rocks.get(rocks.size - 1).increaseLength(1);
 					}
 				}
 				//Player spawn point
 				else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
 				{
 					obj = new Knight();
 					offsetHeight = 8.0f;
 					obj.position.set(pixelX, baseHeight * obj.dimension.y - offsetHeight);
 					knight = (Knight) obj;
 				}
 				//Potion
 				else if (BLOCK_TYPE.ITEM_POTION.sameColor(currentPixel))
 				{
 					obj = new Potion();
 					offsetHeight = -1.5f;
 					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
 					potions.add((Potion)obj);
 				}
 				//Orange
 				else if (BLOCK_TYPE.ITEM_ORANGE.sameColor(currentPixel))
 				{
 					obj = new Orange();
 					offsetHeight = -1.5f;
 					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
 					oranges.add((Orange)obj);
 				}
 				//Enemies
 				else if (BLOCK_TYPE.ENEMY.sameColor(currentPixel))
 				{
 					obj = new Enemy();
 					offsetHeight = -3.0f;
 					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
 					enemies.add((Enemy)obj);
 				}
 				//Unknown object / pixel color
 				else
 				{
 					int r = 0xff & (currentPixel >>> 24); //Red color channel
 					int g = 0xff & (currentPixel >>> 16); //Green color channel
 					int b = 0xff & (currentPixel >>> 8); //Blue color channel
 					int a = 0xff & currentPixel; //Alpha channel
 					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
 				}
 				lastPixel = currentPixel;
 			}
 		}
 
 		//Decoration
 		clouds = new Clouds(pixmap.getWidth());
 		clouds.position.set(0, 2);
 		city = new City(pixmap.getWidth());
 		city.position.set(-1, 1);
 		gooOverlay = new GooOverlay(pixmap.getWidth());
 		gooOverlay.position.set(0, -3.75f);
 
 		//Free memory
 		pixmap.dispose();
 		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
 	}
 
 	/**
 	 * Render method
 	 * @param batch
 	 */
 	public void render (SpriteBatch batch)
 	{
 		//Draw mountains
 		city.render(batch);
 		//Draw rocks
 		for (Rock rock : rocks)
 			rock.render(batch);
 		//Draw Gold Coins
 		for (Orange orange : oranges)
 			orange.render(batch);
 		//Draw Feathers
 		for (Potion potion : potions)
 			potion.render(batch);
 		//Draw Enemies
 		for (Enemy enemy : enemies)
 			enemy.render(batch);
 		//Draw Player Character
 		knight.render(batch);
 		//Draw waterOverlay
 		gooOverlay.render(batch);
 		//Draw Clouds
 		clouds.render(batch);
 	}
 	
 	/**
 	 * Update method
 	 * @param deltaTime
 	 */
 	public void update (float deltaTime)
 	{
 		knight.update(deltaTime);
 		for (Rock rock : rocks)
 			rock.update(deltaTime);
 		for (Orange orange : oranges)
 			orange.update(deltaTime);
 		for (Potion potion : potions)
 			potion.update(deltaTime);
 		for (Enemy enemy : enemies)
 			enemy.update(deltaTime);
 		clouds.update(deltaTime);
 	}
 }