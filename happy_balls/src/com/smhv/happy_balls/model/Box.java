package com.smhv.happy_balls.model;

public class Box extends FixedObject {

	private int hitpoints;
	private boolean breakable;
	
	public boolean isEternal() {
		return !breakable;
	}
	
	public Box(String type, Orientation orient) {
		super(type, orient);
		if (type == "box") {
			hitpoints = 1;
			breakable = true;
		} else if (type == "brick") {
			hitpoints = 0;
			breakable = false;
		} else if (type == "wall") {
			hitpoints = 0;
			breakable = false;
		} else if (type == "corner") {
			hitpoints = 0;
			breakable = false;
		}
	}


	
}
