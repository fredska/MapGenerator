package com.fska.map.voronoi;

public class VParabola {

	/*
	isLeaf		: flag whether the node is Leaf or internal node
	site		: pointer to the focus point of parabola (when it is parabola)
	edge		: pointer to the edge (when it is an edge)
	cEvent		: pointer to the event, when the arch disappears (circle event)
	parent		: pointer to the parent node in tree
    */
	public boolean isLeaf;
	public VPoint site;
	public VEdge edge;
	public VEvent cEvent;
	public VParabola parent;
	
	private VParabola _left, _right;
	
	public VParabola(){
		this.isLeaf = false;
		this.site = null;
		this.edge = null;
		this.cEvent = null;
		this.parent = null;
	}
	
	public VParabola(VPoint site){
		this.site = site;
		this.isLeaf = true;
		this.edge = null;
		this.cEvent = null;
		this.parent = null;
	}
	
	public void setLeft(VParabola p){
		_left = p;
		p.parent = this;
	}
	
	public void setRight(VParabola p){
		_right = p;
		p.parent = this;
	}
	
	public VParabola Left(){ return this._left;}
	public VParabola Right(){ return this._right;}
	
	public static VParabola getLeft(VParabola p){
		return getLeftChild(p);
	}
	public static VParabola getRight(VParabola p){
		return getRightChild(p);
	}
	
	public static VParabola getLeftParent(VParabola p){
		VParabola par = p.parent;
		VParabola pLast = p;
		if(par == null) return par;
		while(par.Left().equals(pLast)){
			if(par.parent == null) return null;
			pLast = par;
			par = par.parent;
		}
		return par;
	}
	
	public static VParabola getRightParent(VParabola p){
		VParabola par = p.parent;
		VParabola pLast = p;
		if(par == null) return null;
		while(par.Right().equals(pLast)){
			if(par.parent == null) return null;
			pLast = par;
			par = par.parent;
		}
		return par;
	}
	
	public static VParabola getLeftChild(VParabola p){
		if(p == null) return null;
		VParabola par = p.Left();
		if(par == null) return par;
		while(!par.isLeaf) {
			par = par.Right();
			if(par == null) return null;
		}
		return par;
	}
	
	public static VParabola getRightChild(VParabola p){
		if(p == null) return null;
		VParabola par = p.Right();
		if(par == null) return par;
		while(!par.isLeaf) {
			par = par.Left();
			if(par == null) return null;
		}
		return par;
	}
}
