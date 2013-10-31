package com.smhv.happy_balls;



import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {
	
	Array<Brick> bricks = new Array<Brick>();
	
	public Player player;
	
	public int width;
	public int height;
	
	public Array<Brick> getBricks() {
		return bricks;
	}

	public Player getPlayer() {
		return player;
	}
	
	public World() {
		
		width = 16;
		height = 16;

		createWorld();
		
	}
	
	public void createWorld() {
		player = new Player(new Vector2(6,2));
		
		bricks.add(new Brick(new Vector2(3, 2)));
		bricks.add(new Brick(new Vector2(5, 3)));
		bricks.add(new Brick(new Vector2(10, 6)));
		bricks.add(new Brick(new Vector2(4, 8)));
		bricks.add(new Brick(new Vector2(8, 9)));
		bricks.add(new Brick(new Vector2(13, 10)));
		bricks.add(new Brick(new Vector2(6, 5)));
		bricks.add(new Brick(new Vector2(2, 2)));
	
		
	}
}
