package com.smhv.happy_balls.render;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BombExplosionTextureMap {
	public Texture texture;
	public TextureRegion[]	bombRegions;
	public Map<WorldRenderingModel.ExplosionPart, Sprite> explosionSprites;

	public BombExplosionTextureMap(String filename) {		
		
		explosionSprites = new EnumMap<WorldRenderingModel.ExplosionPart, Sprite> 
													(WorldRenderingModel.ExplosionPart.class);
		
		texture  = new Texture(Gdx.files.internal(filename));
		TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth()/8, texture.getHeight());
	
		bombRegions = new TextureRegion[3];
		
		bombRegions[0] = tmp[0][0];
		bombRegions[1] = tmp[0][1];
		bombRegions[2] = tmp[0][2];
		
		explosionSprites.put(WorldRenderingModel.ExplosionPart.CENTER, new Sprite(tmp[0][3]));
		explosionSprites.put(WorldRenderingModel.ExplosionPart.PASS_H, new Sprite(tmp[0][4]));
		explosionSprites.put(WorldRenderingModel.ExplosionPart.END_R, new Sprite(tmp[0][5]));
		
		explosionSprites.put(WorldRenderingModel.ExplosionPart.PASS_V, new Sprite(tmp[0][4]));
		explosionSprites.put(WorldRenderingModel.ExplosionPart.END_L, new Sprite(tmp[0][5]));
		explosionSprites.put(WorldRenderingModel.ExplosionPart.END_U, new Sprite(tmp[0][5]));
		explosionSprites.put(WorldRenderingModel.ExplosionPart.END_D, new Sprite(tmp[0][5]));
		
		explosionSprites.get(WorldRenderingModel.ExplosionPart.PASS_V).rotate90(true);
		
		explosionSprites.get(WorldRenderingModel.ExplosionPart.END_D).rotate90(true);
		
		explosionSprites.get(WorldRenderingModel.ExplosionPart.END_L).flip(true, false);
		
		explosionSprites.get(WorldRenderingModel.ExplosionPart.END_U).rotate90(false);
	}

}
