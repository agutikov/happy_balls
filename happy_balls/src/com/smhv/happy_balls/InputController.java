package com.smhv.happy_balls;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.smhv.happy_balls.model.WorldInput;
import com.smhv.happy_balls.model.WorldInput.ControlDirection;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.TimeUtils;

public class InputController implements InputProcessor {
	
	WorldInput world;
	
	enum Keys {
		LEFT, RIGHT, UP, DOWN
	}	
	
	static Map<Keys, Boolean> keys = new HashMap<InputController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
	};

	public InputController(WorldInput controlledWorld) {
		world = controlledWorld;
	}
	

	@Override
	public boolean keyDown(int keycode) {
		
		
		switch(keycode) {
		case Input.Keys.UP:
			keys.get(keys.put(Keys.UP, true));
			break;
		case Input.Keys.DOWN:
			keys.get(keys.put(Keys.DOWN, true));
			break;
		case Input.Keys.LEFT:
			keys.get(keys.put(Keys.LEFT, true));
			break;
		case Input.Keys.RIGHT:
			keys.get(keys.put(Keys.RIGHT, true));
			break;
		case Input.Keys.SPACE:
			// no need to save the state like with direction keys
			world.putBomb();
			break;
		};
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {

		switch(keycode) {
		case Input.Keys.UP:
			keys.get(keys.put(Keys.UP, false));
			break;
		case Input.Keys.DOWN:
			keys.get(keys.put(Keys.DOWN, false));
			break;
		case Input.Keys.LEFT:
			keys.get(keys.put(Keys.LEFT, false));
			break;
		case Input.Keys.RIGHT:
			keys.get(keys.put(Keys.RIGHT, false));
			break;
		};
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	/*
	 * Convert real impact on controls to
	 * virtual impact in world model.
	 */
	public void processInput() {
		
		int horizontal = 0;
		int vertical = 0;		
		
		if (keys.get(Keys.LEFT))
			horizontal--;		
		if (keys.get(Keys.RIGHT))
			horizontal++;	
		if (keys.get(Keys.UP))
			vertical++;		
		if (keys.get(Keys.DOWN))
			vertical--;		
		
		if (horizontal == 0) {
			if (vertical == 0) {
				world.stop();
			} else if (vertical == 1) {
				world.go(ControlDirection.UP);
			} else {
				world.go(ControlDirection.DOWN);				
			}
		} else if (horizontal == 1) {
			if (vertical == 0) {
				world.go(ControlDirection.RIGHT);
			} else if (vertical == 1) {
				world.go(ControlDirection.RIGHT_UP);
			} else {
				world.go(ControlDirection.RIGHT_DOWN);
			}
		} else {
			if (vertical == 0) {
				world.go(ControlDirection.LEFT);
			} else if (vertical == 1) {
				world.go(ControlDirection.LEFT_UP);
			} else {
				world.go(ControlDirection.LEFT_DOWN);
			}
		}
		
		
	}
}
