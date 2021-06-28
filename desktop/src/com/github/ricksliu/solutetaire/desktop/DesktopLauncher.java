package com.github.ricksliu.solutetaire.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.ricksliu.solutetaire.SoluteTaire;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SoluteTaire";
		// config.fullscreen = true;
		// config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		// config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.width = 4 * LwjglApplicationConfiguration.getDesktopDisplayMode().width / 5;
		config.height = 4 * LwjglApplicationConfiguration.getDesktopDisplayMode().height / 5;
		new LwjglApplication(new SoluteTaire(), config);
	}
}
