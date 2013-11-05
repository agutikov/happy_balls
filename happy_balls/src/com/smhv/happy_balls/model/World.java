package com.smhv.happy_balls.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.smhv.happy_balls.Level;
import com.smhv.happy_balls.Level.BoxDescription;
import com.smhv.happy_balls.Level.ObjectDescription;
import com.smhv.happy_balls.WorldRenderingModel;

public class World implements WorldInput {
	
	private WorldRenderingModel renderingModel;
	
	private Protagonist protagonist;	
	private ArrayList<Enemy> enemies;	
	
	//TODO: логика - дать возможность объектам самим делать что захотят и влиять на мир
	
	private int width;
	private int height;
	
	public class MapCell {
//		private FixedObject bottom;
		private FixedObject top;
		
		// enemy sets this pointer by itself during moving
		public Enemy enemy;
		
		public int x;
		public int y;
		
		public void setBomb (Bomb b) {
			top = b;
			renderingModel.setFixedTop(b.getName(), x, y, b.getOrientation());
		}
		public void setGround (GroundSquare g) {
			if (top == null || !top.isEternal()) {
//				bottom = g;
				renderingModel.setFixedBottom(g.getName(), x, y, g.getOrientation());
			}
		}
		public void setBox (Box box) {
			top = box;
			renderingModel.setFixedTop(box.getName(), x, y, box.getOrientation());
		}
		
		public boolean isPassable () {
			if (top != null)
				return top.isPassThrough();
			else 
				return true;
		}
	}
	private MapCell[][] map;
	
	
	public World(WorldRenderingModel m) {
		renderingModel = m;
		
		enemies = new ArrayList<Enemy>();
	}

	private Protagonist resetProtagonist(int x, int y) {		
		protagonist = new Protagonist(new Vector2(x, y));
		renderingModel.moveProtagonistTo(new Vector2(x, y));
		return protagonist;
	}		
	private Enemy addEnemy(int x, int y) {
		Enemy e = new Enemy(new Vector2(x, y));				
		enemies.add(e);		
		renderingModel.addEnemy(new Vector2(x, y));
		return e;
	}		
	private void rmEnemy(Enemy e) {		
		enemies.remove(e);
	}	
	private Box addBox(String name, int x, int y, FixedObject.Orientation orientation) {
		Box b = new Box(name, orientation);
		map[y][x].setBox(b);
		return b;
	}
	private void rmBox(int x, int y) {
		
	}
	private GroundSquare addGround(int x, int y){
		GroundSquare g = new GroundSquare();		
		map[y][x].setGround(g);
		return g;
	}
	private Bomb addBomb(int x, int y) {
		Bomb b = new Bomb();
		map[y][x].setBomb(b);
		return b;
	}
	private void rmBomb(int x, int y) {
		
	}
	
