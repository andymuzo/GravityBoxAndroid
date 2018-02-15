package com.anidari.gravity_box;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "gravity_box";
		cfg.useGL20 = false;
		cfg.width = 960;
		cfg.height = 640;
		
		new LwjglApplication(new GravityBox(), cfg);
	}
}
