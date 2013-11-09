package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BoxTextureMap {
	public Texture texture;
	public TextureRegion[]	boxRegions;

	public BoxTextureMap(String filename) {		
		texture  = new Texture(Gdx.files.internal(filename));
		TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth()/4, texture.getHeight());
	
		boxRegions = new TextureRegion[4];
		
		boxRegions[0] = tmp[0][0];
		boxRegions[1] = tmp[0][1];
		boxRegions[2] = tmp[0][2];
		boxRegions[3] = tmp[0][3];		
	}
	
	public TextureRegion getFrame (int i) {
		return boxRegions[i];
	}

}
