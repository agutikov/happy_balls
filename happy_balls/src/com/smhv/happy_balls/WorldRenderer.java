package com.smhv.happy_balls;


import java.util.HashMap;
import java.util.Map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class WorldRenderer {
	public static float CAMERA_WIDTH = 16f;
	public static float CAMERA_HEIGHT = 16f;
	
	private World world;
	
	public OrthographicCamera cam;
	ShapeRenderer renderer = new ShapeRenderer();
	
	private SpriteBatch spriteBatch;
	Texture texture;
	public  Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();
	
	public int width;
	public int height;
	public float ppuX = 512 / CAMERA_WIDTH;	// пикселей на точку мира по X 
	public float ppuY = 512 / CAMERA_HEIGHT;	// пикселей на точку мира по Y 
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;  
//		ppuX = (float)width / CAMERA_WIDTH;
//		ppuY = (float)height / CAMERA_HEIGHT;
	}
	
	public void SetCamera(float x, float y){
		this.cam.position.set(x, y,0);	
		this.cam.update();
	}
	
	public WorldRenderer(World world) {
		spriteBatch = new SpriteBatch();
		this.world = world;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		SetCamera(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f);
		
		loadTextures();

	}
	
	private void loadTextures() {
		texture  = new Texture(Gdx.files.internal("Map_32.png"));
		TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight());
		textureRegions.put("player", tmp[0][0]);
		textureRegions.put("enemy", tmp[0][1]);
		textureRegions.put("bomb", tmp[0][2]);
		textureRegions.put("box", tmp[0][3]);
		textureRegions.put("floor", tmp[0][4]);
		textureRegions.put("brick", tmp[0][5]);
		textureRegions.put("wall", tmp[0][6]);
		textureRegions.put("corner", tmp[0][7]);
	}
	
	public void render() {				
		spriteBatch.begin();
		drawFloor();
		drawBricks();
		drawPlayer() ;
		spriteBatch.end();			 
	}
	
	private void drawFloor() {
		for (int x = 1; x < world.height - 1; x++) {
		    for (int y = 1; y < world.width - 1; y++) {
		    	spriteBatch.draw(textureRegions.get("floor"), 
		    			x* ppuX, y * ppuY, 1f * ppuX, 1f * ppuY);
				
		    }
		}
		
		for (int x = 1; x < world.height - 1; x++) {

			spriteBatch.draw(textureRegions.get("wall"), 
					x* ppuX, 0, 1f * ppuX / 2f, 1f * ppuX / 2f,
					1f * ppuX, 1f * ppuY, 1, 1,
					90, false);
			
			spriteBatch.draw(textureRegions.get("wall"), 
	    			x* ppuX, (world.height-1) * ppuY, 1f * ppuX, 1f * ppuY);
			
			spriteBatch.draw(textureRegions.get("wall"), 
					0, x * ppuY, 1f * ppuX / 2f, 1f * ppuX / 2f,
					1f * ppuX, 1f * ppuY, 1, 1,
					0, false);
			
			spriteBatch.draw(textureRegions.get("wall"), 
					(world.width-1)* ppuX, x * ppuY, 1f * ppuX / 2f, 1f * ppuX / 2f,
					1f * ppuX, 1f * ppuY, 1, 1,
					180, false);
		}
		

		spriteBatch.draw(textureRegions.get("corner"), 
				0, 0, 1f * ppuX / 2f, 1f * ppuX / 2f,
				1f * ppuX, 1f * ppuY, 1, 1,
				90, false);
		spriteBatch.draw(textureRegions.get("corner"), 
				0, (world.height-1)* ppuX, 1f * ppuX / 2f, 1f * ppuX / 2f,
				1f * ppuX, 1f * ppuY, 1, 1,
				0, false);
		spriteBatch.draw(textureRegions.get("corner"), 
				(world.width-1)* ppuX, 0, 1f * ppuX / 2f, 1f * ppuX / 2f,
				1f * ppuX, 1f * ppuY, 1, 1,
				180, false);
		spriteBatch.draw(textureRegions.get("corner"), 
				(world.width-1)* ppuX, (world.height-1)* ppuX,
				 1f * ppuX / 2f, 1f * ppuX / 2f,
				 1f * ppuX, 1f * ppuY, 1, 1,
					270, false);
		
	}
	
	private void drawBricks() {
		//renderer.setProjectionMatrix(cam.combined);
		//renderer.begin(ShapeType.FilledRectangle);
		for (Brick brick : world.getBricks()) {
			/*Rectangle rect =  brick.getBounds();
			float x1 =  brick.getPosition().x + rect.x;
			float y1 =  brick.getPosition().y + rect.y;
			renderer.setColor(new Color(0, 0, 0, 1));
			renderer.filledRect(x1, y1, rect.width, rect.height);*/
			spriteBatch.draw(textureRegions.get("brick"), brick.getPosition().x* ppuX, brick.getPosition().y * ppuY,Brick.SIZE * ppuX, Brick.SIZE * ppuY);
		}
		
		//renderer.end();
	}
	
	private void drawPlayer() {
		spriteBatch.draw(textureRegions.get("player"), 
				world.getPlayer().getPosition().x* ppuX - Player.SIZE * ppuX / 2, 
				world.getPlayer().getPosition().y * ppuY - Player.SIZE * ppuX / 2,
				Player.SIZE * ppuX / 2, Player.SIZE * ppuY / 2,
				Player.SIZE * ppuX, Player.SIZE * ppuY, 
				1, 1, 90, true);
		
		/*renderer.setProjectionMatrix(cam.combined);
		Player player = world.getPlayer();
		renderer.begin(ShapeType.Rectangle);
		
		Rectangle rect = player.getBounds();
		float x1 = player.getPosition().x + rect.x;
		float y1 = player.getPosition().y + rect.y;
		renderer.setColor(new Color(1, 0, 0, 1));
		renderer.rect(x1, y1, rect.width, rect.height);
		renderer.end();*/
	}
	
}
