package com.smhv.happy_balls.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class FreeObject extends WorldObject {
	
	public enum Direction {
		NONE, 
		LEFT, RIGHT, UP, DOWN,
		LEFT_UP, RIGHT_UP,
		LEFT_DOWN, RIGHT_DOWN
	}

	private static float sqrt2 = (float) (Math.sqrt(2)/2);
	
	protected float speed = 1f;	
	
	public abstract void ballsToTheWall();

	protected boolean alive = true;
	
	public void kill() {
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	protected Vector2 	position = new Vector2();
	
	Direction currDirection = Direction.NONE;
	protected Vector2 	velocity = new Vector2();
	
	public Vector2 getPos() {
		return position;
	}
	
	public FreeObject(String name, Vector2 pos) {
		super(name);
		position = pos;
	}
	
	public boolean isGoingUp() {
		return velocity.y > 0;
	}
	public boolean isGoingDown() {
		return velocity.y < 0;
	}
	public boolean isGoingLeft() {
		return velocity.x < 0;	
	}
	public boolean isGoingRight() {
		return velocity.x > 0;
	}

	public Direction getDirection() {
		return currDirection;
	}
	
	public void setDirection(Direction d) {
		if (d != currDirection) {
			switch (d) {
			case NONE: 			velocity.x = 0; 			velocity.y = 0; break;
			case LEFT: 			velocity.x = -speed; 		velocity.y = 0; break;
			case RIGHT: 		velocity.x = speed; 		velocity.y = 0; break;
			case UP: 			velocity.x = 0; 			velocity.y = speed; break;
			case DOWN: 			velocity.x = 0; 			velocity.y = -speed; break;
			case LEFT_UP: 		velocity.x = -speed*sqrt2; 	velocity.y = speed*sqrt2; break;
			case RIGHT_UP: 		velocity.x = speed*sqrt2; 	velocity.y = speed*sqrt2; break;
			case LEFT_DOWN: 	velocity.x = -speed*sqrt2; 	velocity.y = -speed*sqrt2; break;
			case RIGHT_DOWN: 	velocity.x = speed*sqrt2; 	velocity.y = -speed*sqrt2; break;
			default:
				break;
			}
			currDirection = d;
		}
	}
	
//	long lastLogTime = 0;
	
	public void update(float delta) {
		
//		long curr = TimeUtils.millis();
//		if (curr - lastLogTime > 1000) {
			
//			Gdx.app.debug("FreeObject.update", position.x + ", " + position.y + "; " 
//					+ velocity.x + ", " + velocity.y);	
			
//			lastLogTime = curr;
//		}
		
		if (alive) {
			Vector2 tmp = new Vector2(velocity);
			tmp.scl(delta);
			position.add(tmp); 
		}
	}

}
