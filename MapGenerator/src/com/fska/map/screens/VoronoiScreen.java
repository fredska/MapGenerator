package com.fska.map.screens;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.fska.map.voronoi.VEdge;
import com.fska.map.voronoi.VPoint;
import com.fska.map.voronoi.VoronoiGenerator;

public class VoronoiScreen implements Screen {

	private List<VEdge> voronoiEdges;
	private List<VPoint> voronoiPoints;
	
	ShapeRenderer renderer;
	private final int width = Gdx.graphics.getWidth();
	private final int height = Gdx.graphics.getHeight();
	@Override
	public void render(float delta) {
		//Clear out the screen with a black background
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.RED);
		for(VPoint point : voronoiPoints){
			renderer.circle(point.x, point.y, 5);
		}
		renderer.end();
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.GRAY);
		for (VEdge edge : voronoiEdges) {
			renderer.line(new Vector2(edge.getStart().x, edge.getStart().y),
					new Vector2(edge.getEnd().x, edge.getEnd().y));
		}
		renderer.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		voronoiPoints = new LinkedList<VPoint>();
		voronoiEdges = new LinkedList<VEdge>();
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		VoronoiGenerator vg = new VoronoiGenerator();
		
		//Initialize points here
		voronoiPoints.add(new VPoint(40, 40));
		voronoiPoints.add(new VPoint(width - 40, height - 40));
		voronoiPoints.add(new VPoint(width / 2, 10));
		
		voronoiEdges = vg.getEdges(voronoiPoints, width, height);
		
		renderer = new ShapeRenderer();
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
