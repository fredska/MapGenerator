package com.fska.map.perlin;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PerlinNoise implements Runnable {

	public double[][] data;

	public static void main(String[] args) {
		Thread mainThread = new Thread(
				new PerlinNoise(2020, 800, 800, 62, 0.5d));
		mainThread.start();
	}

	int width;
	int height;
	double zoom;
	double persistence;
	int picRangeStart;
	int picRangeEnd;

	@Override
	public void run() {
		BufferedImage im = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);
		int octaves = 20;
		for (int picCount = picRangeStart; picCount < picRangeEnd; picCount++) {

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					double getNoise = 0;
					for (int a = 0; a < octaves; a++) {
						double frequency = Math.pow(2, a);
						double amplitude = Math.pow(persistence, a);
						getNoise += noise(((double) x + picCount) * frequency
								/ zoom, ((double) y + picCount) / zoom
								* frequency)
								* amplitude;
					}
					int color = (int) ((getNoise * 192) + 128); // Convert
																// to
																// roughly
																// 256
																// values

					if (color > 255) {
						color = 255;
					}
					if (color < 0) {
						color = 0;
					}
					im.setRGB(x, y, new Color(color).getRGB());
					// // Create elevation colors;
					// // Deep sea
					// if (color > 0 && color <= 25) {
					// im.setRGB(x, y, Color.BLUE.getRGB());
					// }
					// // // Shoreline
					// else if (color > 25 && color <= 75) {
					// im.setRGB(x, y, 0x439FFA);
					// }
					// // // Desert Color
					// else if (color > 75 && color <= 85) {
					// im.setRGB(x, y, 0xFCE54E);
					// } else if (color > 85 && color <= 100) {
					// im.setRGB(x, y, 0xFCE54E);
					// }
					// // // Moving onto Forest
					// else if (color > 100 && color <= 175) {
					// im.setRGB(x, y, 0x49AC3E);
					// }
					// // //Rocky Hills
					// else if (color > 175 && color <= 200) {
					// im.setRGB(x, y, 0x5E9059);
					// }
					// else if (color > 200 && color <= 245) {
					// im.setRGB(x, y,Color.gray.getRGB());
					// }
					// // //MountainPeak
					// else if (color > 245 && color <= 255) {
					// im.setRGB(x, y, Color.WHITE.getRGB());
					// }

					// if(color > 100 && color <= 150){
					// im.setRGB(x, y, Color.LIGHT_GRAY.getRGB());
					// }

				}
			}

			/* Save the image. */
			try {
				if (picCount < 10) {
					ImageIO.write(im, "JPG", new File("data/out_00" + picCount
							+ ".jpg"));
				} else if (picCount < 100) {
					ImageIO.write(im, "JPG", new File("data/out_0" + picCount
							+ ".jpg"));
				} else {
					ImageIO.write(im, "JPG", new File("data/out_" + picCount
							+ ".jpg"));
				}
			} catch (IOException e) {
				System.out.println("Failed to write image.");
				System.exit(-1);
			}

			System.out.println("Picture #" + picCount + " complete.");
		}
	}

	// Print out a range of pictures
	public PerlinNoise(int seed, int width, int height, double zoom,
			double persistence, int picRangeStart, int picRangeEnd) {
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.persistence = persistence;
		this.picRangeStart = picRangeStart;
		this.picRangeEnd = picRangeEnd;
		this.seed = seed;
	}

	// For just a default picture
	public PerlinNoise(int seed, int width, int height, double zoom,
			double persistence) {
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		this.persistence = persistence;
		this.picRangeStart = 0;
		this.picRangeEnd = 1;
		this.seed = seed;
	}

	private int seed;

	// Following methods have been shamelessly plugged from
	// http://www.dreamincode.net/forums/topic/66480-perlin-noise/
	public double findNoise2(double x, double y) {
		int n = (int) x + (int) y * 57;
		n = (n << 13) ^ n;
		// int nn = (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
		int nn = (n * (n * n * 60493 + 19990303) + seed) & 0x7fffffff;
		return 1.0 - ((double) nn / 1073741824.0);
	}
	
	public double noise(double x, double y, int octaves){
		double getNoise = 0;
		for (int a = 0; a < octaves; a++) {
			double frequency = Math.pow(2, a);
			double amplitude = Math.pow(persistence, a);
			getNoise += noise(((double) x) * frequency
					/ zoom, ((double) y) / zoom
					* frequency)
					* amplitude;
		}
		return getNoise;
	}

	private double interpolate(double a, double b, double x) {
		double ft = x * Math.PI;
		double f = (1.0 - Math.cos(ft)) * 0.5;
		return a * (1 - f) + b * f;
	}

	// The actual workhorse for a 2d picture
	public double noise(double x, double y) {
		double floorx = (double) ((int) x);// This is kinda a cheap way to floor
											// a double integer.
		double floory = (double) ((int) y);
		double s, t, u, v;// Integer declaration
		s = findNoise2(floorx, floory);
		t = findNoise2(floorx + 1, floory);
		u = findNoise2(floorx, floory + 1);// Get the surrounding pixels to
											// calculate the transition.
		v = findNoise2(floorx + 1, floory + 1);
		double int1 = interpolate(s, t, x - floorx);// Interpolate between the
													// values.
		double int2 = interpolate(u, v, x - floorx);// Here we use x-floorx, to
													// get 1st dimension.
													// Don't mind the
													// x-floorx thingie,
													// it's part of the
													// cosine formula.
		return interpolate(int1, int2, y - floory);// Here we use y-floory, to
													// get the 2nd dimension.
	}
}