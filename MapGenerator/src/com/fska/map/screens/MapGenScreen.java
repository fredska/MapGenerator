package com.fska.map.screens;

import com.badlogic.gdx.Screen;

public enum MapGenScreen {
	VORONOI {
		@Override
		protected Screen getScreenInstance(){
			return new VoronoiScreen();
		}
	},
	PERLIN{
		@Override
		protected Screen getScreenInstance(){
			return new PerlinNoiseScreen();
		}
	};
	protected abstract Screen getScreenInstance();
}
