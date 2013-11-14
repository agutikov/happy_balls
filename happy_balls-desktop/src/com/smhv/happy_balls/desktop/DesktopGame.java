package com.smhv.happy_balls.desktop;

import com.smhv.happy_balls.BGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	      LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	      cfg.title = "Happy Balls";
	      cfg.useGL20 = false;
	      cfg.width = 840;
	      cfg.height = 480;
	      
	      cfg.fullscreen = false;
	      cfg.vSyncEnabled = false;
			
	      new LwjglApplication(new BGame(), cfg);

	}

}
