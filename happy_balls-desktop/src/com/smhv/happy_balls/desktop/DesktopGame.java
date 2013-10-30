package com.smhv.happy_balls.desktop;

import com.smhv.happy_balls.Game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LwjglApplication(new Game(), "Game", 1024, 600, false);

	}

}
