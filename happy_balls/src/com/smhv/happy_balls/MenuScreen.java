package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class MenuScreen implements Screen {

	

	private SoundPlayer soundPlayer;
	
	public MenuScreen(SoundPlayer sp) {
		soundPlayer = sp;
		
	}

	@Override
	public void render(float delta) {
		Gdx.graphics.requestRendering();

	}

	@Override
	public void resize(int width, int height) {
		Gdx.graphics.requestRendering();

	}

	@Override
	public void show() {
		soundPlayer.startMenuTrack();

		Gdx.graphics.setContinuousRendering(false);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
