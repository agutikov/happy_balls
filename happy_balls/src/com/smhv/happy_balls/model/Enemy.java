package com.smhv.happy_balls.model;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends FreeObject {


	private static Random random = new Random(); // seed ?
	
	/*
	 * Идея в том чтобы эта штука как-бы управляла сама собой.
	 */
	
	public Enemy(Vector2 pos) {
		super("enemy", pos);
		goingTime = random.nextFloat() * 20;
		speed = random.nextFloat() * 5f;
	}
	
	private float goingTime = 1.0f;
	private float timeBeforeRotate = 0;
	
	@Override
	public void ballsToTheWall() {
		timeBeforeRotate = 0;
	}
	
	public void update(float delta) {
		if (timeBeforeRotate <= 0f) {
			timeBeforeRotate = goingTime;
			
			int dir = random.nextInt(5);
			
			switch (dir) {
			case 0: this.setDirection(Direction.NONE); break;
			case 1: this.setDirection(Direction.UP); break;
	//		case 2: this.setDirection(Direction.RIGHT_UP); break;
			case 2: this.setDirection(Direction.RIGHT); break;
	//		case 4: this.setDirection(Direction.RIGHT_DOWN); break;
			case 3: this.setDirection(Direction.DOWN); break;
	//		case 6: this.setDirection(Direction.LEFT_DOWN); break;
			case 4: this.setDirection(Direction.LEFT); break;
	//		case 8: this.setDirection(Direction.LEFT_UP); break;
			default:  break;
			}
			
		} else {
			timeBeforeRotate -= delta;
		}
		
		super.update(delta);
	}

	
}
