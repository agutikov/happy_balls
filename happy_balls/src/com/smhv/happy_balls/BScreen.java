package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.smhv.happy_balls.sound.SoundPlayer;

public abstract class BScreen implements Screen {

	protected BGameInput gameInput;
	protected SoundPlayer soundPlayer;
	
	protected boolean continuousRendering = false;
	
	/*
	 * Нужен для того чтобы из экрана можно было перейти в другой экран.
	 */
	public void bindGame(BGameInput gameInput) {
		this.gameInput = gameInput;
	}
	
	/*
	 * 
	 */
	public void bindSoundPlayer(SoundPlayer sp) {
		soundPlayer = sp;
	}
	
	public abstract String getThemeName();
	public abstract InputProcessor getInputProcessor();
	
	/*
	 * TODO: 
	 * show() и hide() должны только запускать и останавливать экран
	 * все необходимые ресы должны подгружаться отдельно, в отдельный объект,
	 * который будет ресурс менеджер.
	 * А экран будет только брать и пользоваться ресами из менеджера.
	 * Ресурс менеджер может запускать отдельный поток для загрузки ресов.
	 * 
	 * 
	 */
	
	@Override
	public void show() {
		soundPlayer.startTheme(getThemeName());
		
		Gdx.graphics.setContinuousRendering(continuousRendering);

		Gdx.input.setInputProcessor(getInputProcessor());
	}

	@Override
	public void hide() {		
		Gdx.input.setInputProcessor(null);
		
		soundPlayer.stopTheme();
	}
	
	public BScreen() {
	}

	
	public void loadResources() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		soundPlayer.pause();
	}

	@Override
	public void resume() {
		soundPlayer.resume();
	}

	@Override
	public void dispose() {
		
	}
	
}
