package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements InputProcessor, Screen {

	long lastRenderTime = 0;

	
	void logg(String func) {
		if (Gdx.app != null) {
			Gdx.app.debug("GameScreen", func);
		} else {
			System.out.print(func + ": !Gdx.app\n");
		}
	}
	
	private World 			world;
	private WorldRenderer 	renderer;
	private WorldController	controller;

//	private int width;
	private int height;
	
	public GameScreen() {
		logg("GameScree()");
	}

	@Override
	public void render(float delta) {
	
		long curr = TimeUtils.millis();
		if (curr - lastRenderTime < 1000) {
			logg("render()" + curr);			
			lastRenderTime = curr;
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		controller.update(delta);
		renderer.render();

	}

	@Override
	public void resize(int width, int height) {
		logg("resize("+width+", "+height+")");
		
		renderer.setSize(width, height);
//		this.width = width;
		this.height = height;

	}

	@Override
	public void show() {
		logg("show()");
		
		world = new World();
		renderer = new WorldRenderer(world);
		controller = new WorldController(world);
		Gdx.input.setInputProcessor(this);

	}

	@Override
	public void hide() {
		logg("hide()");
		Gdx.input.setInputProcessor(null);

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
		Gdx.input.setInputProcessor(null);

	}

	@Override
	public boolean keyDown(int keycode) {
		
		switch(keycode) {
		case Input.Keys.UP:
			controller.upPressed();
			break;
		case Input.Keys.DOWN:
			controller.downPressed();
			break;
		case Input.Keys.LEFT:
			controller.leftPressed();
			break;
		case Input.Keys.RIGHT:
			controller.rightPressed();
			break;
		};
		
		logg("keyDown("+keycode+")");
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		switch(keycode) {
		case Input.Keys.UP:
			controller.upReleased();
			break;
		case Input.Keys.DOWN:
			controller.downReleased();
			break;
		case Input.Keys.LEFT:
			controller.leftReleased();
			break;
		case Input.Keys.RIGHT:
			controller.rightReleased();
			break;
		};
		
		logg("keyUp("+keycode+")");
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		logg("keyTyped('"+character+"')");
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		logg("touchDown("+screenX+", "+screenY+", "+pointer+", "+button+")");

		ChangeNavigation(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		logg("touchUp("+screenX+", "+screenY+", "+pointer+", "+button+")");

		controller.resetWay();
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		logg("touchDragged("+screenX+", "+screenY+", "+pointer+")");
		
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
//		ChangeNavigation(x, y);
//		logg("mouseMoved("+x+", "+y+")");
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		logg("scrolled("+amount+")");
		return false;
	}
	

	private void ChangeNavigation(int x, int y){
		controller.resetWay();
		if(height-y >  controller.player.getPosition().y * renderer.ppuY)
			controller.upPressed();
		
		if(height-y <  controller.player.getPosition().y * renderer.ppuY)
			controller.downPressed();
		
		if ( x< controller.player.getPosition().x * renderer.ppuX) 
			controller.leftPressed();
			
		if (x> (controller.player.getPosition().x +Player.SIZE)* renderer.ppuX)
			controller.rightPressed();
			
	}
	
}
