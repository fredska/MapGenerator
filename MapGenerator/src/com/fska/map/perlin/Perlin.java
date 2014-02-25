package com.fska.map.perlin;

import java.util.Random;


public class Perlin {

	private float[][] mapData;

	public Perlin(int width, int height) {
		mapData = new float[width][height];
	}

	public float[][] generateMap() {
		
		return Perlin.generateMap(mapData.length, mapData[0].length);
	}

	public static float[][] generateMap(int width, int height) {
		return generateMap(width, height, (int) (Math.random() * 5000000));
	}

	private static Point2f gX0Y0, gX0Y1, gX1Y0, gX1Y1;
	private static float s, t, u, v;
	private static double randomDirection;

	private static float[][] generateWhiteNoise(int width, int height){
		float[][] result = new float[height][width];
		Random rand = new Random();
		for(int y = 0; y < width; y++){
			for(int x = 0; x < height; x++){
				result[y][x] = rand.nextFloat();
			}
		}
		
		return result;
	}
	
	public static float[][] generateMap(int width, int height, int randomSeed) {
		
		//Get the base noise map;
		
		float maxDifference = 1f;
		float[][] mapData = generateWhiteNoise(width, height);
		
		float[][] smoothNoise = new float[height][width];
		
		
		Random rand = new Random(randomSeed);
		for (int y = 0; y < mapData.length - 1; y++) {
			for (int x = 0; x < mapData[0].length - 1; x++) {
				// Generate four pseudo random points w/ a gradient length of 1
				randomDirection = rand.nextDouble() * (2 * Math.PI);
				gX0Y0 = new Point2f((float) (x + Math.sin(randomDirection)),
						(float) (y + Math.cos(randomDirection)));
				randomDirection = rand.nextDouble() * (2 * Math.PI);
				gX1Y0 = new Point2f(
						(float) (x + 1 + Math.sin(randomDirection)),
						(float) (y + Math.cos(randomDirection)));
				randomDirection = rand.nextDouble() * (2 * Math.PI);
				gX1Y1 = new Point2f(
						(float) (x + 1 + Math.sin(randomDirection)),
						(float) (y + 1 + Math.cos(randomDirection)));
				randomDirection = rand.nextDouble() * (2 * Math.PI);
				gX0Y1 = new Point2f((float) (x + Math.sin(randomDirection)),
						(float) (y + 1 + Math.cos(randomDirection)));

				// Generate a random point from inside the square
				Point2f innerGridPoint = new Point2f(rand.nextFloat() + x,
						rand.nextFloat() + y);

				// For simplicity, calculate the following
				/*
				 * s = g(x0, y0) · ((x, y) - (x0, y0)) , t = g(x1, y0) · ((x, y)
				 * - (x1, y0)) , u = g(x0, y1) · ((x, y) - (x0, y1)) , v = g(x1,
				 * y1) · ((x, y) - (x1, y1)) .
				 */

				s = gX0Y0.dot(innerGridPoint.sub(new Point2f(x, y)));
				t = gX1Y0.dot(innerGridPoint.sub(new Point2f(x + 1, y)));
				u = gX1Y1.dot(innerGridPoint.sub(new Point2f(x + 1, y + 1)));
				v = gX0Y1.dot(innerGridPoint.sub(new Point2f(x, y + 1)));

				// And the following
				/*
				 * Sx = 3(x - x0)² - 2(x - x0)³ a = s + Sx(t - s) b = u + Sx(v -
				 * u)
				 */

				float x_x0 = innerGridPoint.x - gX0Y0.x;

				float Sx =getEaseCurveResult(x_x0);
				float a = s + getEaseCurveResult(t - s);
				float b = u + getEaseCurveResult(v-u);
				
				
				//Find the y dimension weight, which will result in z!!
				float Sy = getEaseCurveResult(innerGridPoint.y - gX0Y0.y);
				float z = a + Sy * (b - a);
				if(z >= 0)
					mapData[y][x] = z;
				else 
					mapData[y][x] = -z;
			}
		}
		
		return mapData;
	}

	private static float getEaseCurveResult(float a) {
//		return 3 * (a * a) + 2 * (a * a * a);
		return (6 * (a * a* a* a* a)) - (15 * (a * a * a* a)) + (10 * (a* a* a));
	}

}
