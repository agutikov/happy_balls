package com.smhv.happy_balls.model;

import java.util.Random;

public class Box extends FixedObject {
	
	private static Random random = new Random();
	
	private int hitpoints;
	private boolean breakable;
	
	public boolean isEternal() {
		return !breakable;
	}
	
	public boolean explode() {
		if (breakable) {
			hitpoints--;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isAlive() {
		return hitpoints > 0;
	}
	
	public Box(String type, Orientation orient) {
		super(type, orient);
		if (type == "box") {
			hitpoints = 1;
			breakable = true;
		} else if (type == "brick") {
			hitpoints = 1;
			breakable = false;
		} else if (type == "wall") {
			hitpoints = 1;
			breakable = false;
		} else if (type == "corner") {
			hitpoints = 1;
			breakable = false;
		}
	}


	
}
