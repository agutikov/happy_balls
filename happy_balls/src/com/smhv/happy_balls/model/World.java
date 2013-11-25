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
import com.smhv.happy_balls.level.Level;
import com.smhv.happy_balls.level.Level.BoxDescription;
import com.smhv.happy_balls.level.Level.ObjectDescription;
import com.smhv.happy_balls.render.WorldRenderingModel;
import com.smhv.happy_balls.render.WorldRenderingModel.ExplosionPart;
import com.smhv.happy_balls.sound.SoundPlayer;

//TODO: пользоваться полиморфизмом - чтобы пихать объекты в списки

public class World implements WorldInput {
	
	SoundPlayer soundPlayer;
	private WorldRenderingModel renderingModel;
	
	private Protagonist protagonist;	
	
	int enemyCounter = 0;
	private Map<Enemy, Integer> allEnemies;	
	
	Position respawn;
	private float respawnTime = 2f;
	private float respawnTimeLeft;
	
	private float putBombTimeout = 3f;
	private float putBombTimeLeft = 0f;

	private boolean putBombFlag;
	
	private boolean win;
	private boolean gameFinished;
	
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
		private float unexplodeTime = 0.25f;
		
		public Explosion(int x, int y) {
			this.x = x;
			this.y = y;
			timeLeft = 0.4f;
			
			killedEnemies = new ArrayList<Enemy>();
		}
		
		private boolean explodeCell(World w, int x, int y, ExplosionPart part) {
			if (x >= 0 && x < w.width && y >= 0 && y < w.height) {
				w.map[y][x].explode(this); 
				if (w.map[y][x].isExploading)
					renderingModel.setExplosion(x, y, part);
				return map[y][x].isPassable();				
			}
			return false;
		}
		
		//TODO: разные модели взрыва - ограничение по расстоянию или по площади, направление распространения (за угол)
		public void perform(World w) {	
			explodeCell(w, x, y, ExplosionPart.CENTER);
			
			if (explodeCell(w, x, y+1, ExplosionPart.PASS_V))
				explodeCell(w, x, y+2, ExplosionPart.END_U);
			
			if (explodeCell(w, x, y-1, ExplosionPart.PASS_V))
				explodeCell(w, x, y-2, ExplosionPart.END_D);
			
			if (explodeCell(w, x-1, y, ExplosionPart.PASS_H))
				explodeCell(w, x-2, y, ExplosionPart.END_L);
			
			if (explodeCell(w, x+1, y, ExplosionPart.PASS_H))
				explodeCell(w, x+2, y, ExplosionPart.END_R);
		}
		
		private void unexplodeCell(World w, int x, int y) {
			if (x >= 0 && x < w.width && y >= 0 && y < w.height) {
				w.map[y][x].unexplode(); 
			}
		}
		
		public void unexplode(World w) {
			unexplodeCell(w, x, y);
			unexplodeCell(w, x, y+1);
			unexplodeCell(w, x, y+2);
			unexplodeCell(w, x, y-1);
			unexplodeCell(w, x, y-2);
			unexplodeCell(w, x+1, y);
			unexplodeCell(w, x+2, y);
			unexplodeCell(w, x-1, y);
			unexplodeCell(w, x-2, y);	
		}
		
		private void cleanupCell(World w, int x, int y) {
			if (x >= 0 && x < w.width && y >= 0 && y < w.height) {
				w.map[y][x].cleanup(); 
			}
		}
		
		public void cleanup(World w) {		
			cleanupCell(w, x, y);
			cleanupCell(w, x, y+1);
			cleanupCell(w, x, y+2);
			cleanupCell(w, x, y-1);
			cleanupCell(w, x, y-2);
			cleanupCell(w, x+1, y);
			cleanupCell(w, x+2, y);
			cleanupCell(w, x-1, y);
			cleanupCell(w, x-2, y);		
		}
		
		//TODO: общий тип временных объектов
		
		public boolean update(float delta) {
			timeLeft -= delta;
			
			return timeLeft <= 0;		
		}
		
		public boolean isUnexploaded() {
			return timeLeft <= unexplodeTime;
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
		private ArrayList<Enemy> enemiesInThisCell;
		
		public boolean isKilling() {
			for (Enemy e : enemiesInThisCell) {
				if (e.isAlive()) {
					return true;
				}
			}
			return false;
		}
		
		public int x;
		public int y;
		
		public boolean isExploading = false;
		
		public MapCell() {
			enemiesInThisCell = new ArrayList<Enemy>();
		}
		
		public void enemyEnter (Enemy e) {
			enemiesInThisCell.add(e);
		}
		
		public void enemyOut (Enemy e) {
			enemiesInThisCell.remove(e);
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

			if (bomb != null) {
				bomb.detonate();
			}	
			if (box != null) {
				if (box.explode())
					renderingModel.explodeBox(x, y);
				else
					return;
			}			
			for (Enemy e : enemiesInThisCell) {
				soundPlayer.playMurder();
				e.kill();				
				int index = allEnemies.get(e);	
				renderingModel.killEnemy(index);					
				explosion.killedEnemies.add(e);
			}
			enemiesInThisCell.clear();
			isExploading = true;
		}
		
		public void unexplode() {
			renderingModel.rmExplosion(x, y);	
			isExploading = false;		
		}
		
		public void cleanup() {
			if (box != null && !box.isAlive()) {
				box = null;
				renderingModel.rmFixedTop(x, y);
			}	
		}
	}
	private MapCell[][] map;
	
	
	public World(WorldRenderingModel m, SoundPlayer sp) {
		renderingModel = m;
		
		allEnemies = new HashMap<Enemy, Integer>();
		bombs = new HashMap<Bomb, Position>();
		explosions = new ArrayList<Explosion>();
		
		soundPlayer = sp;
	}

