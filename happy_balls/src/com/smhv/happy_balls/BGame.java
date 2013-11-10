package com.smhv.happy_balls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

public class BGame extends Game {


	

	protected GameScreen gameScreen;
	protected MenuScreen menuScreen;
	
	private SoundPlayer soundPlayer;
	
	public BGame() {
	}
	
	
	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);


		soundPlayer = new SoundPlayer();
		soundPlayer.init();
		
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
