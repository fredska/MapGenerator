package com.fska.map.perlin;

public class Point2f {
	protected float x,y;
	
	public Point2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Point2f sub(Point2f value){
		return new Point2f(this.x - value.x, this.y - value.y);
	}
	
	public float dot(Point2f value){
		return this.x * value.x + this.y * value.y;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
