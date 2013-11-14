package com.smhv.happy_balls.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class BRenderer {

	
	protected float ppuX = 32;	// пикселей на ячейку мира по X 
	protected float ppuY = 32;	// пикселей на ячейку мира по Y 
	
	protected static final float CAMERA_WIDTH  = 840f;
	protected static final float CAMERA_HEIGHT = 480f;
    
	protected OrthographicCamera camera;
	protected SpriteBatch spriteBatch;	
	public Rectangle viewport;	
	

	protected void prepareRender() {

		GL10 gl = Gdx.graphics.getGL10();
		
		gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);		

		gl.glViewport((int) viewport.x, (int) viewport.y, 
				(int) viewport.width, (int) viewport.height);
		
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

}
