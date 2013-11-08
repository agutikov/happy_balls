package com.smhv.happy_balls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.smhv.happy_balls.model.FixedObject;

public class WorldRenderingModel {

	public WorldRenderer renderer;
		
	/*
	 * Convert orientation to degrees.
	 */
	public float or2deg (FixedObject.Orientation orient) {
		switch (orient) {
		case DEFAULT: return 0;
		case UP: return 0;
		case RIGHT: return 90;
		case DOWN: return 180;
		case LEFT: return 270;
		default: return 0;
		}
	}
	
	public class GameTexture {		
		public TextureRegion textureRegion;		
		public boolean isFullHover;
		
		public GameTexture(TextureRegion t, boolean hover) {
			textureRegion = t;
			isFullHover = hover;
		}		
	}
	
	public class RenderObject {

		public float rot;
		public GameTexture texture;
		
		RenderObject (GameTexture t, float r) {
			texture = t;
			rot = r;
		}
	}
	
	public class RenderingCell {
		public boolean tinted = false;
		public RenderObject bottom;
		public RenderObject top;
	}
	
	private Map<String, String> textureMap;
	
	public RenderingCell[][] renderingMap;
	
	public void init(int width, int height, Map<String, String> objTextureMap) {
		renderingMap = new RenderingCell[height][width];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				renderingMap[y][x] = new RenderingCell();
			}
		}
		
		textureMap = objTextureMap;
		
		enemyTexture = textureRegions.get("enemy");
		
		protagonistTexture = new RenderObject(textureRegions.get("player"), 0);
	}
	
	public void setFixedTop(String objName, int x, int y, FixedObject.Orientation orient) {
		renderingMap[y][x].top = new RenderObject(
				textureRegions.get(textureMap.get(objName)),
				or2deg(orient));
	}
	public void setFixedBottom(String objName, int x, int y, FixedObject.Orientation orient) {
		renderingMap[y][x].bottom = new RenderObject(
				textureRegions.get(textureMap.get(objName)),
				or2deg(orient));
	}
	public void rmFixedTop(int x, int y) {
		renderingMap[y][x].top = null;
	}
	
	//TODO: перейти на использование спрайтов
	//TODO: SpriteCache
	
	public GameTexture enemyTexture;
	public RenderObject protagonistTexture;
	
	public Vector2 protogonistPosition;
	public ArrayList<Vector2> enemiesPositions;
	
	public void kill() {
		protagonistTexture.rot = 90;
	}
	
	// world coordinates
	public void moveProtagonistTo (Vector2 pos) {	
		
		renderer.SetCamera(pos.x, pos.y);

		protogonistPosition  = pos;
	}
	
	public void tint (int x, int y, boolean t) {
		renderingMap[y][x].tinted = t;
	}
	
	// world coordinates
	public void addEnemy (Vector2 pos) {
		enemiesPositions.add(pos);
	}
	
	public void rmEnemy (int i) {
		enemiesPositions.remove(i);
	}
	
	// world coordinates
	public void moveEnemyTo (int i, Vector2 pos) {
		enemiesPositions.set(i, pos);
	}
	
	public WorldRenderingModel() {				
		enemiesPositions = new ArrayList<Vector2>();
	}
	
	Texture texture;
	public  Map<String, GameTexture> textureRegions = new HashMap<String, GameTexture>();

	
	/*
	 * TODO: add texture's world size - get it from model object size
	 * it is not necessary while we have all objects the same size
	 */
	
	public void loadTextures() {
		texture  = new Texture(Gdx.files.internal("graphics/Map_32.png"));
		TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight());
				
		textureRegions.put("player", new GameTexture(tmp[0][0], false));
		textureRegions.put("enemy", new GameTexture(tmp[0][1], false));
		textureRegions.put("bomb", new GameTexture(tmp[0][2], false));
		textureRegions.put("box", new GameTexture(tmp[0][3], true));
		textureRegions.put("floor", new GameTexture(tmp[0][4], true));
		textureRegions.put("brick", new GameTexture(tmp[0][5], true));
		textureRegions.put("wall", new GameTexture(tmp[0][6], true));
		textureRegions.put("corner", new GameTexture(tmp[0][7], true));
	}
}
