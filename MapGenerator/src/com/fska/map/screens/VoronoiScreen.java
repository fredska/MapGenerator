package com.fska.map.screens;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.fska.map.voronoi.VEdge;
import com.fska.map.voronoi.VPoint;
import com.fska.map.voronoi.VoronoiGenerator;

public class VoronoiScreen implements Screen {

	private List<VEdge> voronoiEdges;
	private List<VPoint> voronoiPoints;
	@Override
	public void render(float delta) {
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		voronoiPoints = new LinkedList<VPoint>();
		voronoiEdges = new LinkedList<VEdge>();
		
		VoronoiGenerator vg = new VoronoiGenerator();
		
		voronoiPoints.add(new VPoint(40,10));
		voronoiPoints.add(new VPoint(30,20));
		voronoiPoints.add(new VPoint(20,30));
		voronoiPoints.add(new VPoint(10,40));
		voronoiEdges = vg.getEdges(voronoiPoints, 400, 400);
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
