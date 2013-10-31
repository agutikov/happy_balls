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
	      cfg.useGL20 = true;
	      cfg.width = 512;
	      cfg.height = 512;
			
	      new LwjglApplication(new BGame(), cfg);

	}

}
