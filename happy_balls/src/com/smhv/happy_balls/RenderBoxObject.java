package com.smhv.happy_balls;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderBoxObject {

	BoxTextureMap textureMap;
	private Animation explosionAnimation;

	float stateTime = 0;
	
	boolean exploaded = false;
	
	public RenderBoxObject(BoxTextureMap t) {
		textureMap = t;
		explosionAnimation = new Animation(0.05f, textureMap.boxRegions);
	}
	
	public void explode() {
		exploaded = true;
	}
	
	public void update(float delta) {
		if (exploaded) {
			stateTime += delta;
		}
	}
	
	public TextureRegion currentFrame() {
		return explosionAnimation.getKeyFrame(stateTime);
	}

}
