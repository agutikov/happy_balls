package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {


	public enum State {
		NONE, WALKING, DEAD
	}
	public static enum Direction {
		LEFT, RIGHT, UP, DOWN, NONE
	}

	public static final float SPEED = 8f;	
	public static final float SIZE = 1f;
	
	
	Vector2 	position = new Vector2();
	Vector2 	velocity = new Vector2();
	Rectangle 	bounds = new Rectangle();
	State		state = State.NONE;
	boolean		facingLeft = true;


	boolean		goToDestination = false;
	Vector2 	destination = new Vector2();
	
	public void setDest(Vector2 pos) {
		destination = pos;
		Gdx.app.log("position", position.x + ", " + position.y);
		Gdx.app.log("destination", destination.x + ", " + destination.y);
		Vector2 tmpDest = new Vector2(destination);
		Vector2 direction = tmpDest.sub(position);
		Gdx.app.log("direction", direction.x + ", " + direction.y);
		velocity = direction.nor().scl(SPEED);
		Gdx.app.log("velocity", velocity.x + ", " + velocity.y);
		goToDestination = true;
	}
	public void clearDest() {
		goToDestination = false;
	}
	
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
	
	public void go(int x, int y) {

		clearDest();
		
		if (x == 1)
			velocity.x = SPEED;
		else if (x == -1)
			velocity.x = -SPEED;

		if (y == 1)
			velocity.y = SPEED;
		else if (y == -1)
			velocity.y = -SPEED;
		
		velocity = velocity.nor().scl(SPEED);

	}

	public void stop() {
		stop(true, true);
	}
	
	public void stop(boolean x, boolean y) {
		if (x)
			velocity.x = 0;
		if (y)
			velocity.y = 0;
	}
	
	public void update(float delta) {
		if (goToDestination) {
			if (position.dst(destination) < 0.1f) {				
				stop();
				clearDest();				
			} else {
				Vector2 tmpDest = new Vector2(destination);
				Vector2 direction = tmpDest.sub(position);
				velocity = direction.nor().scl(SPEED);
			}
		}
		Vector2 vel = new Vector2(velocity);
		position.add(vel.scl(delta));	
	}
}
