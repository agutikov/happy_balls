package com.smhv.happy_balls;

/*
 * TODO: engine settings:
    - размер камеры по умолчанию
    - камера следит за игроком или нет
    - поведение при ресайзе
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.smhv.happy_balls.WorldRenderingModel.RenderingCell;

public class WorldRenderer {
	
	private WorldRenderingModel world;
	//TODO: объединить WorldRenderingModel и WorldRenderer
	// TODO: добавить partialRendering - рендерить только то что попадает в viewport
	// TODO: рендерить только то что visible
	// TODO: нормально рисовать текстуры с альфаканалом - без усиления контрастности со временем

	private OrthographicCamera camera;
	ShapeRenderer renderer;	
	private SpriteBatch spriteBatch;	
	Rectangle viewport;	
	
	private float ppuX = 64;	// пикселей на ячейку мира по X 
	private float ppuY = 64;	// пикселей на ячейку мира по Y 
	
    private static final float CAMERA_WIDTH  = 8f;
    private static final float CAMERA_HEIGHT = 8f;
    
	public WorldRenderer(WorldRenderingModel model) {
		this.world = model;		
		spriteBatch = new SpriteBatch();		
		camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		renderer = new ShapeRenderer();
		viewport = new Rectangle(0, 0, CAMERA_WIDTH * ppuX, CAMERA_HEIGHT * ppuY);
	}
	
	public enum ScaleMode {
		// размер изображения сохраняется, меняется область видимости
		NON_SCALE_FREE_VIEWPORT, 
		// скалируется, сохраняется отношение сторон и высота
		SCALE_Y_FREE_VIEWPORT, 
		// скалируется, сохраняется соотношение сторон и ширина
		SCALE_X_FREE_VIEWPORT,
		// скалируется вместе с окном, деформируется
		SCALE_XY_FREE_VIEWPORT,
		// размер изображения сохраняется, область видимости не меняется при увеличении
		NON_SCALE_FIXED_VIEWPORT,
		// область видимости не меняется, вписывается в окно целиком
		SCALE_FIT_FIXED_VIEWPORT,
		// область видимости не меняется, вписывается в окно c обрезанными краями		
		SCALE_CROP
	}

	public ScaleMode scaleMode = ScaleMode.NON_SCALE_FREE_VIEWPORT;
	
	public void setSize (int w, int h) {	
		float aspectRatio;
		switch (scaleMode) {
		case NON_SCALE_FREE_VIEWPORT:
			camera = new OrthographicCamera(w/ppuX, h/ppuY);
			viewport.width = w;
			viewport.height = h;
			break;
		case SCALE_Y_FREE_VIEWPORT:
			aspectRatio = (float) w / (float) h;
			camera = new OrthographicCamera(CAMERA_WIDTH * aspectRatio, CAMERA_HEIGHT);
			viewport.width = w;
			viewport.height = h;
			break;
		case SCALE_X_FREE_VIEWPORT:
			aspectRatio = (float) w / (float) h;
			camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT / aspectRatio);
			viewport.width = w;
			viewport.height = h;
			break;
		case SCALE_XY_FREE_VIEWPORT:
			viewport.width = w;
			viewport.height = h;
			break;
		case NON_SCALE_FIXED_VIEWPORT:
			viewport.x = (w - viewport.width)/2;
			viewport.y = (h - viewport.height)/2;
			break;
		case SCALE_FIT_FIXED_VIEWPORT:		
			viewport.width = Math.min(w, h);
			viewport.height = viewport.width;
			viewport.x = (w - viewport.width)/2;
			viewport.y = (h - viewport.height)/2;
			break;
		case SCALE_CROP:		
			viewport.width = Math.max(w, h);
			viewport.height = viewport.width;
			viewport.x = (w - viewport.width)/2;
			viewport.y = (h - viewport.height)/2;
			break;
		default:
			break;
		}
		/* ВАЖНО!
		 * При рендеринге используется координатная система камеры, 
		 * которая может быть в каких угодно единицах, 
		 * не имея ничего общего с пикселями.
		 * Центр камеры - начало отсчёта.
		 * Эта координатся система натягивается на окно в котором отображается.
		 * Вьюпорт вырезается в окне для отображения.
		 * Координаты вьюпорта в пикселях как и у окна.
		 */
	}	
	
	public void SetCamera(float x, float y){
		camera.position.set(x+0.5f, y+0.5f, 0);
		camera.update();
	}	
	
	public void render() {				
		GL10 gl = Gdx.graphics.getGL10();
		
		if (world.isChanged()) {
			gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
		}

		gl.glViewport((int) viewport.x, (int) viewport.y, 
				(int) viewport.width, (int) viewport.height);
	        
		// делаем чтобы батч рисовал в координатах нашей камеры
		// а не своей собственной по умолчанию
		spriteBatch.setProjectionMatrix(camera.combined);
	
		spriteBatch.begin();		
		renderMap();	
		renderEnemies();
		renderProtagonist();
		spriteBatch.end();			 
	}
	
	//TODO: использовать размеры объектов?
	private void draw(TextureRegion tr, float x, float y) {
		spriteBatch.draw(tr, 
				(x), 
				(y), 
				0, 0, 
				1, 1, 
				1, 1, 
				0);
	}

	private void drawRotated(TextureRegion tr, float x, float y, float rot) {
		spriteBatch.draw(tr, 
				(x), 
				(y), 
				0.5f, 0.5f, 
				1, 1, 
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
