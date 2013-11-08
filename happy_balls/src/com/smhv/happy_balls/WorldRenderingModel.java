package com.smhv.happy_balls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.smhv.happy_balls.RenderFreeObject.FreeObjectState;
import com.smhv.happy_balls.model.FixedObject;
import com.smhv.happy_balls.model.FreeObject.Direction;


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
	}
	
	public void setFixedTop(String objName, int x, int y, FixedObject.Orientation orient) {
		renderingMap[y][x].top = new RenderObject(
				textureGlobalRegions.get(textureMap.get(objName)),
				or2deg(orient));
	}
	public void setFixedBottom(String objName, int x, int y, FixedObject.Orientation orient) {
		renderingMap[y][x].bottom = new RenderObject(
				textureGlobalRegions.get(textureMap.get(objName)),
				or2deg(orient));
	}
	public void rmFixedTop(int x, int y) {
		renderingMap[y][x].top = null;
	}
	
	//TODO: перейти на использование спрайтов
	//TODO: SpriteCache

	public FreeObjectTextureMap protagonistTextureMap;
	public FreeObjectTextureMap enemyTextureMap;
	
	
	public RenderFreeObject protagonistRenderObject;		
	public ArrayList<RenderFreeObject> enemies;
	
	public void kill() {
		protagonistRenderObject.kill();
	}
	
	
	public void resurrection() {
		protagonistRenderObject.resurrection();
	}
	
	//TODO: может объединить все эти енумы-направления?
	public void setProtagonistDirection (Direction dir) {
		switch (dir) {
		case NONE: 		protagonistRenderObject.setState(FreeObjectState.STAND); 		break;
		case LEFT: 		protagonistRenderObject.setState(FreeObjectState.WALK_LEFT); 	break;
		case RIGHT: 	protagonistRenderObject.setState(FreeObjectState.WALK_RIGHT); 	break;
		case UP: 		protagonistRenderObject.setState(FreeObjectState.WALK_UP); 		break;
		case DOWN: 		protagonistRenderObject.setState(FreeObjectState.WALK_DOWN); 	break;
		default:
			break;
		}
	}
	
	public void update (float delta) {
		protagonistRenderObject.update(delta);
		for (RenderFreeObject enemy : enemies) {
			enemy.update(delta);
		}
	}
	
	// world coordinates
	public void moveProtagonistTo (Vector2 pos) {	
		
		renderer.SetCamera(pos.x, pos.y);

		protagonistRenderObject.setPos(pos);
	}
	
	public void tint (int x, int y, boolean t) {
		renderingMap[y][x].tinted = t;
	}
	
	// world coordinates
	public void addEnemy (Vector2 pos) {
		enemies.add(new RenderFreeObject(enemyTextureMap));
	}
	
	public void rmEnemy (int i) {
		enemies.remove(i);
	}
	
	// world coordinates
	public void moveEnemyTo (int i, Vector2 pos) {
		enemies.get(i).setPos(pos);
	}
	
	public void setEnemyDirection (int i, Direction dir) {
		switch (dir) {
		case NONE: 		enemies.get(i).setState(FreeObjectState.STAND); 		break;
		case LEFT: 		enemies.get(i).setState(FreeObjectState.WALK_LEFT); 	break;
		case RIGHT: 	enemies.get(i).setState(FreeObjectState.WALK_RIGHT); 	break;
		case UP: 		enemies.get(i).setState(FreeObjectState.WALK_UP); 		break;
		case DOWN: 		enemies.get(i).setState(FreeObjectState.WALK_DOWN); 	break;
		default:
			break;
		}
	}
	
	public WorldRenderingModel() {				
		enemies = new ArrayList<RenderFreeObject>();
	}
	
	
	Texture textureGlobal;
	public  Map<String, GameTexture> textureGlobalRegions = new HashMap<String, GameTexture>();

	
	/*
	 * TODO: add texture's world size - get it from model object size
	 * it is not necessary while we have all objects the same size
	 */
	
	public void loadTextures() {
		textureGlobal  = new Texture(Gdx.files.internal("graphics/Map_32.png"));
		TextureRegion tmp[][] = TextureRegion.split(textureGlobal, textureGlobal.getWidth() / 8, textureGlobal.getHeight());
				
		textureGlobalRegions.put("player", new GameTexture(tmp[0][0], false));
		textureGlobalRegions.put("enemy", new GameTexture(tmp[0][1], false));
		textureGlobalRegions.put("bomb", new GameTexture(tmp[0][2], false));
		textureGlobalRegions.put("box", new GameTexture(tmp[0][3], true));
		textureGlobalRegions.put("floor", new GameTexture(tmp[0][4], true));
		textureGlobalRegions.put("brick", new GameTexture(tmp[0][5], true));
		textureGlobalRegions.put("wall", new GameTexture(tmp[0][6], true));
		textureGlobalRegions.put("corner", new GameTexture(tmp[0][7], true));
		

		protagonistTextureMap = new FreeObjectTextureMap("graphics/player_sprite.png");
		protagonistRenderObject = new RenderFreeObject(protagonistTextureMap);
	
		enemyTextureMap = new FreeObjectTextureMap("graphics/Enemy_sprite.png");
		
		
	}
}
