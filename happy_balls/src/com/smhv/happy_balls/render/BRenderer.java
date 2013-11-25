package com.smhv.happy_balls.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BRenderer {

	//TODO: отдельные spriteBatch, камеры и вьюпорты для рендеринга например игры, контролов и fps
	
	/*
	 * TODO: т.к. на контролы надо нажимать 
	 * (т.е. рисовальные координаты и координаты обработки событий должны совпадать)
	 * то рендерер для контролов должен быть со стандартным FREE_VIEWPORT или вообще без него
	 * и ресайзиться там тоже ничего не будет т.к. размеры самих контролов на экране будут настраваться
	 * и расположение будет относительно сторон экрана 
	 * 
	 * Relative render - устанавливать координаты в окне относительно которых рендерится, flip осей
	 * 
	 */
	
	/*
	 * TODO: config manager, default values in classes (renderer. soundPlayer, ...)
	 * config parts: engine (hidde for users), user preferences (settings menu),
	 * config layers: default compiled-in, config file
	 */
	
	protected static final float CAMERA_WIDTH  = 840f;
	protected static final float CAMERA_HEIGHT = 480f;
	protected static final float CAMERA_ASPECT = CAMERA_WIDTH / CAMERA_HEIGHT;
    
	protected OrthographicCamera camera;
	protected SpriteBatch spriteBatch;	
	public Rectangle viewport;	
	
	protected Vector2 cameraPosition = new Vector2(0, 0);
	private Rectangle cameraViewport = new Rectangle(-CAMERA_WIDTH/2, -CAMERA_HEIGHT/2, CAMERA_WIDTH, CAMERA_HEIGHT);
	
	public void setCamera (float x, float y) {
		cameraPosition.x = x;
		cameraPosition.y = y;
	}

	protected void prepareRender() {

	//	gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);		

	//	camera.position.set(cameraPosition.x, cameraPosition.y, 0);
	//	camera.update();
				
	}
	
	public abstract void render();
	
	public BRenderer() {
		spriteBatch = new SpriteBatch();		
		camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		spriteBatch.setProjectionMatrix(camera.combined);
		viewport = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	}
	
	
	//TODO: сбор статистики для профилирования
	
	//TODO: использовать отдельные батчи для рендеринга с blending и без
	
	private boolean isVisible(float x, float y, float w, float h) {
		//TODO: change to strong solution
		return cameraViewport.contains(x, y)
				|| cameraViewport.contains(x + w, y + h)
				|| cameraViewport.contains(x + w/2, y + h/2)
				|| cameraViewport.contains(x, y + h)
				|| cameraViewport.contains(x + w, y);
	}
	
	private void realDraw (TextureRegion tr, float x, float y, float w, float h, float rot) {
		x = x - cameraPosition.x;
		y = y - cameraPosition.y;
		if (isVisible(x, y, w, h)) {
			spriteBatch.draw(tr, x, y, 0.5f*w, 0.5f*h, w, h, 1, 1, rot);
		}
	}
	
	protected void draw(TextureRegion tr, float x, float y) {
			realDraw(tr, x, y, tr.getRegionWidth(), tr.getRegionHeight(), 0);
	}
	
	protected void draw(TextureRegion tr, float x, float y, float w, float h) {
			realDraw(tr, x, y, w, h, 0);
	}

	protected void drawRotated(TextureRegion tr, float x, float y, float w, float h, float rot) {
			realDraw(tr, x, y, w, h, rot);
	}
	
	public enum RelativeMode {
		CENTER, //default
		LEFT_BOTTOM,
		LEFT_TOP,
		RIGHT_TOP,
		RIGHT_BOTTOM
	}
	
	protected void relativeDraw(RelativeMode rmode, TextureRegion tr, float x, float y) {
		relativeDraw(rmode, tr, x, y, tr.getRegionWidth(), tr.getRegionHeight(), 0);
	}
	
	protected void relativeDraw(RelativeMode rmode, TextureRegion tr, float x, float y, float w, float h, float rot) {
		switch (rmode) {	
		case CENTER:
			x -= w/2;
			y -= h/2;
			break;
		case LEFT_BOTTOM:
			x -= viewport.width/2;
			y -= viewport.height/2;
			break;
		case LEFT_TOP: 
			x -= viewport.width/2;
			y = viewport.height/2 - y - h;
			break;
		case RIGHT_TOP: 
			x = viewport.width/2 - x - w;
			y = viewport.height/2 - y - h;
			break;
		case RIGHT_BOTTOM: 
			x = viewport.width/2 - x - w;
			y -= viewport.height/2;
			break;
		default: break;
		}		
		spriteBatch.draw(tr, x, y, 0.5f*w, 0.5f*h, w, h, 1, 1, rot);
	}
	
	public enum ResizeMode {
		//TODO: Надо ли делать при NON_SCALE отдельно FIXED_VIEPORT и FREE_VIEPORT
		// размер изображения сохраняется, меняется область видимости
		NON_SCALE, 
		// скалируется вместе с окном, деформируется
		SCALE,
		// область видимости не меняется, вписывается в окно целиком
		FIT,
		// область видимости не меняется, вписывается в окно c обрезанными краями		
		CROP
	}

	protected ResizeMode resizeMode = ResizeMode.NON_SCALE;
	
	public void setSize (int w, int h) {	
		/* ВАЖНО!
		 * При рендеринге используется координатная система камеры, 
		 * которая может быть в каких угодно единицах, 
		 * не имея ничего общего с пикселями.
		 * Центр камеры - начало отсчёта.
		 * Эта координатся система натягивается на окно в котором отображается.
		 * Вьюпорт вырезается в окне для отображения.
		 * Координаты вьюпорта в пикселях как и у окна.
		 */
		float aspectRatio;
		switch (resizeMode) {
		case NON_SCALE:
			camera = new OrthographicCamera(w, h);
			spriteBatch.setProjectionMatrix(camera.combined);
			viewport.width = w;
			viewport.height = h;
			viewport.y = 0;
			viewport.x = 0;
			break;
		case SCALE:			
			viewport.width = w;
			viewport.height = h;
			viewport.y = 0;
			viewport.x = 0;
			break;
		case FIT:	
			aspectRatio = (float) w / (float) h;			
			if (aspectRatio > CAMERA_ASPECT) {
				viewport.height = h;
				viewport.width = viewport.height * CAMERA_ASPECT;
				viewport.x = (w - viewport.width)/2;
				viewport.y = 0;
			} else {
				viewport.width = w;
				viewport.height = viewport.width / CAMERA_ASPECT;
				viewport.x = 0;
				viewport.y = (h - viewport.height)/2;
			}
			break;
		case CROP:			
			aspectRatio = (float) w / (float) h;			
			if (aspectRatio < CAMERA_ASPECT) {
				viewport.height = h;
				viewport.width = viewport.height * CAMERA_ASPECT;
				viewport.x = (w - viewport.width)/2;
				viewport.y = 0;
			} else {
				viewport.width = w;
				viewport.height = viewport.width / CAMERA_ASPECT;
				viewport.x = 0;
				viewport.y = (h - viewport.height)/2;
			}
			break;
		default:
			break;
		}

		cameraViewport.x = -viewport.width/2;
		cameraViewport.y = -viewport.height/2;
		cameraViewport.width = viewport.width;
		cameraViewport.height = viewport.height;

		Gdx.graphics.getGL10().glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
		
	}	

}
