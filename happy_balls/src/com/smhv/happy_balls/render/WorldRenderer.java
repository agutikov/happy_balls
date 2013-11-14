package com.smhv.happy_balls.render;

/*
 * TODO: engine settings:
    - размер камеры по умолчанию
    - камера следит за игроком или нет
    - поведение при ресайзе
 */

import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.smhv.happy_balls.render.WorldRenderingModel.RenderingCell;

public class WorldRenderer {

	public FPSLogger fpsLogger = new FPSLogger();
	
	BitmapFont font;
	float fps = 0;
	float fpsCount = 0;
	long lastTime;
	
	private WorldRenderingModel world;
	
	public void bindRenderingModel(WorldRenderingModel model) {
		world = model;
	}
	// TODO: рендерить только то что visible
	// TODO: нормально рисовать текстуры с альфаканалом - без усиления контрастности со временем

	// TODO: добавить partialRendering - рендерить только то что попадает в viewport
	//TODO: добавить fpslogger и вывод на экран
	//TODO: сравнить рендеринг всего и только вьюпорта
	
	
	private OrthographicCamera camera;
	ShapeRenderer renderer;	
	private SpriteBatch spriteBatch;	
	Rectangle viewport;	
	
	private float ppuX = 32;	// пикселей на ячейку мира по X 
	private float ppuY = 32;	// пикселей на ячейку мира по Y 
	
    private static final float CAMERA_WIDTH  = 840f;
    private static final float CAMERA_HEIGHT = 480f;
    
    public void loadResources() {		
		font = new BitmapFont(Gdx.files.local("fonts/font_exocet.fnt"));
		font.setColor(0.0f, 1.0f, 0.0f, 1.0f);    	
    }
    
	public WorldRenderer() {	
		spriteBatch = new SpriteBatch();		
		camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		renderer = new ShapeRenderer();
		viewport = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
	}
	
	public enum ScaleMode {
		// размер изображения сохраняется, меняется область видимости
		NON_SCALE_FREE_VIEWPORT, 
		// скалируется, сохраняется отношение сторон и высота
		SCALE_Y_FREE_VIEWPORT, 
		// скалируется, сохраняется соотношение сторон и ширина
		SCALE_X_FREE_VIEWPORT,
		// скалируется вместе с окном, деформируется
		SCALE_XY,
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
			camera = new OrthographicCamera(w, h);
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
		case SCALE_XY:
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
	
	public void setCamera(float x, float y){
		camera.position.set((x+0.5f)*ppuX, (y+0.5f)*ppuY, 0);
		camera.update();
	}	
	
	public void render() {				
		GL10 gl = Gdx.graphics.getGL10();
		
		gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);		

		gl.glViewport((int) viewport.x, (int) viewport.y, 
				(int) viewport.width, (int) viewport.height);
	        
		// делаем чтобы батч рисовал в координатах нашей камеры
		// а не своей собственной по умолчанию
		spriteBatch.setProjectionMatrix(camera.combined);
	
		long curr = TimeUtils.millis();
		if (curr - lastTime > 1000) {			
			fps = fpsCount / ((float)(curr - lastTime)/1000);
			fpsCount = 0;
			lastTime = curr;			
		//	fpsLogger.log();
		}
		fpsCount++;
		
		
		spriteBatch.begin();		
		renderMap();	
		spriteBatch.enableBlending();
		renderEnemies();		
		renderProtagonist();
		renderExplosions();
		renderFPS();
		spriteBatch.disableBlending();
		spriteBatch.end();			 
	}
	
	//TODO: использовать размеры объектов?
	private void draw(TextureRegion tr, float x, float y) {
		spriteBatch.draw(tr, 
				(x)*ppuX, 
				(y)*ppuY, 
				0, 0, 
				1*ppuX, 1*ppuY, 
				1, 1, 
				0);
	}

	private void drawRotated(TextureRegion tr, float x, float y, float rot) {
		spriteBatch.draw(tr, 
				(x)*ppuX, 
				(y)*ppuY, 
				0.5f*ppuY, 0.5f*ppuX, 
				1*ppuX, 1*ppuY, 
				1, 1, 
				rot);
	}

	private void renderExplosions() {
		for (int y = 0; y < world.renderingMap.length; y++) {
			for (int x = 0; x < world.renderingMap[y].length; x++) {
				if (world.renderingMap[y][x].explosion) {
					
					//TODO в рендеринг модели использовать только спрайты
					// это решит вопрос со всеми поворотами, отражениями и координатами
					// тупа один спрайт для каждого статического элемента
					
					world.bombExplosionTextureMap.explosionSprites.get(
							world.renderingMap[y][x].part).setPosition(x*ppuX, y*ppuY);
					
					world.bombExplosionTextureMap.explosionSprites.get(
							world.renderingMap[y][x].part).draw(spriteBatch);
				}
			}
		}
	}
	
	private void renderProtagonist() {
		if (world.protagonistRenderObject.tint) {
			spriteBatch.setColor(world.protagonistRenderObject.tintColor);
		}
		draw(world.protagonistRenderObject.currentFrame(), 
				world.protagonistRenderObject.getPos().x, 
				world.protagonistRenderObject.getPos().y);	
		if (world.protagonistRenderObject.tint) {
			spriteBatch.setColor(1f, 1f, 1f, 1f);	
		}
	}

	private void renderEnemies() {
		for (Entry<Integer, RenderFreeObject> entry : world.enemies.entrySet()) {
			draw(entry.getValue().currentFrame(), 
					entry.getValue().getPos().x, 
					entry.getValue().getPos().y);
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
		if (cell.box != null) {
			draw(cell.box.currentFrame(), x, y);
		}
		if (cell.bomb != null) {
			spriteBatch.enableBlending();
			draw(cell.bomb.currentFrame(), x, y);
			spriteBatch.disableBlending();
		}
	}
	
	private void renderMap() {
					
		for (int y = 0; y < world.renderingMap.length; y++) {
			for (int x = 0; x < world.renderingMap[y].length; x++) {
				renderCell(world.renderingMap[y][x], x, y);
			}
		}
		
	}
	
	private void renderFPS() {
		font.draw(spriteBatch, String.format("%3.1f", fps), 
				camera.position.x - viewport.width/2 + 16, 
				camera.position.y - viewport.height/2 + 48);
	}
	
}
