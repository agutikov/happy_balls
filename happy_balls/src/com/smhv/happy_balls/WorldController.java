package com.smhv.happy_balls;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;


public class WorldController {
	
	enum Keys {
		LEFT, RIGHT, UP, DOWN, TOUCH
	}
	
	public Player player;
	
	static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.TOUCH, false);
	};

	public WorldController(World world) {

	
		this.player = world.getPlayer();
	}
	

	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}
	
	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}
	
	public void upPressed() {
		keys.get(keys.put(Keys.UP, true));
	}
	
	public void downPressed() {
		keys.get(keys.put(Keys.DOWN, true));
	}
	
	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}
	
	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}
	
	public void upReleased() {
		keys.get(keys.put(Keys.UP, false));
	}
	
	public void downReleased() {
		keys.get(keys.put(Keys.DOWN, false));
	}
	
	public void touchDown(Vector2 place) {
		
		keys.get(keys.put(Keys.TOUCH, true));
		player.setDest(place);
	}
	
	public void touchDragged(Vector2 newPlace) {
		player.setDest(newPlace);
	}
	
	public void touchUp() {
		player.stop();
		player.clearDest();
		keys.get(keys.put(Keys.TOUCH, false));
	}
	
	public void update(float delta) {

		processInput();
		player.update(delta);

	}
	
	public void resetWay(){
		rightReleased();
		leftReleased();
		downReleased();
		upReleased();
	}
	
	private void processInput() {
		
		if (!keys.get(Keys.TOUCH)) {
		
			if (keys.get(Keys.LEFT)) 
				player.go(-1, 0);
				
			
			if (keys.get(Keys.RIGHT))
				player.go(1, 0);
			
			if (keys.get(Keys.UP)) 
				player.go(0, 1);
			
			
			if (keys.get(Keys.DOWN))
				player.go(0, -1);

			if ((keys.get(Keys.DOWN) && keys.get(Keys.UP)) ||
					(!keys.get(Keys.DOWN) && !keys.get(Keys.UP)))
				player.stop(false, true);

			if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) ||
					(!keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT)))
				player.stop(true, false);
		}

	}
}
