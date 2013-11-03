package com.smhv.happy_balls.model;


public interface WorldInput {
	
	public enum ControlDirection {
		LEFT, RIGHT, UP, DOWN,
		LEFT_UP, RIGHT_UP,
		LEFT_DOWN, RIGHT_DOWN
	}
	
	/*
	 * Reset current direction immediately.
	 */
	public void go(ControlDirection dir);	
	
	/*
	 * Stop moving.
	 */
	public void stop();
	
	public void putBomb();
}
