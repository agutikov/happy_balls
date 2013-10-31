package com.smhv.happy_balls;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {


	public enum State {
		NONE, WALKING, DEAD
	}
	public static enum Direction {
		LEFT, RIGHT, UP, DOWN, NONE
	}

	public static final float SPEED = 2f;	
	public static final float SIZE = 1f;
	
	
	Vector2 	position = new Vector2();
	Vector2 	velocity = new Vector2();
	Rectangle 	bounds = new Rectangle();
	State		state = State.NONE;
	boolean		facingLeft = true;


	Vector2 	destination = new Vector2();
	
	public Player(Vector2 position) {
		
		this.position = position;
		this.destination = this.position;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void update(float delta) {
		
		position.add(velocity.scl(delta));
	}
}
