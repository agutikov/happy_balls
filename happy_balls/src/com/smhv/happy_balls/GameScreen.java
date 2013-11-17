package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.TimeUtils;


import com.smhv.happy_balls.level.Level;
import com.smhv.happy_balls.model.*;
import com.smhv.happy_balls.render.WorldRenderer;
import com.smhv.happy_balls.render.WorldRenderingModel;
import com.smhv.happy_balls.sound.SoundPlayer;


public class GameScreen extends BScreen {

	private World 			world;
	private WorldRenderingModel 	model;
	private WorldRenderer 	renderer;
	private GameInputController	input;

	BGameInput gameInput;

	/*
	 * Initialize game engine, no real game data loaded.
	 */	
	public GameScreen() {	
		continuousRendering = true;
		renderer = new WorldRenderer();
	}
	
	public void bindGame(BGameInput gameInput) {
		this.gameInput = gameInput;
	}
	
	public void bindSoundPlayer(SoundPlayer sp) {
		soundPlayer = sp;
	}
	
	/*
	 * Load all resources like textures, sound,
	 * that this screen would use.
	 * For now is not necessary to load data during playing.
	 */
	public void loadResources() {
		//TODO: разделить ресурсы и данные конкретного запуска игры (модели)
		model = new WorldRenderingModel();	
		model.renderer = renderer;
		renderer.bindRenderingModel(model);
		model.loadTextures();
		renderer.loadResources();
	}
	
	public void init(Level lvl) {
		world = new World(model, soundPlayer);
		input = new GameInputController(world, gameInput);	
		
		//TODO: надо всётаки разделять инициализацию уровня и инициализацию и очистку ресурсов игрового движка

		/*
		 * TODO: а вообще надо разделять долго создаваемые данные (загружаемые) 
		 * и временные (данные игрового движка для текущей игры)
		 * и тогда можно создавать скрины каждый раз
		 */
		
		
		world.init(lvl); //тут вызывается model.init()
	}
	
	public void cleanup() {
		model.cleanup();
		world = null;
		input = null;
	}

	
	long inputTime;
	long updateTime;
	long renderTime;
	
	long lastTime = 0;

	@Override
	public void render(float delta) {	
		//TODO: deWiTTERS game loop
		

		long curr = TimeUtils.millis();
		if (curr - lastTime > 1000) {	
			Gdx.app.debug("prof", "" + inputTime + ", " + updateTime + ", " + renderTime);
			lastTime = curr;
		}
		
		
		long tmp = TimeUtils.millis();
		input.processInput();		
		
		inputTime = TimeUtils.millis() - tmp;
		tmp = TimeUtils.millis();
		
		world.update(delta);	
		
		updateTime = TimeUtils.millis() - tmp;
		tmp = TimeUtils.millis();
		
		renderer.render();
		
		renderTime = TimeUtils.millis() - tmp;
		tmp = TimeUtils.millis();
	}

	@Override
	public void resize(int width, int height) {
		
		renderer.setSize(width, height);

	}

	@Override
	public String getThemeName() {
		return "menu";
	}

	@Override
	public InputProcessor getInputProcessor() {
		return input;
	}

	
	
}
