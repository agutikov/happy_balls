package com.smhv.happy_balls.model;

public abstract class FixedObject extends WorldObject {
	
	public enum Orientation {
		LEFT,
		DOWN,
		RIGHT,
		UP,
		DEFAULT
	}
	
	private Orientation orientation;
	
	public FixedObject(String name, Orientation orient) {
		super(name);
		orientation = orient;
	}

	public abstract boolean isEternal();
	
	
	public Orientation getOrientation() {
		return orientation;
	}
	
}
