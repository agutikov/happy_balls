package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FreeObjectTextureMap {
	public Texture texture;
	public TextureRegion[]	walkUpFrames;
	public TextureRegion[]	walkDownFrames;
	public TextureRegion[]	walkLeftFrames;
	public TextureRegion[]	walkRightFrames;
	public TextureRegion deadFrame;
	public TextureRegion standFrame;
	
	public FreeObjectTextureMap(String filename) {
		texture  = new Texture(Gdx.files.internal(filename));
		TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth()/4, texture.getHeight()/4);
		
		deadFrame = tmp[3][0];
		standFrame = tmp[1][0];
		
		walkUpFrames = new TextureRegion[4];
		walkUpFrames[0] = tmp[0][0];
		walkUpFrames[1] = tmp[0][1];
		walkUpFrames[2] = tmp[0][0];
		walkUpFrames[3] = tmp[0][2];			

		walkDownFrames = new TextureRegion[4];
		walkDownFrames[0] = tmp[1][0];
		walkDownFrames[1] = tmp[1][1];
		walkDownFrames[2] = tmp[1][0];
		walkDownFrames[3] = tmp[1][2];			

		walkLeftFrames = new TextureRegion[4];
		walkLeftFrames[0] = tmp[2][0];
		walkLeftFrames[1] = tmp[2][1];
		walkLeftFrames[2] = tmp[2][0];
		walkLeftFrames[3] = tmp[2][2];			

		walkRightFrames = new TextureRegion[4];
		walkRightFrames[0] = new TextureRegion(tmp[2][0]);
		walkRightFrames[0].flip(true, false);
		walkRightFrames[1] = new TextureRegion(tmp[2][1]);
		walkRightFrames[1].flip(true, false);
		walkRightFrames[2] = walkRightFrames[0];
		walkRightFrames[3] = new TextureRegion(tmp[2][2]);
		walkRightFrames[3].flip(true, false);	
	}
	

}
