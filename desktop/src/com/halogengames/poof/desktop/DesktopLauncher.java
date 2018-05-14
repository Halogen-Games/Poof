package com.halogengames.poof.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.halogengames.poof.Poof;

@SuppressWarnings("WeakerAccess")
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Poof";
		config.width = Poof.V_WIDTH;
		config.height = Poof.V_HEIGHT;
//		config.samples = 3;
		new LwjglApplication(new Poof(), config);
	}
}
