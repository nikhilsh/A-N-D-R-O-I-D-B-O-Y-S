package com.androidboys.spellarena.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.ggps.DesktopGoogleServices;
import com.androidboys.spellarena.ggps.DesktopResolver;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpellArena(new DesktopResolver()), config);
	}
}
