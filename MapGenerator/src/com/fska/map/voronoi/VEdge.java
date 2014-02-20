package com.fska.map.voronoi;
/**
A class that stores an edge in Voronoi diagram

start		: pointer to start point
end			: pointer to end point
left		: pointer to Voronoi place on the left side of edge
right		: pointer to Voronoi place on the right side of edge

neighbour	: some edges consist of two parts, so we add the pointer to another part to connect them at the end of an algorithm

direction	: directional vector, from "start", points to "end", normal of |left, right|
f, g		: directional coeffitients satisfying equation y = f*x + g (edge lies on this line)
*/
public class VEdge {

	private VPoint start, end, direction, left, right;
	
	private float f, g;
	
	private VEdge neighbour;
	
	/*
	Constructor of the class

	start		: pointer to start
	left		: pointer to left place
	right		: pointer to right place
    */
	public VEdge(VPoint start, VPoint left, VPoint right){
		this.start = start;
		this.left = left;
		this.right = right;
		this.neighbour = null;
		this.end = null;
		
		f = (right.getX() - left.getX()) / (left.getY() - right.getY());
		g = start.getY() - f * start.getX();
		direction = new VPoint(right.getY() - left.getY(), -(right.getX() - left.getX()));
	}

	public VPoint getStart() {
		return start;
	}

	public void setStart(VPoint start) {
		this.start = start;
	}

	public VPoint getEnd() {
		return end;
	}

	public void setEnd(VPoint end) {
		this.end = end;
	}

	public VPoint getDirection() {
		return direction;
	}

	public void setDirection(VPoint direction) {
		this.direction = direction;
	}

	public VPoint getLeft() {
		return left;
	}

	public void setLeft(VPoint left) {
		this.left = left;
	}

	public VPoint getRight() {
		return right;
	}

	public void setRight(VPoint right) {
		this.right = right;
	}

	public float getF() {
		return f;
	}

	public void setF(float f) {
		this.f = f;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}

	public VEdge getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(VEdge neighbour) {
		this.neighbour = neighbour;
	}
}
