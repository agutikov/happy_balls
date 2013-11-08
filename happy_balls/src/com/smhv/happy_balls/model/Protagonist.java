package com.smhv.happy_balls.model;

import com.badlogic.gdx.math.Vector2;



public class Protagonist extends FreeObject {

	public void resurrection() {
		alive = true;
	}
	
	public Protagonist(Vector2 pos) {
		super("protagonist", pos);
		speed = 5f;
		
	}
	
	public void go(FreeObject.Direction dir) {
		setDirection(dir);		
	}


	public void stop() {
		setDirection(Direction.NONE);
	}

	public void putBomb() {
		
	}

	@Override
	public void ballsToTheWall() {
		// TODO Auto-generated method stub
		
	}

	
}
