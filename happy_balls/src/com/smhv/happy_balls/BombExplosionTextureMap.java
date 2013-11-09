package com.smhv.happy_balls;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BombExplosionTextureMap {
	public Texture texture;
	public TextureRegion[]	bombRegions;
	public Map<WorldRenderingModel.ExplosionPart, TextureRegion> explosionRegions;

	public BombExplosionTextureMap(String filename) {		
		
		explosionRegions = new EnumMap<WorldRenderingModel.ExplosionPart, TextureRegion> 
													(WorldRenderingModel.ExplosionPart.class);
		
		texture  = new Texture(Gdx.files.internal(filename));
		TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth()/6, texture.getHeight());
	
		bombRegions = new TextureRegion[3];
		
		bombRegions[0] = tmp[0][0];
		bombRegions[1] = tmp[0][1];
		bombRegions[2] = tmp[0][2];
		
		
	}

}
