package com.brooks.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.brooks.gdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.brooks.gdx.game.util.GamePreferences;

/**
 * Created by: Becky Brooks
 */
public class WorldRenderer implements Disposable
{
	//Declare variables
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera cameraGUI;

	//Initialize the WorldRenderer
	public WorldRenderer (WorldController worldController)
	{
		this.worldController = worldController;
		init();
	}
 
	//Init function
	private void init ()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true);	//flip y-axis
		cameraGUI.update();
	}

	//Render function
	public void render ()
	{
		renderWorld(batch);
		renderGui(batch);
	}

	//RenderWorld function
	private void renderWorld (SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}

	//Resize function
	public void resize (int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height * (float)width);
		cameraGUI.position.set(cameraGUI.viewportWidth/ 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}

	//Dispose function
	@Override
	public void dispose ()
	{
		batch.dispose();
	}
	
	//RenderGuiScore function
	private void renderGuiScore (SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.orange.orange, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y + 37);
	}
	
	//RenderGuiExtraLive function
	private void renderGuiExtraLive (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 25 - Constants.LIVES_START * 50;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++)
		{
			if (worldController.lives <= i)
			{
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			}
			batch.draw(Assets.instance.knight.knight, x + i * 50, y, 50, 50, 22, 49, 1f, -1f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}
	
	//RenderGuiFpsCounter function
	private void renderGuiFpsCounter (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps >= 45)
		{
			//45 or more FPS show up in green
			fpsFont.setColor(0,1,0,1);
		}
		else if (fps >= 30)
		{
			//30 or more FPS show up in yellow
			fpsFont.setColor(1,1,0,1);
		}
		else
		{
			//Less than 30 FPS show up in red
			fpsFont.setColor(1,0,0,1);
		}
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1,1,1,1);	//white
	}
	
	//RenderGui function
	private void renderGui (SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		//Draw collected gold coins icon + text (anchored to top left edge)
		renderGuiScore(batch);
		//Draw collected feather icon (anchored to top left edge)
		renderGuiPotionPowerup(batch);
		//Draw extra lives icon + text (anchored to top right edge)
		renderGuiExtraLive(batch);
		//Draw FPS text (anchored to bottom right edge)
		if (GamePreferences.instance.showFpsCounter)
			renderGuiFpsCounter(batch);
		//Draw game over text
		renderGuiGameOverMessage(batch);
		batch.end();
	}
	
	//RenderGuiGameOverMessage function
	private void renderGuiGameOverMessage (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		if (worldController.isGameOver())
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, true);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}
	
	//RenderGuiPotionPowerup function
	private void renderGuiPotionPowerup(SpriteBatch batch)
	{
		float x = -15;
		float y = 30;
		float timeLeftPotionPowerup = worldController.level.knight.timeLeftPotionPowerup;
		if (timeLeftPotionPowerup > 0)
		{
			//Start icon fade in/out if the left power-up time is less than 4 seconds. The fade interval is set to 5 changes per second
			if (timeLeftPotionPowerup < 4)
			{
				if (((int)(timeLeftPotionPowerup * 5) % 2) != 0)
					batch.setColor(1, 1, 1, 0.5f);
			}
			batch.draw(Assets.instance.potion.potion, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
			Assets.instance.fonts.defaultSmall.draw(batch,  "" + (int)timeLeftPotionPowerup, x + 60, y + 57);
		}
	}
}
