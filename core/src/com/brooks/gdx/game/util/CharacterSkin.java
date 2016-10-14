package com.brooks.gdx.game.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by: Becky Brooks
 */
public enum CharacterSkin
{
	//Declare variables
	WHITE("White", 1.0f, 1.0f, 1.0f),
	GRAY("Gray", 0.7f, 0.7f, 0.7f),
	BROWN("Brown", 0.7f, 0.5f, 0.3f);
	private String name;
	private Color color = new Color();
	
	/**
	 * CharacterSkin method
	 * @param name
	 * @param r
	 * @param g
	 * @param b
	 */
	private CharacterSkin (String name, float r, float g, float b)
	{
		this.name= name;
		color.set(r, g, b, 1.0f);
	}
	
	/**
	 * ToString method
	 */
	@Override
	public String toString()
	{
		return name;
	}
	
	/**
	 * GetColor method
	 * @return
	 */
	public Color getColor ()
	{
		return color;
	}
}