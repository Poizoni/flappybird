package com.poizoni.flappygame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.poizoni.flappygame.FlappyBird;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(FlappyBird.WIDTH, FlappyBird.HEIGHT);
		config.setForegroundFPS(60);
		config.setTitle(FlappyBird.TITLE);
		config.setResizable(false);
		new Lwjgl3Application(new FlappyBird(), config);
	}
}
