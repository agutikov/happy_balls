package com.smhv.happy_balls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class BGame implements ApplicationListener {

	long lastRenderTime = 0;

	boolean logConfigured = false;
	
	void logg(String func) {
		if (Gdx.app != null) {
			if (!logConfigured) {
				Gdx.app.setLogLevel(Application.LOG_DEBUG);
				logConfigured = true;
			}
			Gdx.app.debug("BGame", func);
		} else {
			System.out.print(func + ": !Gdx.app\n");
		}
	}
	
	public BGame() {
		logg("constructor BGame()");
	}
	
	
	@Override
	public void create() {
		logg("create()");
	}

	@Override
	public void resize(int width, int height) {

		logg("resize()");

	}

	@Override
	public void render() {
		long curr = TimeUtils.millis();
		if (curr - lastRenderTime < 1000) {

			logg("render()" + curr);
			
			lastRenderTime = curr;
		}
	}

	@Override
	public void pause() {
		logg("pause()");

	}

	@Override
	public void resume() {
		logg("resume()");

	}

	@Override
	public void dispose() {
		logg("dispose()");

	}

}
