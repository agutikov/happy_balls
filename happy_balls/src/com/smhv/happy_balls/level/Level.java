package com.smhv.happy_balls.level;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.smhv.happy_balls.model.FixedObject;
import com.smhv.happy_balls.model.FixedObject.Orientation;



public class Level {
	
	//TODO: load level from file, object to sprite name mapping too
	
	public static Map<String, String> spriteNames = new HashMap<String, String>();
	
	{
		spriteNames.put("protagonist", "player");
		spriteNames.put("enemy", "enemy");
		spriteNames.put("bomb", "bomb");
		spriteNames.put("box", "box");
		spriteNames.put("ground", "floor");
		spriteNames.put("brick", "brick");
		spriteNames.put("wall", "wall");
		spriteNames.put("corner", "corner");
	}

	public class ObjectDescription {
		public int x;
		public int y;
		public ObjectDescription (int posX, int posY) {
			x = posX;
			y = posY;
		}
	}
	
	public class BoxDescription extends ObjectDescription {
		public FixedObject.Orientation orientation;

		public BoxDescription (int posX, int posY, FixedObject.Orientation or) {
			super(posX, posY);
			orientation = or;
		}
	}
	
	// mapping object names to textures names
	public Map<String, String> objectSpriteNames;
	
	public int mapWidth;
	public int mapHeight;
	
	public Array<ObjectDescription> enemies;	
	public Map<String, Array<BoxDescription>> boxes;
	public Array<ObjectDescription> grounds;	
	public ObjectDescription protDesc;
	
	
	public Level (int w, int h) {

		mapWidth = w;
		mapHeight = h;
		
		grounds = new Array<ObjectDescription>();
		enemies = new Array<ObjectDescription>();

		boxes = new HashMap<String, Array<BoxDescription>>();
		
	}
	
	public static Level createTestLevel01() {

		int w = 111;
		int h = 111;
		
		Level lvl = new Level(w, h);
		
		lvl.objectSpriteNames = spriteNames;
		
		for (int y = 1; y < h-1; y++) {
			for (int x = 1; x < w-1; x++) {				
				lvl.grounds.add(lvl.new ObjectDescription(x, y));
			}
		}
		
		//TODO: flags fill ground, filling rectangles
		
		Array<BoxDescription> walls = new Array<BoxDescription>();
		for (int i = 1; i < h-1; i++) {
			walls.add(lvl.new BoxDescription(0, i, Orientation.RIGHT));
			walls.add(lvl.new BoxDescription(w-1, i, Orientation.LEFT));
		}		
		for (int i = 1; i < w-1; i++) {
			walls.add(lvl.new BoxDescription(i, 0, Orientation.DOWN));
			walls.add(lvl.new BoxDescription(i, h-1, Orientation.UP));
		}
		
		lvl.boxes.put("wall", walls);

		Array<BoxDescription> corners = new Array<BoxDescription>();
		corners.add(lvl.new BoxDescription(0, 0, Orientation.DOWN));
		corners.add(lvl.new BoxDescription(0, h-1, Orientation.RIGHT));
		corners.add(lvl.new BoxDescription(w-1, h-1, Orientation.UP));
		corners.add(lvl.new BoxDescription(w-1, 0, Orientation.LEFT));
		lvl.boxes.put("corner", corners);
		

		Array<BoxDescription> bricks = new Array<BoxDescription>();
		for (int y = 2; y < h-2; y+=2)
			for (int x = 2; x < w-2; x+=2) 
				bricks.add(lvl.new BoxDescription(x, y, Orientation.DEFAULT));
		
		lvl.boxes.put("brick", bricks);
		

		Random random = new Random();
		
		Array<BoxDescription> boxes = new Array<BoxDescription>();
		for (int y = 1; y < h-1; y+=1) {
			for (int x = 1; x < w-1; x+=1) {
				if (!(x%2 == 0 && y%2 == 0)) {
				if ((Math.abs(w/2 - x) > 1 || Math.abs(h/2 - y) > 1) 
						&& random.nextBoolean()) {
					boxes.add(lvl.new BoxDescription(x, y, Orientation.DEFAULT));
				} else if ((Math.abs(w/2 - x) > 3 || Math.abs(h/2 - y) > 3)
						&& random.nextBoolean()) {
					lvl.enemies.add(lvl.new ObjectDescription(x, y));
				}
				}
			}
		}

		lvl.boxes.put("box", boxes);
		
		lvl.protDesc = lvl.new ObjectDescription(w/2, h/2);
		
		return lvl;
	}
	
	public static Level createTestLevel02() {

		int w = 4;
		int h = 2;
		
		Level lvl = new Level(w, h);
		
		lvl.objectSpriteNames = spriteNames;
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {				
				lvl.grounds.add(lvl.new ObjectDescription(x, y));
			}
		}
		

		Array<BoxDescription> bricks = new Array<BoxDescription>();

		bricks.add(lvl.new BoxDescription(1, 1, Orientation.DEFAULT));
		
		lvl.boxes.put("brick", bricks);
		
		
		lvl.enemies.add(lvl.new ObjectDescription(0, 1));
		
		
		lvl.protDesc = lvl.new ObjectDescription(3, 1);
		
		return lvl;
	}
	
	
	public static Level createTestLevel03() {

		int w = 10;
		int h = 1;
		
		Level lvl = new Level(w, h);
		
		lvl.objectSpriteNames = spriteNames;
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {				
				lvl.grounds.add(lvl.new ObjectDescription(x, y));
			}
		}
		

		Array<BoxDescription> bricks = new Array<BoxDescription>();

		bricks.add(lvl.new BoxDescription(1, 0, Orientation.DEFAULT));
		
		lvl.boxes.put("box", bricks);
		
		
		lvl.enemies.add(lvl.new ObjectDescription(0, 0));
		lvl.enemies.add(lvl.new ObjectDescription(0, 0));
		
		
		lvl.protDesc = lvl.new ObjectDescription(3, 0);
		
		return lvl;
	}
	
	
}