	public void initLevel(Level lvl) {
			width = lvl.mapWidth;
			height = lvl.mapHeight;
		
			map = new MapCell[width][height];
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					map[y][x] = new MapCell();
					map[y][x].x = x;
					map[y][x].y = y;
				}
			}
			
			renderingModel.init(width, height, lvl.objectSpriteNames);
			
			for (Entry<String, Array<BoxDescription>> entry : lvl.boxes.entrySet()) {
				for (BoxDescription desc : entry.getValue()) {						
					addBox(entry.getKey(), desc.x, desc.y, desc.orientation);						
				}
			}			
			for (ObjectDescription desc : lvl.grounds) {
				addGround(desc.x, desc.y);
			}		
			for (ObjectDescription desc : lvl.enemies) {
				addEnemy(desc.x, desc.y);
			}
			resetProtagonist(lvl.protDesc.x, lvl.protDesc.y);
	}
	
	long lastLogTime = 0;
	
	private void debug(String title, String msg) {
		long curr = TimeUtils.millis();
		if (curr - lastLogTime > 1000) {
			
			Gdx.app.debug(title, msg);	
			
			lastLogTime = curr;
		}
	}
	//TODO: refactor collision detection
	//TODO: check next position not current - to prevent collision
	//and if on next frame will be collision - decrease velocity
	//TODO проходить через один свободный квадрат
	
	private void posCorrection(FreeObject obj) {
		Vector2 pos = obj.getPos();
	
		int x = Math.round(pos.x);
		int y = Math.round(pos.y);		
		
		
		if (obj.isGoingDown()) {
			if (y > 0) {
				// bottom middle
				if (!map[y-1][x].isPassable()) {
					if (pos.y < y) 
						pos.y = y;
				}			
			}
			if (pos.x > x) {
				if (y > 0 && x < width-1) {
					// bottom right
					if (!map[y-1][x+1].isPassable()) {
						if (pos.y < y) 
							pos.y = y;
					}
				}
			} 
			if (pos.x < x) {
				if (y > 0 && x > 0) {
					// bottom left
					if (!map[y-1][x-1].isPassable()) {
						if (pos.y < y) 
							pos.y = y;
					}
				}
			}
		}
		
		if (obj.isGoingUp()) {
			if (y < height-1) {
				// top middle
				if (!map[y+1][x].isPassable()) {
					if (pos.y > y) 
						pos.y = y;
				}
			}
			if (pos.x > x) {
				if (y < height-1 && x < width-1) {
					// top right
					if (!map[y+1][x+1].isPassable()) {
						if (pos.y > y) 
							pos.y = y;
					}
				}
			} 
			if (pos.x < x) {
				if (y < height-1 && x > 0) {
					// top left
					if (!map[y+1][x-1].isPassable()) {
						if (pos.y > y) 
							pos.y = y;
					}
				}
			}
		}
		
		if (obj.isGoingLeft()) {
			if (x > 0) {
				// middle left
				if (!map[y][x-1].isPassable()) {
					if (pos.x < x) 
						pos.x = x;				
				}
			}
			if (pos.y > y) {
				if (y < height-1 && x > 0) {
					// top left
					if (!map[y+1][x-1].isPassable()) {
						if (pos.x < x) 
							pos.x = x;
					}
				}
			}
			if (pos.y < y) {
				if (y > 0 && x > 0) {
					// bottom left
					if (!map[y-1][x-1].isPassable()) {
						if (pos.x < x) 
							pos.x = x;
					}
				}
			}
		}
		
		if (obj.isGoingRight()) {
			if (x < width-1) {
				// middle right
				if (!map[y][x+1].isPassable()) {
					if (pos.x > x) 
						pos.x = x;
				}
			}
			if (pos.y > y) {
				if (y < height-1 && x < width-1) {
					// top right
					if (!map[y+1][x+1].isPassable()) {
						if (pos.x > x) 
							pos.x = x;
					}
				}
			} 
			if (pos.y < y) {
				if (y > 0 && x < width-1) {
					// bottom right
					if (!map[y-1][x+1].isPassable()) {
						if (pos.x > x) 
							pos.x = x;
					}
				}
			} 
		}
		
		

		if (pos.x < 0) pos.x = 0;
		if (pos.x > width-1) pos.x = width-1;
		if (pos.y < 0) pos.y = 0;
		if (pos.y > height-1) pos.y = height-1;
	}
	
	private boolean putBombFlag = false;
	
	private void kill() {
		protagonist.alive = false;
		renderingModel.kill();
	}
	
	private void enemyInteraction() {		
		int x = Math.round(protagonist.getPos().x);
		int y = Math.round(protagonist.getPos().y);	
		
		if (map[y][x].enemy != null) {
			kill();
		}
	}
	
	private void moveEnemies(float delta) {
		int i = 0;
		for (Enemy e : enemies) {
			int x = Math.round(e.getPos().x);
			int y = Math.round(e.getPos().y);	
			
			map[y][x].enemy = null;
			
			e.update(delta);
			//TODO: nps должен получать сигналы когда врезается куда-нибудь
			posCorrection(e);
			renderingModel.moveEnemyTo(i, e.getPos());
			i++;

			x = Math.round(e.getPos().x);
			y = Math.round(e.getPos().y);	
			
			map[y][x].enemy = e;
		}
	}
	
	public void update(float delta) {
		protagonist.update(delta);
		posCorrection(protagonist);		
		renderingModel.moveProtagonistTo(protagonist.getPos());

		moveEnemies(delta);
		enemyInteraction();
		
		processBomb();
	}
	
	private void processBomb() {
		if (putBombFlag) {			
			int x = Math.round(protagonist.getPos().x);
			int y = Math.round(protagonist.getPos().y);	
		
			addBomb(x, y);
			
			putBombFlag = false;
		}
	}

	@Override
	public void go(ControlDirection dir) {		
		FreeObject.Direction d = FreeObject.Direction.NONE;
		switch (dir) {
		case LEFT: d = FreeObject.Direction.LEFT; break;
		case RIGHT: d = FreeObject.Direction.RIGHT; break;
		case UP: d = FreeObject.Direction.UP; break;
		case DOWN: d = FreeObject.Direction.DOWN; break;
		case LEFT_UP: d = FreeObject.Direction.LEFT_UP; break;
		case LEFT_DOWN: d = FreeObject.Direction.LEFT_DOWN; break;
		case RIGHT_UP: d = FreeObject.Direction.RIGHT_UP; break;
		case RIGHT_DOWN: d = FreeObject.Direction.RIGHT_DOWN; break;
		default: break;
		}		
		protagonist.go(d);
	}


	@Override
	public void putBomb() {
		putBombFlag = true;
	}

	@Override
	public void stop() {
		protagonist.stop();		
	}
	

}
