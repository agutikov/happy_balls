package com.smhv.happy_balls.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class RenderFreeObject {

	public Color tintColor = new Color(1, 1, 1, 1);;
	public boolean tint = false;
	
	FreeObjectTextureMap textureMap;
	
	private Animation walkUpAnimation;
	private Animation walkDownAnimation;
	private Animation walkLeftAnimation;
	private Animation walkRightAnimation;
	
	public enum FreeObjectState {
		STAND, DEAD, WALK_UP, WALK_DOWN, WALK_LEFT, WALK_RIGHT
	}
	
	public void resurrection() {
		state = FreeObjectState.STAND;
		stateTime = 0;
	}
	
	public void setState (FreeObjectState st) {
		if (state != st && state != FreeObjectState.DEAD) {
			state = st;
			stateTime = 0;
		}
	}
	
	float speed = 0.05f;
	float stateTime = 0;
	Vector2 position;
	FreeObjectState state = FreeObjectState.STAND;
	
	public RenderFreeObject(FreeObjectTextureMap tm) {
		position = new Vector2(0, 0);
		textureMap = tm;
				
		walkUpAnimation = new Animation(speed, textureMap.walkUpFrames);		
		walkUpAnimation.setPlayMode(Animation.LOOP);

		walkDownAnimation = new Animation(speed, textureMap.walkDownFrames);	
		walkDownAnimation.setPlayMode(Animation.LOOP);
	
		walkLeftAnimation = new Animation(speed, textureMap.walkLeftFrames);	
		walkLeftAnimation.setPlayMode(Animation.LOOP);	

		walkRightAnimation = new Animation(speed, textureMap.walkRightFrames);
		walkRightAnimation.setPlayMode(Animation.LOOP);
	}
	
	public TextureRegion currentFrame() {
		switch (state) {
		case STAND:
			return textureMap.standFrame;
		case DEAD:
			return textureMap.deadFrame;
		case WALK_UP:
			return walkUpAnimation.getKeyFrame(stateTime);
		case WALK_DOWN:
			return walkDownAnimation.getKeyFrame(stateTime);
		case WALK_LEFT:
			return walkLeftAnimation.getKeyFrame(stateTime);
		case WALK_RIGHT:
			return walkRightAnimation.getKeyFrame(stateTime);			
		default:
			return textureMap.standFrame;					
		}		
	}
	
	public void update (float delta) {
		if (state != FreeObjectState.DEAD) {
			stateTime += delta;
		}
	}	
	
	public void kill() {
		state = FreeObjectState.DEAD;
	}
	
	public void setPos(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public Vector2 getPos() {
		return position;
	}
}




