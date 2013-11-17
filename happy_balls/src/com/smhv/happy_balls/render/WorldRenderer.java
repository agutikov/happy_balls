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

public class WorldRenderer extends BRenderer {

	public FPSLogger fpsLogger = new FPSLogger();
	
	BitmapFont font;
	float fps = 0;
	float fpsCount = 0;
	long lastTime;
		
	private float ppuX = 32;	// пикселей на ячейку мира по X 
	private float ppuY = 32;	// пикселей на ячейку мира по Y 
	
	private WorldRenderingModel world;
	
	public void bindRenderingModel(WorldRenderingModel model) {
		world = model;
	}
	// TODO: рендерить только то что visible
	// TODO: нормально рисовать текстуры с альфаканалом - без усиления контрастности со временем

	//TODO: сравнить рендеринг всего и только вьюпорта
    
    public void loadResources() {		
		font = new BitmapFont(Gdx.files.internal("fonts/font_exocet.fnt"));
		font.setColor(0.0f, 1.0f, 0.0f, 1.0f);    	
    }

	
	public void setCamera(float x, float y){
		super.setCamera((x+0.5f)*ppuX, (y+0.5f)*ppuY);
	}	
	

	public void render() {				
		prepareRender();		
	
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
				world.protagonistRenderObject.getPos().x*ppuX, 
				world.protagonistRenderObject.getPos().y*ppuY,
				world.protagonistRenderObject.currentFrame().getRegionWidth(),
				world.protagonistRenderObject.currentFrame().getRegionHeight());	
		if (world.protagonistRenderObject.tint) {
			spriteBatch.setColor(1f, 1f, 1f, 1f);	
		}
	}

	private void renderEnemies() {
		for (Entry<Integer, RenderFreeObject> entry : world.enemies.entrySet()) {
			draw(entry.getValue().currentFrame(), 
					entry.getValue().getPos().x*ppuX, 
					entry.getValue().getPos().y*ppuY,
					entry.getValue().currentFrame().getRegionWidth(),
					entry.getValue().currentFrame().getRegionHeight());
		}
		
	}

	//TODO: cache rotated texture regions 
	
	private void renderCell(RenderingCell cell, int x, int y) {
		
		if (cell.top == null || !cell.top.texture.isFullHover) {
			if (cell.bottom != null) {
				drawRotated(cell.bottom.texture.textureRegion, x*ppuX, y*ppuY,
						cell.bottom.texture.textureRegion.getRegionWidth(),
						cell.bottom.texture.textureRegion.getRegionHeight(),
						cell.bottom.rot);
			}
		}
		if (cell.top != null) {
			drawRotated(cell.top.texture.textureRegion, x*ppuX, y*ppuY,
					cell.top.texture.textureRegion.getRegionWidth(),
					cell.top.texture.textureRegion.getRegionHeight(),
					cell.top.rot);
		}
		if (cell.box != null) {
			draw(cell.box.currentFrame(), x*ppuX, y*ppuY,
					cell.box.currentFrame().getRegionWidth(),
					cell.box.currentFrame().getRegionHeight());
		}
		if (cell.bomb != null) {
			spriteBatch.enableBlending();
			draw(cell.bomb.currentFrame(), x*ppuX, y*ppuY,
					cell.bomb.currentFrame().getRegionWidth(),
					cell.bomb.currentFrame().getRegionHeight());
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
