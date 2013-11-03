package com.smhv.happy_balls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Game;

public class BGame extends Game {


	

	public GameScreen gameScreen;
	
	public BGame() {
	}
	
	
	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Level lvl = Level.createTestLevel();
		
		gameScreen = new GameScreen();
		
		gameScreen.loadResources();
		
		gameScreen.setLevel(lvl);	
		
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
