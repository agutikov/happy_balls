package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;


import com.smhv.happy_balls.model.*;
import com.smhv.happy_balls.render.WorldRenderer;
import com.smhv.happy_balls.render.WorldRenderingModel;


public class GameScreen implements Screen {

	private World 			world;
	private WorldRenderingModel 	model;
	private WorldRenderer 	renderer;
	private GameInputController	input;
	
	private SoundPlayer soundPlayer;
	
	private boolean running = false;

	BGame game;

	/*
	 * Initialize game engine, no real game data loaded.
	 */	
	public GameScreen(BGame game, SoundPlayer sp) {
		
		this.game = game;
		
		soundPlayer = sp;
		
		model = new WorldRenderingModel();
		world = new World(model, sp);
		renderer = new WorldRenderer(model);
		input = new GameInputController(world, game);
		
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

		Gdx.graphics.setContinuousRendering(true);
		
		soundPlayer.startGameTrack();
		
		running = true;

	}

	@Override
	public void hide() {
		Gdx.app.debug("", "GameScreen.hide()");
		
		Gdx.input.setInputProcessor(null);

		soundPlayer.stopGameTrack();
		
		running = false;
	}

	@Override
	public void pause() {
		Gdx.app.debug("", "GameScreen.pause()");

		soundPlayer.pause();
		

		running = false;
	}

	@Override
	public void resume() {
		Gdx.app.debug("", "GameScreen.resume()");


		soundPlayer.resume();
		
		running = true;
	}

	@Override
	public void dispose() {
		
		
		Gdx.input.setInputProcessor(null);

	}

	
	
}
