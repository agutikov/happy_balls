package com.smhv.happy_balls.model;

import java.util.HashSet;
import java.util.Set;



public abstract class WorldObject {

	private String name;
	private boolean visible;

	private boolean passThrough;
	
	private static Set<String> passableObjects = new HashSet<String>();
	private static Set<String> nonPassableObjects = new HashSet<String>();
	
	{
		passableObjects.add("ground");
		
		nonPassableObjects.add("protagonist");
		nonPassableObjects.add("enemy");
		nonPassableObjects.add("wall");
		nonPassableObjects.add("brick");
		nonPassableObjects.add("box");
		nonPassableObjects.add("corner");
	}

	public boolean isPassThrough() {
		return passThrough;		
	}
	
	
	public WorldObject(String n) {
		name = n;

		passThrough = passableObjects.contains(name);
	}

	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean v) {
		visible = v;
	}
	
	public String getName() {
		return name;
	}
	
}
