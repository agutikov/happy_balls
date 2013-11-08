package com.smhv.happy_balls.model;


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
	
	public void detonate() {
		timeLeft = 0;
	}

}
