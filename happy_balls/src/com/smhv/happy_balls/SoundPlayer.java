package com.smhv.happy_balls;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {

	float volume = 0.1f;
	
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
	
	
	
	public SoundPlayer() {
		sounds = new HashMap<String, Sound>();

	}
	
	
	public void init () {
		
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
	
	
	public void startGameTrack() {
		sounds.get("menu").loop(volume);	
	}
	public void stopGameTrack() {
		sounds.get("menu").stop();	
	}
	
	public void startMenuTrack() {	
	}	
	public void stopMenuTrack() {	
	}
	
	public void playExplosion() {

		sounds.get("explosion").play(volume);	
	}
	
	public void playSetBomb() {
		
	}
	
	public void playMurder() {

		sounds.get("murder").play(volume);	
	}
	
	public void playSpawn() {

		sounds.get("spawn").play(volume);
	}
	
	public void playDeath() {

		sounds.get("death").play(volume);
		
	}
	
	public void playGameOver() {

	}

	
	public void playGameWin() {

		sounds.get("win").play(volume);
	}
	

}
