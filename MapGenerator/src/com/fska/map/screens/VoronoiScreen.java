package com.fska.map.screens;

import java.util.LinkedList;
import java.util.List;

import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fska.map.voronoi.VEdge;
import com.fska.map.voronoi.VPoint;
import com.fska.map.voronoi.VoronoiGenerator;

public class VoronoiScreen implements Screen {

	private List<VEdge> voronoiEdges;
	private List<VPoint> voronoiPoints;
	
	//Online version of Fortune's Algorithm
	//http://ageeksnotes.blogspot.com/2010/11/fast-java-implementation-fortunes.html
	List<GraphEdge> beHumphreysVoronoiEdges;
	private OrthographicCamera camera;
	
	ShapeRenderer renderer;
	private final int width = Gdx.graphics.getWidth();
	private final int height = Gdx.graphics.getHeight();
	
	private Voronoi humphreyVoronoiGenerator;
	List<Double> xInputs, yInputs;
	@Override
	public void render(float delta) {
		//Clear out the screen with a black background
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		updateCamera(delta);
		camera.update();
		
		//Add a floating dot based on the mouse cursor's position.  If left button is clicked, add the point to the list permanently
		double cursorX = Gdx.input.getX(), cursorY = height - Gdx.input.getY();
		xInputs.add(cursorX); yInputs.add(cursorY);
		beHumphreysVoronoiEdges = humphreyVoronoiGenerator.generateVoronoi(xInputs, yInputs, 0, width, 0, height);
		xInputs.remove(xInputs.size()-1); yInputs.remove(yInputs.size()-1);
		renderer.setProjectionMatrix(camera.combined);
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
		
		renderer.setColor(Color.GREEN);
		for(GraphEdge edge : beHumphreysVoronoiEdges){
			renderer.line(new Vector2((float)edge.x1, (float)edge.y1), new Vector2((float)edge.x2, (float)edge.y2));
		}
		renderer.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		camera = new OrthographicCamera(width, height);
		camera.position.set(new Vector3(width/2, height/2, 0));
		camera.update();
		voronoiPoints = new LinkedList<VPoint>();
		voronoiEdges = new LinkedList<VEdge>();
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		VoronoiGenerator vg = new VoronoiGenerator();
		
		//Initialize points here
		voronoiPoints.add(new VPoint(width * 0.1f, height * 0.11f));
		voronoiPoints.add(new VPoint(width * 0.1f, height * 0.9f));
		voronoiPoints.add(new VPoint(width / 2f, height / 2f));
		voronoiPoints.add(new VPoint(width * 0.91f, height * 0.15f));
		voronoiPoints.add(new VPoint(width * 0.915f, height * 0.915f));
		
		voronoiEdges = vg.getEdges(voronoiPoints, width, height);
		
		humphreyVoronoiGenerator = new Voronoi(0.00001d);
//		double[] xArrInputs = new double[]{width * 0.1, width * 0.1, width / 2d, width * 0.91, width * 0.91};
//		double[] yArrInputs = new double[]{height * 0.11, height * 0.9, height / 2d, height * 0.15, height * 0.915};
		xInputs = new LinkedList<Double>();
		yInputs = new LinkedList<Double>();
		for(int i = 0; i < 3000; i++){
			xInputs.add(Math.random() * width);
			yInputs.add(Math.random() * height);
		}
		
		renderer = new ShapeRenderer();
	}
	
	public void updateCamera(float delta){
		float modifier = 100f;
		Vector2 translation = new Vector2();
		if(Gdx.input.isKeyPressed(Keys.A)){
			translation.add(-delta * modifier, 0);
		}if(Gdx.input.isKeyPressed(Keys.D)){
			translation.add(delta * modifier, 0);
		}if(Gdx.input.isKeyPressed(Keys.W)){
			translation.add(0, delta * modifier);
		}if(Gdx.input.isKeyPressed(Keys.S)){
			translation.add(0, -delta * modifier);
		}
		camera.translate(translation);
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
