package com.smhv.happy_balls.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.smhv.happy_balls.Level;
import com.smhv.happy_balls.SoundPlayer;
import com.smhv.happy_balls.Level.BoxDescription;
import com.smhv.happy_balls.Level.ObjectDescription;
import com.smhv.happy_balls.WorldRenderingModel;
import com.smhv.happy_balls.WorldRenderingModel.ExplosionPart;

//TODO: пользоваться полиморфизмом - чтобы пихать объекты в списки

public class World implements WorldInput {
	
	SoundPlayer player;
	private WorldRenderingModel renderingModel;
	
	private Protagonist protagonist;	
	private ArrayList<Enemy> enemies;	
	
	Position respawn;
	private float respawnTime = 3f;
	private float respawnTimeLeft;
	
	public class Position {
		public int x;
		public int y;
		public Position (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	//TODO: механизм создания, удаления и поиска объектов мира вне зависимости от типа
	
	//TODO: общий тип "событие-воздействие" в мире, которому в вызываемый метод передается ссылка на мир
	
	private Map <Bomb, World.Position> bombs;
	
	// TODO: Blow должен создаваться фабрикой членом Bomb (это логично и параметры можно передать)
	public class Explosion {

		public ArrayList<Enemy> killedEnemies;
		
		private int x;
		private int y;
		
		private float timeLeft;
		
		public Explosion(int x, int y) {
			this.x = x;
			this.y = y;
			timeLeft = 0.3f;
			
			killedEnemies = new ArrayList<Enemy>();
		}
		
		//TODO: разные модели взрыва - ограничение по расстоянию или по площади, направление распространения (за угол)
		public void perform(World w) {			
			w.map[y][x].explode(this);		
			if (y > 0)
				w.map[y - 1][x].explode(this);
			if (y < w.height)
				w.map[y + 1][x].explode(this);
			if (x > 0)
				w.map[y][x - 1].explode(this);
			if (x < w.width)
				w.map[y][x + 1].explode(this);				
		}
		
		public void cleanup(World w) {			
			w.map[y][x].unexplode();		
			if (y > 0)
				w.map[y - 1][x].unexplode();
			if (y < w.height)
				w.map[y + 1][x].unexplode();
			if (x > 0)
				w.map[y][x - 1].unexplode();
			if (x < w.width)
				w.map[y][x + 1].unexplode();	
		}
		
		//TODO: общий тип временных объектов
		
		public boolean update(float delta) {
			timeLeft -= delta;
			
			return timeLeft <= 0;		
		}

	}
	
	private ArrayList<Explosion> explosions;
	
	//TODO: логика - дать возможность объектам самим делать что захотят и влиять на мир
	
	private int width;
	private int height;
	
	public class MapCell {
//		private FixedObject bottom;
		private Bomb bomb;
		private Box box;
		
		// enemy sets this pointer by itself during moving
		public ArrayList<Enemy> enemies;
		
		public int x;
		public int y;
		
		public boolean isExploading = false;
		
		public MapCell() {
			enemies = new ArrayList<Enemy>();
		}
		
		public void setBomb (Bomb b) {
			bomb = b;
			renderingModel.setFixedTop(b.getName(), x, y, b.getOrientation());
		}
		public void setGround (GroundSquare g) {
			if (box == null || !box.isEternal()) {
//				bottom = g;
				renderingModel.setFixedBottom(g.getName(), x, y, g.getOrientation());
			}
		}
		public void setBox (Box box) {
			this.box = box;
			renderingModel.setFixedTop(box.getName(), x, y, box.getOrientation());
		}
		public void rmBomb () {
			bomb = null;
			renderingModel.rmFixedTop(x, y);
		}
		public void rmBox () {
			box = null;
			renderingModel.rmFixedTop(x, y);
		}
		
		public boolean isPassable () {
			if (box != null)
				return box.isPassThrough();
			else if (bomb != null)
				return bomb.isPassThrough(); 
			//TODO: как нормально обработать ситуацию, когда стоишь на бомбе (она же непроходимая)
			else
				return true;
		}
		
		public void explode(Explosion explosion) {
			if (box != null && box.isEternal()) {
				
			} else {
				if (box != null && !box.isEternal()) {
					rmBox();
				}
				if (bomb != null) {
					bomb.detonate();
				}
				
				for (Enemy e : enemies) {
					player.playMurder();
					e.kill();				
					int index = enemies.indexOf(e);	
					renderingModel.killEnemy(index);					
					explosion.killedEnemies.add(e);
				}
				
				renderingModel.setExplosion(x, y, ExplosionPart.CENTER);
				isExploading = true;
			}
		}
		
		public void unexplode() {
			renderingModel.rmExplosion(x, y);	
			isExploading = false;		
		}
	}
	private MapCell[][] map;
	
	
	public World(WorldRenderingModel m, SoundPlayer sp) {
		renderingModel = m;
		
		enemies = new ArrayList<Enemy>();
		bombs = new HashMap<Bomb, Position>();
		explosions = new ArrayList<Explosion>();
		
		player = sp;
	}

	private Protagonist resetProtagonist(int x, int y) {		
		protagonist = new Protagonist(new Vector2(x, y));
		renderingModel.moveProtagonistTo(new Vector2(x, y));
		renderingModel.resurrection();
		return protagonist;
	}		
	private Enemy addEnemy(int x, int y) {
		Enemy e = new Enemy(new Vector2(x, y));				
		enemies.add(e);		
		renderingModel.addEnemy(new Vector2(x, y));
		return e;
	}		
	private void rmEnemy(Enemy e) {		
		int index = enemies.indexOf(e);
		enemies.remove(e);
		renderingModel.rmEnemy(index);
	}	
	private Box addBox(String name, int x, int y, FixedObject.Orientation orientation) {
		Box b = new Box(name, orientation);
		map[y][x].setBox(b);
		return b;
	}
	private GroundSquare addGround(int x, int y){
		GroundSquare g = new GroundSquare();		
		map[y][x].setGround(g);
		return g;
	}
	private void addBomb(int x, int y) {
		if (map[y][x].bomb == null) {
			
			Gdx.app.debug("bomb", x + ", " + y);
			
			player.playSetBomb();
			Bomb b = new Bomb();
			map[y][x].setBomb(b);
			bombs.put(b, new Position(x, y));
		}
	}
	private void rmBomb(Bomb b) {
		Position pos = bombs.get(b);
		map[pos.y][pos.x].rmBomb();
		bombs.remove(b);
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
			respawn = new Position(lvl.protDesc.x, lvl.protDesc.y);
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
		
		float passLimit = 0.25f;
		
		if (obj.isGoingDown()) {
			if (y > 0) {
				// bottom middle
				if (!map[y-1][x].isPassable()) {
					if (pos.y < y) {
						pos.y = y;
						obj.ballsToTheWall();
					}
				} else {
					if (pos.x > x && (pos.x - x) > passLimit) {
						if (y > 0 && x < width-1) {
							// bottom right
							if (!map[y-1][x+1].isPassable()) {
								if (pos.y < y) {
									pos.y = y;
								}
							}
						}
					} 
					if (pos.x < x && (x - pos.x) > passLimit) {
						if (y > 0 && x > 0) {
							// bottom left
							if (!map[y-1][x-1].isPassable()) {
								if (pos.y < y) {
									pos.y = y;	
								}
							}
						}
					}
				}
			}
		}
		
		if (obj.isGoingUp()) {
			if (y < height-1) {
				// top middle
				if (!map[y+1][x].isPassable()) {
					if (pos.y > y) {
						pos.y = y;
						obj.ballsToTheWall();
					}
				} else {
					if (pos.x > x && (pos.x - x) > passLimit) {
						if (y < height-1 && x < width-1) {
							// top right
							if (!map[y+1][x+1].isPassable()) {
								if (pos.y > y) {
									pos.y = y;
								}
							}
						}
					} 
					if (pos.x < x && (x - pos.x) > passLimit) {
						if (y < height-1 && x > 0) {
							// top left
							if (!map[y+1][x-1].isPassable()) {
								if (pos.y > y) {
									pos.y = y;
								}
							}
						}
					}
				}
			}
		}
		
		if (obj.isGoingLeft()) {
			if (x > 0) {
				// middle left
				if (!map[y][x-1].isPassable()) {
					if (pos.x < x) {
						pos.x = x;	
						obj.ballsToTheWall();
					}
				} else {
					if (pos.y > y && (pos.y - y) > passLimit) {
						if (y < height-1 && x > 0) {
							// top left
							if (!map[y+1][x-1].isPassable()) {
								if (pos.x < x) {
									pos.x = x;
								}
							}
						}
					}
					if (pos.y < y && (y - pos.y) > passLimit) {
						if (y > 0 && x > 0) {
							// bottom left
							if (!map[y-1][x-1].isPassable()) {
								if (pos.x < x) {
									pos.x = x;
								}
							}
						}
					}
				}
			}
		}
		
		if (obj.isGoingRight()) {
			if (x < width-1) {
				// middle right
				if (!map[y][x+1].isPassable()) {
					if (pos.x > x) {
						pos.x = x; 
						obj.ballsToTheWall();
					}
				} else {
					if (pos.y > y && (pos.y - y) > passLimit) {
						if (y < height-1 && x < width-1) {
							// top right
							if (!map[y+1][x+1].isPassable()) {
								if (pos.x > x) {
									pos.x = x;
								}
							}
						}
					} 
					if (pos.y < y && (y - pos.y) > passLimit) {
						if (y > 0 && x < width-1) {
							// bottom right
							if (!map[y-1][x+1].isPassable()) {
								if (pos.x > x) {
									pos.x = x;
								}
							}
						}
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
		if (protagonist.alive) {
			player.playDeath();
			protagonist.kill();
			renderingModel.kill();	
			respawnTimeLeft = respawnTime;
		}
	}
	
	private void enemyInteraction() {		
		int x = Math.round(protagonist.getPos().x);
		int y = Math.round(protagonist.getPos().y);	
		
		for (Enemy e : map[y][x].enemies) {
			if (e.isAlive()) {
				kill();
				break;
			}
		}
	}
	
	private void moveEnemies(float delta) {
		int i = 0;
		for (Enemy e : enemies) {
			int x = Math.round(e.getPos().x);
			int y = Math.round(e.getPos().y);	
			
			map[y][x].enemies.remove(e);
			
			e.update(delta);
			//TODO: NPC должен получать сигналы когда врезается куда-нибудь
			posCorrection(e);
			renderingModel.moveEnemyTo(i, e.getPos());
			renderingModel.setEnemyDirection(i, e.getDirection());
			i++;

			x = Math.round(e.getPos().x);
			y = Math.round(e.getPos().y);	
			
			map[y][x].enemies.add(e);
		}
	}
	
	public void update(float delta) {
		protagonist.update(delta);
		posCorrection(protagonist);		
		{
			int x = Math.round(protagonist.getPos().x);
			int y = Math.round(protagonist.getPos().y);	
			
			if (map[y][x].isExploading) {
				kill();
			}
		}		
		renderingModel.moveProtagonistTo(protagonist.getPos());
		renderingModel.setProtagonistDirection(protagonist.getDirection());

		moveEnemies(delta);
		enemyInteraction();
		
		processBombing();
		updateBombs(delta);
		updateExplosions(delta);
		
		renderingModel.update(delta);
		
		if (!protagonist.isAlive()) {
			if (respawnTimeLeft > 0) {
				respawnTimeLeft -= delta;
			} else {
				protagonist.resurrection();
				resetProtagonist(respawn.x, respawn.y);
			}
		}
	}
	
	private void processBombing() {
		if (putBombFlag) {			
			int x = Math.round(protagonist.getPos().x);
			int y = Math.round(protagonist.getPos().y);	
		
			addBomb(x, y);
			
			putBombFlag = false;
		}
	}

	private void updateBombs(float delta) {		
		ArrayList <Bomb> removedBombs = new ArrayList <Bomb>();
		
		for(Map.Entry<Bomb, Position> entry : bombs.entrySet()) {
			if (entry.getKey().update(delta)) {

				player.playExplosion();
				
				Explosion b = new Explosion(entry.getValue().x, entry.getValue().y);
				explosions.add(b);
				b.perform(this);
				removedBombs.add(entry.getKey());
			}
		}
		for (Bomb b : removedBombs) {
			rmBomb(b);			
		}
		removedBombs.clear();
	}
	
	private void updateExplosions(float delta) {
		ArrayList <Explosion> removedExplosions = new ArrayList <Explosion>();
		
		for (Explosion b : explosions) {
			if (b.update(delta)) {
				for (Enemy e : b.killedEnemies) {
					rmEnemy(e);
				}
				b.killedEnemies.clear();
				b.cleanup(this);
				removedExplosions.add(b);
			}			
		}
		for (Explosion b : removedExplosions) {
			explosions.remove(b);			
		}
		removedExplosions.clear();
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
		//TODO: move setBomb to Protagonist class
		if (protagonist.alive) {
			putBombFlag = true;
		}
	}

	@Override
	public void stop() {
		protagonist.stop();		
	}
	

}
