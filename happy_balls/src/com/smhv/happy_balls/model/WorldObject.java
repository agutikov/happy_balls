package com.smhv.happy_balls.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;




public abstract class WorldObject {

	private String name;
	private boolean visible;

	private boolean passThrough;
	
	private static Map<String, Boolean> passableObjects = new HashMap<String, Boolean>();
	
	{
		passableObjects.put("ground", true);
		
		passableObjects.put("protagonist", false);
		passableObjects.put("enemy", false);
		passableObjects.put("wall", false);
		passableObjects.put("brick", false);
		passableObjects.put("box", false);
		passableObjects.put("corner", false);
		passableObjects.put("bomb", false);
	}

	public boolean isPassThrough() {
		return passThrough;		
	}
	
	
	public WorldObject(String n) {
		name = n;

		passThrough = passableObjects.get(name);
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
