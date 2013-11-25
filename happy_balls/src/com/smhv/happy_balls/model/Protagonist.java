package com.smhv.happy_balls.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;



public class Protagonist extends FreeObject {

	public float safeTime;
	private boolean safeModeDisabled = true;
	
	
	public void resurrection() {
		alive = true;
		safeTime = 3f;
	}
	
	public boolean isUndamagableDisabled() {
		if (safeModeDisabled) {
			safeModeDisabled = false;
			return true;
		} 
		return false;
	}
	
	public void update(float delta) {
		super.update(delta);
		if (safeTime > 0f) {
			safeTime -= delta;
			if (safeTime <= 0f)
				safeModeDisabled = true;
		}
	}
	
	public boolean kill() {
		if (safeTime <= 0) {
			return super.kill();
		} else {
			return false;
		}
	}
	
	public Protagonist(Vector2 pos) {
		super("protagonist", pos);
		speed = 5f;
		safeTime = 0f;	
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
