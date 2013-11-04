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

	
	private float sizeOfGameCell = 64;
	
	public float cameraWidth = 8f;
	public float cameraHeight = 8f;	
	
	private WorldRenderingModel world;
	//TODO: объединить WorldRenderingModel и WorldRenderer
	
	public OrthographicCamera cam;
	ShapeRenderer renderer = new ShapeRenderer();
	
	private SpriteBatch spriteBatch;

	public int width;
	public int height;
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h; 
		// размеры камеры нигде не используются - изменим разрешение
		world.ppuX = w / cameraWidth;
		world.ppuY = h / cameraHeight;
		
//		cam.viewportWidth = w / sizeOfGameCell;
//		cam.viewportHeight = h / sizeOfGameCell;
	
	//	Gdx.app.debug("setSize", cam.viewportWidth + ", " + cam.viewportHeight);
		
		//cam.setToOrtho(false, cameraWidth, cameraHeight);
		
		
		//TODO: Сделать так чтобы при ресайзе увеличивался или уменьшался обзор
		// а не сжималось и растягивалось изображение
		//TODO: Сделать чтобы при резсайзе viewport оставалься тех же игровых размеров
		// и вписывался по высоте и ширине в окно по наименьше или по наибольше 
		// т.е. с черными краями или с обрезанными краями
	}
	
	public void SetCamera(float x, float y){
		this.cam.position.set(x, y, 0);	
		this.cam.update();
	}
	
	public WorldRenderer(WorldRenderingModel model) {
		this.world = model;
		
		spriteBatch = new SpriteBatch();
		this.cam = new OrthographicCamera(cameraWidth, cameraHeight);
		
		// set camera on protagonist
		// SetCamera(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f);
	}
	
	
	public void render() {		
		
		if (world.isChanged()) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			
			
		}
		
		spriteBatch.begin();
		renderMap();	
		renderEnemies();
		renderProtagonist();
		spriteBatch.end();			 
	}
	
	private void draw(TextureRegion tr, float x, float y) {
		spriteBatch.draw(tr, 
				(x - cam.position.x) * world.ppuX, 
				(y - cam.position.y) * world.ppuY, 
				0, 0, 
				sizeOfGameCell, sizeOfGameCell, 
				1, 1, 
				0);
	}

	private void drawRotated(TextureRegion tr, float x, float y, float rot) {
		spriteBatch.draw(tr, 
				(x - cam.position.x) * world.ppuX, 
				(y - cam.position.y) * world.ppuY, 
				0.5f * world.ppuX, 0.5f * world.ppuY, 
				sizeOfGameCell, sizeOfGameCell, 
				1, 1, 
				rot);
	}

	private void renderProtagonist() {
		draw(world.protagonistTexture.textureRegion, 
				world.protogonistPosition.x, 
				world.protogonistPosition.y);		
	}

	private void renderEnemies() {
		for (Vector2 pos : world.enemiesPositions) {
			draw(world.enemyTexture.textureRegion, pos.x, pos.y);
		}
		
	}

	//TODO: cache rotated texture regions 
	
	private void renderCell(RenderingCell cell, int x, int y) {
		if (cell.top == null || !cell.top.texture.isFullHover) {
			if (cell.bottom != null) {
				drawRotated(cell.bottom.texture.textureRegion, x, y, cell.bottom.rot);
			}
		}
		if (cell.top != null) {
			drawRotated(cell.top.texture.textureRegion, x, y, cell.top.rot);
		}
	}
	
	private void renderMap() {
		
		if (world.isChanged()) {			
			for (int y = 0; y < world.renderingMap.length; y++) {
				for (int x = 0; x < world.renderingMap[y].length; x++) {
					renderCell(world.renderingMap[y][x], x, y);
				}
			}
			world.save();
		} else {
			for (int y = 0; y < world.renderingMap.length; y++) {
				for (int x = 0; x < world.renderingMap[y].length; x++) {
					RenderingCell cell = world.renderingMap[y][x];
					if (cell.changed) {
						renderCell(cell, x, y);
						cell.changed = false;
					}
				}
			}
			
		}
		
	}
	
}
