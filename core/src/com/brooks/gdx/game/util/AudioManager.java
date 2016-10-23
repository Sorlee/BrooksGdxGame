package com.brooks.gdx.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Create by: Becky Brooks
 */
public class AudioManager
{
	/**
	 * Declare variables
	 */
	public static final AudioManager instance = new AudioManager();
	private Music playingMusic;
	
	/**
	 * singleton: prevent instantiation from other classes
	 */
	private AudioManager()
	{
	}
	
	/**
	 * Play function
	 * @param sound
	 */
	public void play (Sound sound)
	{
		play(sound, 1);
	}
	
	/**
	 * Play function
	 * @param sound
	 * @param volume
	 */
	public void play (Sound sound, float volume)
	{
		play(sound, volume, 1);
	}
	
	/**
	 * Play function
	 * @param sound
	 * @param volume
	 * @param pitch
	 */
	public void play (Sound sound, float volume, float pitch)
	{
		play(sound, volume, pitch, 0);
	}
	
	/**
	 * Play function
	 * @param sound
	 * @param volume
	 * @param pitch
	 * @param pan
	 */
	public void play (Sound sound, float volume, float pitch, float pan)
	{
		if (!GamePreferences.instance.sound)
			return;
		sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
	}
	
	/**
	 * Play function
	 * @param music
	 */
	public void play (Music music)
	{
		stopMusic();
		playingMusic = music;
		if (GamePreferences.instance.music)
		{
			music.setLooping(true);
			music.setVolume(GamePreferences.instance.volMusic);
			music.play();
		}
	}
	
	/**
	 * StopMusic function
	 */
	public void stopMusic()
	{
		if (playingMusic != null)
			playingMusic.stop();
	}
	
	/**
	 * OnSettingsUpdate function
	 */
	public void onSettingsUpdated()
	{
		if (playingMusic == null)
			return;
		playingMusic.setVolume(GamePreferences.instance.volMusic);
		if (GamePreferences.instance.music)
		{
			if (!playingMusic.isPlaying())
				playingMusic.play();
		}
		else
			playingMusic.pause();
	}
}
