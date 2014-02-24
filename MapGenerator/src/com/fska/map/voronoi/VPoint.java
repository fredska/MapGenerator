package com.fska.map.voronoi;

public class VPoint {
	public float x, y;

	public VPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof VPoint)) return false;
		VPoint vPoint = (VPoint)obj;
		return (vPoint.x == this.x) && (vPoint.y == this.y);
	}
	
	@Override
	public String toString(){
		return "(" + x + "," + y + ")";
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
