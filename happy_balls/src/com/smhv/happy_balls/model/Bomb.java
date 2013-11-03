package com.smhv.happy_balls.model;


public class Bomb extends FixedObject {

	public Bomb() {
		super("bomb", Orientation.DEFAULT);
	}

	@Override
	public boolean isEternal() {
		return false;
	}
	

}
