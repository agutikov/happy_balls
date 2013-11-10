package com.smhv.happy_balls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

public class BGame extends Game {


	

	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	
	private SoundPlayer soundPlayer;
	
	public BGame() {
	}
	
	
	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Level lvl = Level.createTestLevel01();

		soundPlayer = new SoundPlayer();
		soundPlayer.init();
		
		gameScreen = new GameScreen(soundPlayer);
		menuScreen = new MenuScreen(soundPlayer);
		
		
		
		gameScreen.loadResources();
		
		gameScreen.setLevel(lvl);	
		
		setScreen(menuScreen);
		setScreen(gameScreen);
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
