package com.fska.map.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fska.map.perlin.PerlinNoise;

public class PerlinNoiseScreen implements Screen {

	double[][][] perlinNoiseMapData;
	
	
	private final int width = 512;
	private final int height = 512;
	
	private SpriteBatch batch;
	private boolean hasChange;
	private boolean buttonPressed = false;
	Pixmap pixMap;
	Texture texture;
	private int zoom = 0;
	private boolean preProcessComplete = false;
	
	private final int zoomIterations = 50;
	@Override
	public void render(float delta) {
		//Clear out the screen with a black background
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isKeyPressed(Keys.UP) ){//&& !buttonPressed){
			zoom = (zoom < (zoomIterations-1))? zoom + 1: zoom;
			hasChange = true;
			buttonPressed = true;
			System.out.println("New Zoom: " + zoom);
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) ){//&& !buttonPressed){
			zoom = (zoom > 0)? zoom - 1: zoom;
			hasChange = true;
			buttonPressed = true;
			System.out.println("New Zoom: " + zoom);
		}
		if(!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN))
			buttonPressed = false;
		
		if(hasChange && preProcessComplete){
			System.out.println("Updating pixMap..");
			hasChange = false;
			pixMap = new Pixmap(width, height, Format.RGBA8888);
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					Color color = Color.BLACK;
					double noise = perlinNoiseMapData[zoom][x][y];
					
					if(noise >= -1 && noise < -0.25d)
						color = Color.BLUE;
					if(noise > -0.25d && noise <= 0.0d)
						color = Color.ORANGE;
					if(noise > 0 && noise <= 0.85d)
						color = Color.GREEN;
					if(noise > 0.85d)
						color = Color.GRAY;
					
					pixMap.setColor(color);
					pixMap.drawPixel(x, y);
				}
			}
			texture = new Texture(pixMap);
		}
		if(preProcessComplete){
		batch.begin();
		batch.draw(texture, 
				(Gdx.graphics.getWidth() - width) / 2,
				(Gdx.graphics.getHeight() - height) / 2);
		batch.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
//		perlinNoiseMapData = Perlin.generateMap(width, height);
		perlinNoiseMapData = new double[zoomIterations][width][height];
		batch = new SpriteBatch();
		hasChange = true;
		pixMap = new Pixmap(width, height, Format.RGBA8888);
		
		Thread[] group = new Thread[10];
		int maxIteration = (zoomIterations / group.length);
		for(int threadCount = 0; threadCount < group.length; threadCount++){
			group[threadCount] = createThreadTeam(threadCount * maxIteration
					, maxIteration + (threadCount * maxIteration));
		}
		
		try{
			for(Thread thread : group)
				thread.join();
			for(Thread thread : group)
				thread.start();
		} catch(InterruptedException ie){
			ie.printStackTrace();
		}
	}
	
	public Thread createThreadTeam(final int minIteration, final int maxIteration){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				double[][] mapData = new double[width][height];
				for (int i = minIteration; i < maxIteration; i++) {
					mapData = new double[width][height];
					PerlinNoise pn = new PerlinNoise(2020, width, height,
							10 + (i), 0.3d);
					for (int x = 0; x < width; x++) {
						for (int y = 0; y < height; y++) {
							mapData[x][y] = pn.noise(x, y, 6);
						}
					}
					updateNoiseMapData(i, mapData);
				}
				preProcessComplete = true;
			}
		});
		return thread;
	}
	
	public synchronized void updateNoiseMapData(int index, double[][] mapData){
		System.out.println("Index #: " + index + " Done");
		perlinNoiseMapData[index] = mapData;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
