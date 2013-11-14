package com.smhv.happy_balls.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class BRenderer {

	//TODO: отдельные spriteBatch, камеры и вьюпорты для рендеринга например игры, контролов и fps
	
	/*
	 * TODO: т.к. на контролы надо нажимать 
	 * (т.е. рисовальные координаты и координаты обработки событий должны совпадать)
	 * то рендерер для контролов должен быть со стандартным FREE_VIEWPORT или вообще без него
	 * и ресайзиться там тоже ничего не будет т.к. размеры самих контролов на экране будут настраваться
	 * и расположение будет относительно сторон экрана 
	 */
	
	protected float ppuX = 32;	// пикселей на ячейку мира по X 
	protected float ppuY = 32;	// пикселей на ячейку мира по Y 
	
	protected static final float CAMERA_WIDTH  = 840f;
	protected static final float CAMERA_HEIGHT = 480f;
	protected static final float CAMERA_ASPECT = CAMERA_WIDTH / CAMERA_HEIGHT;
    
	protected OrthographicCamera camera;
	protected SpriteBatch spriteBatch;	
	public Rectangle viewport;	
	
	private float cameraX = 0;
	private float cameraY = 0;
	
	public void setCamera (float x, float y) {
		cameraX = x;
		cameraY = y;
	}

	protected void prepareRender() {
		GL10 gl = Gdx.graphics.getGL10();
		
		gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);		

		gl.glViewport((int) viewport.x, (int) viewport.y, 
				(int) viewport.width, (int) viewport.height);

		camera.position.set(cameraX, cameraY, 0);
		camera.update();
		
		// делаем чтобы батч рисовал в координатах нашей камеры
		// а не своей собственной по умолчанию
		spriteBatch.setProjectionMatrix(camera.combined);		
	}
	
	public abstract void render();
	
	public BRenderer() {
		spriteBatch = new SpriteBatch();		
		camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		viewport = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	}
	

	/*
	 * TODO: resize: non-scale / fit / crop / scale
	 */
	
	public enum ResizeMode {
		// размер изображения сохраняется, меняется область видимости
		NON_SCALE, 
		// скалируется вместе с окном, деформируется
		SCALE,
		// область видимости не меняется, вписывается в окно целиком
		FIT,
		// область видимости не меняется, вписывается в окно c обрезанными краями		
		CROP
	}

	public ResizeMode resizeMode = ResizeMode.NON_SCALE;
	
	public void setSize (int w, int h) {	
		float aspectRatio;
		switch (resizeMode) {
		case NON_SCALE:
			camera = new OrthographicCamera(w, h);
			viewport.width = w;
			viewport.height = h;
			break;
		case SCALE:
			viewport.width = w;
			viewport.height = h;
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

}