	private void addEnemy(int x, int y) {
		if (map[y][x].isPassable()) {		
			Enemy e = new Enemy(new Vector2(x, y));		
			enemyCounter++;
			allEnemies.put(e, enemyCounter);		
			map[y][x].enemyEnter(e);
			renderingModel.addEnemy(enemyCounter, new Vector2(x, y));
		}
	}		
	private void rmEnemy(Enemy e) {		
		int index = allEnemies.get(e);
		allEnemies.remove(e);
		renderingModel.rmEnemy(index);
		
		win = allEnemies.isEmpty();
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
			
			soundPlayer.playSetBomb();
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
	
	public void init(Level lvl) {

			putBombFlag = false;
			win = false;
			gameFinished = false;
		
			width = lvl.mapWidth;
			height = lvl.mapHeight;
		
			map = new MapCell[height][width];
			
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
			protagonist = new Protagonist(new Vector2(lvl.protDesc.x, lvl.protDesc.y));
			renderingModel.moveProtagonistTo(lvl.protDesc.x, lvl.protDesc.y);
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
	
	
	private void kill() {
		if (protagonist.alive && protagonist.kill()) {
			soundPlayer.playDeath();
			renderingModel.kill();	
			respawnTimeLeft = respawnTime;
			renderingModel.disableBombing();
		}
	}
	
	private void enemyInteraction() {		
		int x = Math.round(protagonist.getPos().x);
		int y = Math.round(protagonist.getPos().y);	
		
		if (map[y][x].isKilling()) {
			kill();
		}
	}
	
	private void moveEnemies(float delta) {
		for (Entry<Enemy, Integer> entry : allEnemies.entrySet()) {
			Enemy e = entry.getKey();
			int index = entry.getValue();
			
			int x = Math.round(e.getPos().x);
			int y = Math.round(e.getPos().y);				
			
			e.update(delta);
			//TODO: NPC должен получать информацию о том что видит и где находится
			posCorrection(e);
			renderingModel.moveEnemyTo(index, e.getPos());
			renderingModel.setEnemyDirection(index, e.getDirection());

			int newX = Math.round(e.getPos().x);
			int newY = Math.round(e.getPos().y);	
			
			if (newX != x || newY != y) {
				map[y][x].enemyOut(e);
				map[newY][newX].enemyEnter(e);
			}
		}
	}
	
	public void update(float delta) {
		if (!gameFinished) {
			protagonist.update(delta);
			if (protagonist.isUndamagableDisabled())
				renderingModel.unhighlightProtagonist();
				
			posCorrection(protagonist);		
			{
				int x = Math.round(protagonist.getPos().x);
				int y = Math.round(protagonist.getPos().y);	
				
				if (map[y][x].isExploading) {
					kill();
				}
			}		
			renderingModel.moveProtagonistTo(protagonist.getPos().x, protagonist.getPos().y);
			renderingModel.setProtagonistDirection(protagonist.getDirection());

			moveEnemies(delta);
			enemyInteraction();
			
			processBombing(delta);
			updateBombs(delta);
			updateExplosions(delta);
			
			renderingModel.update(delta);
			
			if (!protagonist.isAlive()) {
				if (respawnTimeLeft > 0) {
					respawnTimeLeft -= delta;
				} else {
					protagonist.resurrection();
					renderingModel.resurrection();
					protagonist.setPos(respawn.x, respawn.y);
					renderingModel.moveProtagonistTo(respawn.x, respawn.y);
					renderingModel.highlightProtagonist();
					renderingModel.enableBombing();
				}
			}
			if (win) {
				soundPlayer.playGameWin();
				// gameFinished = true;
			}
		}
	}
	
	private void processBombing(float delta) {
		if (putBombTimeLeft > 0) {
			putBombTimeLeft -= delta;
			if (putBombTimeLeft < 0)
				putBombTimeLeft = 0;
			if (putBombTimeLeft == 0)
				renderingModel.enableBombing();
		}
		if (putBombFlag) {			
			int x = Math.round(protagonist.getPos().x);
			int y = Math.round(protagonist.getPos().y);	
		
			addBomb(x, y);
			
			putBombFlag = false;
			putBombTimeLeft = putBombTimeout;
			renderingModel.disableBombing();
		}
	}

	private void updateBombs(float delta) {		
		ArrayList <Bomb> removedBombs = new ArrayList <Bomb>();
		
		for(Map.Entry<Bomb, Position> entry : bombs.entrySet()) {
			if (entry.getKey().update(delta)) {

				soundPlayer.playExplosion();
				
				Explosion e = new Explosion(entry.getValue().x, entry.getValue().y);
				explosions.add(e);
				e.perform(this);
				removedBombs.add(entry.getKey());
			}
			
			renderingModel.setBombState(entry.getKey().getState(), 
					entry.getValue().x, entry.getValue().y);
		}
		for (Bomb b : removedBombs) {
			rmBomb(b);			
		}
		removedBombs.clear();
	}
	
	private void updateExplosions(float delta) {
		ArrayList <Explosion> removedExplosions = new ArrayList <Explosion>();
		
		for (Explosion expl : explosions) {
			if (expl.update(delta)) {
				for (Enemy e : expl.killedEnemies) {
					rmEnemy(e);
				}
				expl.cleanup(this);
				expl.killedEnemies.clear();
				removedExplosions.add(expl);
			} else if (expl.isUnexploaded()) {
				expl.unexplode(this);				
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
		if (protagonist.alive && putBombTimeLeft == 0) {
			putBombFlag = true;
		}
	}

	@Override
	public void stop() {
		protagonist.stop();		
	}
	

}
