package com.smhv.happy_balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen implements Screen, InputProcessor {

	BGame game;
	
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

	private SoundPlayer soundPlayer;
	
	public MenuScreen(BGame game, SoundPlayer sp) {
		this.game = game;
		soundPlayer = sp;
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
		buttons[0] = new Rectangle(width-120 - start.getRegionWidth(), 
				height-100 - start.getRegionHeight(), 
				start.getRegionWidth(), start.getRegionHeight());
		
		settings = new TextureRegion(textTexture, 0, h1+h2, 450, h2);
		buttons[1] = new Rectangle(width-20 - settings.getRegionWidth(), 
				height-100 - start.getRegionHeight() 
				- 50 - settings.getRegionHeight(), 
				settings.getRegionWidth(), settings.getRegionHeight());
		
		quit= new TextureRegion(textTexture, 0, h1+2*h2, 250, h2);
		buttons[2] = new Rectangle(width-120 - quit.getRegionWidth(), 
				height-100 - start.getRegionHeight() 
				- 50 - settings.getRegionHeight() 
				- 50 - quit.getRegionHeight(), 
				quit.getRegionWidth(), quit.getRegionHeight());
		
		
		highlight = new TextureRegion(textTexture, 0, h-h3, 650, h3);
	}
	
	private void start() {
		

		game.gameScreen.loadResources();		
		Level lvl = Level.createTestLevel01();
		game.gameScreen.setLevel(lvl);	
				
		
		game.setScreen(game.gameScreen);
	}
	
	private void settings() {
		
	}
	
	private void quit() {
		game.dispose();
		Gdx.app.exit();
	}
	

	@Override
	public void render(float delta) {
		//TODO: use scene2d
		

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
		Gdx.graphics.requestRendering();
	}

	@Override
	public void show() {
		soundPlayer.startMenuTrack();

		Gdx.graphics.setContinuousRendering(false);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
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
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		
		
		return true;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		
		highlightOn = false;
		
		int i = 0;
		for (Rectangle r : buttons) {
			if (r.contains(x, height-y)) {
				hightlightIndex = i;
				highlightOn = true;
			}
			i++;
		}
		
		
		
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
