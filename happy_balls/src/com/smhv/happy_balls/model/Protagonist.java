package com.smhv.happy_balls.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;



public class Protagonist extends FreeObject {

	private float safeTime;
	
	public void resurrection() {
		alive = true;
		safeTime = 10f;
		Gdx.app.debug("resurrection", "" + safeTime);
	}
	
	public void update(float delta) {
		super.update(delta);
		Gdx.app.debug("update", safeTime + " -= " + delta);
		if (safeTime > 0f) {
			safeTime -= delta;
		}
	}
	
	public boolean kill() {
		Gdx.app.debug("kill", "" + safeTime);
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
		Gdx.app.debug("Protagonist", "" + safeTime);
		
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
