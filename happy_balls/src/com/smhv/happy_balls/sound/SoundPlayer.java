package com.smhv.happy_balls.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {

	float mainThemeVolume = 0.1f;
	float effectsVolume = 0.3f;
	
	private static Map <String, String> soundFilenames = new HashMap <String, String>();
	
	{
		soundFilenames.put("bomb_explode.mp3", "explosion");
		soundFilenames.put("bomb_put.mp3", "set_bomb");
		soundFilenames.put("enemy_dead.mp3", "murder");
		soundFilenames.put("game_menu.mp3", "menu");
		soundFilenames.put("game_win.mp3", "win");
		soundFilenames.put("player_birth.mp3", "spawn");
		soundFilenames.put("player_dead.mp3", "death");		
	}
	
	private Map <String, Sound> sounds;
	
	private Sound currentTheme;
	
	
	public SoundPlayer() {
		sounds = new HashMap<String, Sound>();

	}
	
	
	public void loadResources () {
		
		for (Entry<String, String> entry : soundFilenames.entrySet()) {
			Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/" + entry.getKey()));
			sounds.put(entry.getValue(), sound);
		}
		
	}
	
	public void pause() {
		for (Entry<String, Sound> entry : sounds.entrySet()) {
			entry.getValue().pause();
		}
	}
	public void resume() {
		for (Entry<String, Sound> entry : sounds.entrySet()) {
			entry.getValue().resume();
		}
	}
	

	public void playExplosion() {

		sounds.get("explosion").play(effectsVolume);	
	}
	
	public void playSetBomb() {
		
	}
	
	public void playMurder() {

		sounds.get("murder").play(effectsVolume);	
	}
	
	public void playSpawn() {

		sounds.get("spawn").play(effectsVolume);
	}
	
	public void playDeath() {

		sounds.get("death").play(effectsVolume);
		
	}
	
	public void playGameOver() {

	}

	
	public void playGameWin() {

		sounds.get("win").play(effectsVolume);
	}


	public void startTheme(String themeName) {
		if (currentTheme != null) {
			currentTheme.stop();
		}
		currentTheme = sounds.get(themeName);
		currentTheme.loop(mainThemeVolume);		
	}


	public void stopTheme() {
		if (currentTheme != null) {
			currentTheme.stop();
			currentTheme = null;
		}		
	}
	

}
