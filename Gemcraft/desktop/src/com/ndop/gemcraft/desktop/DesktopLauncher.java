package com.ndop.gemcraft.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ndop.gemcraft.framework.view.MyGemcraft;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Gemcraft";
		
		new LwjglApplication(new MyGemcraft(), config);
	}
}
