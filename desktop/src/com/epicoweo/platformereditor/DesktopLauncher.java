package com.epicoweo.platformereditor;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	
	public static Lwjgl3Application app;
	
	
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1280, 720);
		config.setWindowSizeLimits(640, 360, 1920, 1080);
		config.setTitle("2DPlatformerEditor");
		app = new Lwjgl3Application(new PlatformerEditor(), config);
	}
}
