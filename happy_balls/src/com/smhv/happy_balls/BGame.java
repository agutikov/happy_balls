package com.smhv.happy_balls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.smhv.happy_balls.model.Level;

public class BGame extends Game implements BGameInput {


	

	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	
	
	public void startGame () {
		gameScreen.loadResources();		
		Level lvl = Level.createTestLevel01();
		gameScreen.setLevel(lvl);					
		
		setScreen(gameScreen);
	}
	
	public void quitToMenu () {		
		setScreen(menuScreen);
	}
	
	private SoundPlayer soundPlayer;
	
	public BGame() {
	}
	
	
	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);


		soundPlayer = new SoundPlayer();
		soundPlayer.loadResources();
		
		gameScreen = new GameScreen(this, soundPlayer);
		menuScreen = new MenuScreen(this, soundPlayer);
		

		menuScreen.loadResources();
		
		setScreen(menuScreen);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

	}

	@Override
	public void render() {
		super.render();
		
	}

	@Override
	public void pause() {
		super.pause();
		

	}

	@Override
	public void resume() {
		super.resume();
		

	}

	@Override
	public void dispose() {
		super.dispose();
		

	}
	


}
