package com.myproject.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.myproject.game.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MainGame(), config);

		// WINDOW SIZE
		config.width = 1280;
		config.height = 720;
		config.vSyncEnabled = false;
		//config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		config.resizable = false;
		config.fullscreen = false;
	}
}
