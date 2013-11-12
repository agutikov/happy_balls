package com.smhv.happy_balls.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderBombObject {

	private BombExplosionTextureMap textureMap;
	
	private int state = 0;
	
	public RenderBombObject(BombExplosionTextureMap map) {
		textureMap = map;
		
	}
	
	public void setState(int s) {
		state = s;
	}
	
	public TextureRegion currentFrame() {
		return textureMap.bombRegions[state];
	}

}
