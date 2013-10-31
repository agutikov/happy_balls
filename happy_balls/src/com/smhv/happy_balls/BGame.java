package com.smhv.happy_balls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Game;

public class BGame extends Game {

	long lastRenderTime = 0;

	
	void logg(String func) {
		if (Gdx.app != null) {
			Gdx.app.debug("BGame", func);
		} else {
			System.out.print(func + ": !Gdx.app\n");
		}
	}
	

	public GameScreen gameScreen;
	
	public BGame() {
		logg("constructor BGame()");
	}
	
	
	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		logg("create()");
		
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		logg("resize("+width+", "+height+")");

	}

	@Override
	public void render() {
		super.render();
		
		long curr = TimeUtils.millis();
		if (curr - lastRenderTime > 100000) {

			logg("render()" + curr);
			
			lastRenderTime = curr;
		}
	}

	@Override
	public void pause() {
		super.pause();
		
		logg("pause()");

	}

	@Override
	public void resume() {
		super.resume();
		
		logg("resume()");

	}

	@Override
	public void dispose() {
		super.dispose();
		
		logg("dispose()");

	}
	


}
