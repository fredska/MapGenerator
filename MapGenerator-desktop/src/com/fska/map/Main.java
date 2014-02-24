package com.fska.map;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MapGenerator";
		cfg.useGL20 = false;
		cfg.width = 600;
		cfg.height = 600;
		
		new LwjglApplication(new MainMapGenerator(), cfg);
	}
}
