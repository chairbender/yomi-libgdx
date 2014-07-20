package com.chairbender.yomi.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chairbender.yomi.YomiLibgdx;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 900;
        config.width = 1600;
		new LwjglApplication(new YomiLibgdx(), config);
	}
}
