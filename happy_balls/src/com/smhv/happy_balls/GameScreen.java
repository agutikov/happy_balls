package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.FPSLogger;

import com.smhv.happy_balls.model.*;


public class GameScreen implements Screen {

	private World 			world;
	private WorldRenderingModel 	model;
	private WorldRenderer 	renderer;
	private InputController	input;
	
	private SoundPlayer soundPlayer;
	
	private boolean running = false;


	/*
	 * Initialize game engine, no real game data loaded.
	 */	
	public GameScreen(SoundPlayer sp) {
		
		soundPlayer = sp;
		
		model = new WorldRenderingModel();
		world = new World(model, sp);
		renderer = new WorldRenderer(model);
		input = new InputController(world);
		
		model.renderer = renderer;
	}
	
	/*
	 * Load all resources like textures, sound,
	 * that this screen would use.
	 * For now is not necessary to load data during playing.
	 */
	public void loadResources() {
		model.loadTextures();
	}
	
	/*
	 * Level should be already set before calling setScreen().
	 */
	public void setLevel(Level lvl) {
		world.initLevel(lvl);
	}

	@Override
	public void render(float delta) {	
		//TODO: deWiTTERS game loop
		
		if (running) {
			input.processInput();		
			world.update(delta);			
			renderer.render();
		}
	}

	@Override
	public void resize(int width, int height) {
		
		renderer.setSize(width, height);

	}

	@Override
	public void show() {	
		Gdx.app.debug("", "GameScreen.show()");
		
		Gdx.input.setInputProcessor(input);
		
		soundPlayer.startGameTrack();
		
		running = true;

	}

	@Override
	public void hide() {
		Gdx.app.debug("", "GameScreen.hide()");
		
		Gdx.input.setInputProcessor(null);

		running = false;
	}

	@Override
	public void pause() {
		Gdx.app.debug("", "GameScreen.pause()");

		
		

		running = false;
	}

	@Override
	public void resume() {
		Gdx.app.debug("", "GameScreen.resume()");


		running = true;
	}

	@Override
	public void dispose() {
		
		
		Gdx.input.setInputProcessor(null);

	}

	
	
}
