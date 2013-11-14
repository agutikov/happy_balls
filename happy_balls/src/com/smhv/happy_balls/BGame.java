package com.smhv.happy_balls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.smhv.happy_balls.level.Level;
import com.smhv.happy_balls.sound.SoundPlayer;

public class BGame extends Game implements BGameInput {


	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	
	private SoundPlayer soundPlayer;	

	Level levels[] = new Level[3];
	
	public void startNewGame () {	
		gameScreen.init(levels[0]);		
		
		setScreen(gameScreen);
	}
	
	public void quitToMenu () {	
		gameScreen.cleanup();
		
		setScreen(menuScreen);
	}
	
	
	public BGame() {
	}
	
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.debug("BGame", "create");
		

		Gdx.app.debug("BGame", "DisplayMode: " + Gdx.graphics.getDesktopDisplayMode().width + "x" + Gdx.graphics.getDesktopDisplayMode().height);
		
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		Gdx.graphics.setVSync(true);
			
		

		soundPlayer = new SoundPlayer();
		gameScreen = new GameScreen();
		menuScreen = new MenuScreen();
		
		gameScreen.bindGame(this);
		gameScreen.bindSoundPlayer(soundPlayer);
		
		menuScreen.bindGame(this);
		menuScreen.bindSoundPlayer(soundPlayer);		

		levels[0] = Level.createTestLevel01();
		levels[1] = Level.createTestLevel02();
		levels[2] = Level.createTestLevel03();
		
		soundPlayer.loadResources();
		menuScreen.loadResources();
		gameScreen.loadResources();	
		
		setScreen(menuScreen);
	}

}
