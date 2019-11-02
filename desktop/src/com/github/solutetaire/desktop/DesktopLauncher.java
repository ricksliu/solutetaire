package com.github.solutetaire.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.solutetaire.SoluteTaire;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SoluteTaire";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new SoluteTaire(), config);
	}
}
