package com.fska.map.screens;

import com.badlogic.gdx.Screen;

public enum MapGenScreen {
	VORONOI {
		@Override
		protected Screen getScreenInstance(){
			return new VoronoiScreen();
		}
	};
	protected abstract Screen getScreenInstance();
}
