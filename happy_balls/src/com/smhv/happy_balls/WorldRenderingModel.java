package com.smhv.happy_balls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
		boolean explosion;
		ExplosionPart part;
		
		public RenderObject bottom;
		public RenderObject top;
		
		public RenderBombObject bomb;
		public RenderBoxObject box;
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
	
	ArrayList <RenderBoxObject> boxes = new ArrayList <RenderBoxObject> ();
	ArrayList <RenderBoxObject> exploadedBoxes = new ArrayList <RenderBoxObject> ();
	
	public void setFixedTop(String objName, int x, int y, FixedObject.Orientation orient) {
		if (objName == "bomb") {
			renderingMap[y][x].bomb = new RenderBombObject(bombExplosionTextureMap);
		} else if (objName == "box") {
			renderingMap[y][x].box = new RenderBoxObject(boxTextureMap);
			boxes.add(renderingMap[y][x].box);
		} else {
			renderingMap[y][x].top = new RenderObject(
					textureGlobalRegions.get(textureMap.get(objName)),
					or2deg(orient));
		}
	}
	public void setFixedBottom(String objName, int x, int y, FixedObject.Orientation orient) {
		renderingMap[y][x].bottom = new RenderObject(
				textureGlobalRegions.get(textureMap.get(objName)),
				or2deg(orient));
	}
	public void rmFixedTop(int x, int y) {
		if (renderingMap[y][x].top != null)
			renderingMap[y][x].top = null;
		if (renderingMap[y][x].box != null) {
			exploadedBoxes.remove(renderingMap[y][x].box);
			renderingMap[y][x].box = null;
		}
		if (renderingMap[y][x].bomb != null) {
			renderingMap[y][x].bomb = null;
		}
	}
	
	public void highlightProtagonist () {
		protagonistRenderObject.tintColor = new Color(1, 1, 1, 0.5f);
		protagonistRenderObject.tint = true;
	}
	public void unhighlightProtagonist () {
		protagonistRenderObject.tint = false;
	}
		
	public void explodeBox (int x, int y) {
		boxes.remove(renderingMap[y][x].box);
		exploadedBoxes.add(renderingMap[y][x].box);
		renderingMap[y][x].box.explode();
	}
	//TODO: перейти на использование спрайтов
	//TODO: SpriteCache

	public FreeObjectTextureMap protagonistTextureMap;
	public FreeObjectTextureMap enemyTextureMap;
	public BoxTextureMap boxTextureMap;
	public BombExplosionTextureMap bombExplosionTextureMap;
	
	
	public RenderFreeObject protagonistRenderObject;		
	public Map<Integer, RenderFreeObject> enemies;
	
	public void kill() {
		protagonistRenderObject.kill();
	}
	
	public void setBombState(int state, int x, int y) {
		renderingMap[y][x].bomb.setState(state);
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
		for (Entry<Integer, RenderFreeObject> entry : enemies.entrySet()) {
			entry.getValue().update(delta);
		}
		for (RenderBoxObject box : exploadedBoxes) {
			box.update(delta);
		}
	}
	
	// world coordinates
	public void moveProtagonistTo (float x, float y) {	
		
		renderer.SetCamera(x, y);

		protagonistRenderObject.setPos(x, y);
	}
	
	public enum ExplosionPart {
		CENTER,
		PASS_H,
		PASS_V,
		END_L,
		END_R,
		END_U,
		END_D
	}
	
	public void setExplosion (int x, int y, ExplosionPart part) {
		renderingMap[y][x].explosion = true;
		renderingMap[y][x].part = part;
	}
	
	public void rmExplosion (int x, int y) {
		renderingMap[y][x].explosion = false;
	}
	
	// world coordinates
	public void addEnemy (int index, Vector2 pos) {
		enemies.put(index, new RenderFreeObject(enemyTextureMap));
	}
	
	public void rmEnemy (int i) {
		enemies.remove(i);
	}
	
	// world coordinates
	public void moveEnemyTo (int i, Vector2 pos) {
		enemies.get(i).setPos(pos.x, pos.y);
	}
	
	public void killEnemy(int i) {
		enemies.get(i).setState(FreeObjectState.DEAD);
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
		enemies = new HashMap<Integer, RenderFreeObject>();
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
	
		enemyTextureMap = new FreeObjectTextureMap("graphics/enemy_sprite.png");
		
		boxTextureMap = new BoxTextureMap("graphics/box_sprite.png");		
		
		bombExplosionTextureMap = new BombExplosionTextureMap("graphics/bomb_sprite.png");
	}
}
