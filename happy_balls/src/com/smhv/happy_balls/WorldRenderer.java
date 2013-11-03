package com.smhv.happy_balls;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.smhv.happy_balls.WorldRenderingModel.RenderingCell;

public class WorldRenderer {

	public static float CAMERA_WIDTH = 16f;
	public static float CAMERA_HEIGHT = 16f;	
	
	private WorldRenderingModel world;
	
	public OrthographicCamera cam;
	ShapeRenderer renderer = new ShapeRenderer();
	
	private SpriteBatch spriteBatch;

	public int width;
	public int height;
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;  
//		ppuX = (float)width / CAMERA_WIDTH;
//		ppuY = (float)height / CAMERA_HEIGHT;
	}
	
	public void SetCamera(float x, float y){
		this.cam.position.set(x, y, 0);	
		this.cam.update();
	}
	
	public WorldRenderer(WorldRenderingModel model) {
		this.world = model;
		
		spriteBatch = new SpriteBatch();
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		
		SetCamera(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f);
	}
	
	
	public void render() {		
		
		if (world.changed) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
		
		spriteBatch.begin();
		renderMap();	
		renderEnemies();
		renderProtagonist();
		spriteBatch.end();			 
	}
	
	private void draw(TextureRegion tr, Vector2 pos) {
		spriteBatch.draw(tr, pos.x, pos.y, world.ppuX, world.ppuY);
	}

	private void drawRotated(TextureRegion tr, Vector2 pos, float rot) {
		spriteBatch.draw(tr, pos.x, pos.y, 
				0.5f * world.ppuX, 0.5f * world.ppuY, 
				world.ppuX, world.ppuY, 
				1, 1, 
				rot);
	}

	private void renderProtagonist() {
		draw(world.protagonistTexture.textureRegion, world.protogonistPosition);		
	}

	private void renderEnemies() {
		for (Vector2 pos : world.enemiesPositions) {
			draw(world.enemyTexture.textureRegion, pos);
		}
		
	}

	//TODO: cache rotated texture regions 
	
	private void renderCell(RenderingCell cell, Vector2 pos) {
		if (cell.top == null || !cell.top.texture.isFullHover) {
			if (cell.bottom != null) {
				drawRotated(cell.bottom.texture.textureRegion, pos, cell.bottom.rot);
			}
		}
		if (cell.top != null) {
			drawRotated(cell.top.texture.textureRegion, pos, cell.top.rot);
		}
	}
	
	private void renderMap() {
		
		if (world.changed) {			
			for (int y = 0; y < world.renderingMap.length; y++) {
				for (int x = 0; x < world.renderingMap[y].length; x++) {
					renderCell(world.renderingMap[y][x], 
							new Vector2(x * world.ppuX, y * world.ppuY));
				}
			}
			world.changed = false;
		} else {
			for (int y = 0; y < world.renderingMap.length; y++) {
				for (int x = 0; x < world.renderingMap[y].length; x++) {
					RenderingCell cell = world.renderingMap[y][x];
					if (cell.changed) {
						renderCell(cell, new Vector2(x * world.ppuX, y * world.ppuY));
						cell.changed = false;
					}
				}
			}
			
		}
		
	}
	
}
