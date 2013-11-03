package com.smhv.happy_balls.model;

public class GroundSquare extends FixedObject {

	public GroundSquare() {
		super("ground", Orientation.DEFAULT);
	}

	@Override
	public boolean isEternal() {
		return true;
	}
	


}
