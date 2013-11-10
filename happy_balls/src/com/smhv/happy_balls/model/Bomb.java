package com.smhv.happy_balls.model;

import com.badlogic.gdx.Gdx;


public class Bomb extends FixedObject {

	float timeLeft;
	
	
	public Bomb() {
		super("bomb", Orientation.DEFAULT);
		
		timeLeft = 3;
	}

	@Override
	public boolean isEternal() {
		return false;
	}
	
	public boolean update(float delta) {
		timeLeft -= delta;
		return timeLeft <= 0f;
	}
	
	public int getState() {
		if (timeLeft > 1)
			return 0;
		else if (timeLeft > 0.5f) 
			return 1;
		else 
			return 2;
	}
	
	public void detonate() {
		timeLeft = 0;
	}

}
