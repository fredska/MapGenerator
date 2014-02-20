package com.fska.map;

import com.badlogic.gdx.Game;
import com.fska.map.screens.MapGenScreen;
import com.fska.map.screens.ScreenManager;

public class MainMapGenerator extends Game {
	@Override
	public void create() {		
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(MapGenScreen.VORONOI);
	}

	@Override
	public void dispose() {
		super.dispose();
		ScreenManager.getInstance().dispose();
	}
}
