package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.smhv.happy_balls.GameInputController.Keys;
import com.smhv.happy_balls.sound.SoundPlayer;

public class MenuScreen extends BScreen implements InputProcessor {
	
	Texture backgroundTexture;
	Texture textTexture;
	
	TextureRegion background;
	TextureRegion title;
	TextureRegion start;
	TextureRegion settings;
	TextureRegion quit;
	TextureRegion highlight;
	

	boolean highlightOn = false;
	int hightlightIndex = -1;	
	Rectangle buttons[] = new Rectangle[3];
	
	SpriteBatch spriteBatch;

	
	public MenuScreen() {
		continuousRendering = false;
		spriteBatch = new SpriteBatch();
	}
	
	
	int width;
	int height;
	
	public void loadResources() {
		backgroundTexture = new Texture(Gdx.files.internal("graphics/game_menu_bg.png"));
		
		background = new TextureRegion(backgroundTexture, 0, 0, 840, 480);
		
		width = background.getRegionWidth();
		height = background.getRegionHeight();
		

		textTexture = new Texture(Gdx.files.internal("graphics/game_menu_text.png"));
		
		int w = textTexture.getWidth();
		int h = textTexture.getHeight();
		
		int h1 = 50;
		int h2 = 70;
		int h3 = h - 3*h2 - h1;
		
		title = new TextureRegion(textTexture, 0, 0, 350, h1);
		
		start = new TextureRegion(textTexture, 0, h1, 300, h2);		
		settings = new TextureRegion(textTexture, 0, h1+h2, 450, h2);		
		quit= new TextureRegion(textTexture, 0, h1+2*h2, 250, h2);		
		
		highlight = new TextureRegion(textTexture, 0, h-h3, 650, h3);
		

		buttons[0] = new Rectangle(width-120 - start.getRegionWidth(), 
				height-100 - start.getRegionHeight(), 
				start.getRegionWidth(), start.getRegionHeight());
		
		buttons[1] = new Rectangle(width-20 - settings.getRegionWidth(), 
				height-100 - start.getRegionHeight() 
				- 50 - settings.getRegionHeight(), 
				settings.getRegionWidth(), settings.getRegionHeight());
		
		buttons[2] = new Rectangle(width-120 - quit.getRegionWidth(), 
				height-100 - start.getRegionHeight() 
				- 50 - settings.getRegionHeight() 
				- 50 - quit.getRegionHeight(), 
				quit.getRegionWidth(), quit.getRegionHeight());
	}	
	
	private void changeSelection(int index) {
		highlightOn = true;
		hightlightIndex += index;
		if (hightlightIndex >= buttons.length) {
			hightlightIndex = hightlightIndex % buttons.length;
		}
		if (hightlightIndex < 0) {
			hightlightIndex = hightlightIndex % buttons.length;		
			hightlightIndex += 3;
		}
	}
	
	private void setSelection(int index) {
		if (index >= 0 && index < buttons.length) {
			highlightOn = true;
			hightlightIndex = index;
		}
	}
	
	private void changeSelection(float x, float y) {			
		int i = 0;
		for (Rectangle r : buttons) {
			if (r.contains(x, height-y)) {
				hightlightIndex = i;
				highlightOn = true;
			}
			i++;
		}
	}
	
	private void performAction() {
		if (highlightOn) {			
			switch(hightlightIndex) {
			case 0:
				start();
				break;
			case 1:
				settings();
				break;
			case 2:
				quit();
				break;			
			default:
				break;
			}			
		}
	}
	
	
	private void start() {		
		gameInput.startNewGame();
	}
	
	private void settings() {
	}
	
	private void quit() {
		Gdx.app.exit();
	}
	
	int renderCounter = 0;
	
	@Override
	public void render(float delta) {
		//TODO: use scene2d
	//	Gdx.app.debug("MenuScreen", "render" + renderCounter++);
		

		spriteBatch.begin();
		
		spriteBatch.draw(background, 0, 0);
		
		if (highlightOn) {
			spriteBatch.draw(highlight, buttons[hightlightIndex].x - 150, 
										buttons[hightlightIndex].y - 70);
		}
		
		spriteBatch.draw(title, 50, height-20 - title.getRegionHeight());
		
		spriteBatch.draw(start, buttons[0].x, buttons[0].y);
		spriteBatch.draw(settings, buttons[1].x, buttons[1].y);		
		spriteBatch.draw(quit, buttons[2].x, buttons[2].y);
		
		
		spriteBatch.end();		
	}


	@Override
	public void resize(int width, int height) {
		Gdx.app.debug("MenuScreen", "resize("+ width +", "+ height +")");
		Gdx.graphics.requestRendering();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Input.Keys.UP:
			changeSelection(-1);
			break;
		case Input.Keys.DOWN:
			changeSelection(1);
			break;
		case Input.Keys.ENTER:
			performAction();
			break;
		case Input.Keys.ESCAPE:
			setSelection(2);
			performAction();
			break;
		};
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		changeSelection(x, y);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		performAction();		
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		changeSelection(x, y);
		return true;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		changeSelection(x, y);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		changeSelection(amount);
		return true;
	}

	@Override
	public String getThemeName() {
		return "menu";
	}

	@Override
	public InputProcessor getInputProcessor() {
		return this;
	}

}
