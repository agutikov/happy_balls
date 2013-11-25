package com.smhv.happy_balls;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.smhv.happy_balls.model.WorldInput;
import com.smhv.happy_balls.model.WorldInput.ControlDirection;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameInputController implements InputProcessor {
	
	WorldInput world;
	
	BGameInput gameInput;

	private float walkControlRadius = 56;
	private Vector2 walkControlPos = new Vector2(0, 0);

	private float bombControlRadius = 56;
	private Vector2 bombControlPos = new Vector2(0, 0);

	private float pauseControlRadius = 24;
	private Vector2 pauseControlPos = new Vector2(0, 0);
	
	private Rectangle viewport = new Rectangle();
	
	enum Keys {
		LEFT, RIGHT, UP, DOWN
	}	
	
	private Map<Keys, Boolean> keys = new HashMap<GameInputController.Keys, Boolean>();
	private Map<Integer, Vector2> touch = new HashMap<Integer, Vector2>();
	

	public GameInputController(WorldInput controlledWorld, BGameInput gameInput) {
		world = controlledWorld;
		this.gameInput = gameInput;
		
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
	}
	
	/*
	 * TODO: связать InputController и Renderer чтобы координаты соответсвовали изоборажению
	 * может вызывать setSize из рендерера?
	 */
	public void setSize(Rectangle r) {
		viewport = new Rectangle(r);
		
		walkControlPos.x = 10 + walkControlRadius;
		walkControlPos.y = 10 + walkControlRadius;
		
		bombControlPos.x = viewport.width - 10 - bombControlRadius;
		bombControlPos.y = 10 + bombControlRadius;
		
		pauseControlPos.x = viewport.width - 10 - pauseControlRadius;
		pauseControlPos.y = viewport.height / 2 + pauseControlRadius;
		
		
	}

	@Override
	public boolean keyDown(int keycode) {

		switch(keycode) {
		case Input.Keys.UP:
			keys.put(Keys.UP, true);
			break;
		case Input.Keys.DOWN:
			keys.put(Keys.DOWN, true);
			break;
		case Input.Keys.LEFT:
			keys.put(Keys.LEFT, true);
			break;
		case Input.Keys.RIGHT:
			keys.put(Keys.RIGHT, true);
			break;
		case Input.Keys.SPACE:
			// no need to save the state like with direction keys
			world.putBomb();
			break;

		case Input.Keys.ESCAPE:
			gameInput.quitToMenu();
			break;
		};
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {

		switch(keycode) {
		case Input.Keys.UP:
			keys.put(Keys.UP, false);
			break;
		case Input.Keys.DOWN:
			keys.put(Keys.DOWN, false);
			break;
		case Input.Keys.LEFT:
			keys.put(Keys.LEFT, false);
			break;
		case Input.Keys.RIGHT:
			keys.put(Keys.RIGHT, false);
			break;
		};
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
			
		touch.put(pointer, new Vector2(x, viewport.height - y));
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		
		touch.remove(pointer);
		
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		
		touch.get(pointer).x = x;
		touch.get(pointer).y = viewport.height - y;
		
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
	
	private boolean isIn (Vector2 pos, Vector2 from, float r) {
		return from.dst(pos) < r;
	}
	
	/*
	 * Convert real impact on controls to
	 * virtual impact in world model.
	 */
	public void processInput() {
		
		int horizontal = 0;
		int vertical = 0;		
		
		for (Entry<Integer, Vector2> e : touch.entrySet()) {
			Vector2 v = e.getValue();
			// радиус детектирования больше - чтобы можно было нажимать на край контрола
			if (isIn(v, walkControlPos, walkControlRadius + 10)) {
				Vector2 rel = new Vector2(v);
				rel.sub(walkControlPos);
								
				float a = rel.angle();
				a += 22.5;
				if (a > 360)
					a -= 360;
				a /= 45;
				
				int i = (int) Math.floor(a);
				
				switch (i) {
				case 0:
					horizontal++;
					break;
				case 1:
					horizontal++;
					vertical++;
					break;
				case 2:
					vertical++;
					break;
				case 3:
					horizontal--;
					vertical++;
					break;
				case 4:
					horizontal--;
					break;
				case 5:
					horizontal--;
					vertical--;
					break;
				case 6:
					vertical--;
					break;
				case 7:
					horizontal++;
					vertical--;
					break;
				default:
					break;
				}				
			}
			if (isIn(v, bombControlPos, bombControlRadius)) {
				world.putBomb();
			}
			if (isIn(v, pauseControlPos, pauseControlRadius)) {
								
			}			
		}
		
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
